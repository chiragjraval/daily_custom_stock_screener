package com.chirag.stockscreener.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client utility for fetching web pages with retry logic
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final int MAX_RETRIES = 5;
    // initial backoff delay (4 seconds)
    private static final long INITIAL_BACKOFF_MS = 2_000L;
    // optional cap to avoid unbounded delays
    private static final long MAX_BACKOFF_MS = 60_000L;

    /**
     * Fetch HTML content from a URL with retry logic
     * @param url The URL to fetch
     * @return HTML content as string, or null if fetch fails after retries
     */
    public static String fetchHtml(String url) {
        int retries = 0;
        long backoff = INITIAL_BACKOFF_MS;

        while (retries < MAX_RETRIES) {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .header("User-Agent", USER_AGENT)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String html = response.body().string();
                        logger.info("Successfully fetched: {}", url);
                        return html;
                    }

                    int code = response.code();

                    if (code == 429) {
                        // Too Many Requests: exponential backoff
                        retries++;
                        logger.warn("Received 429 for {}. Retry {}/{}. Backing off {} ms", url, retries, MAX_RETRIES, backoff);
                        if (retries < MAX_RETRIES) {
                            try {
                                // add small jitter to avoid thundering herd
                                long jitter = (long) (Math.random() * 500);
                                Thread.sleep(Math.min(backoff + jitter, MAX_BACKOFF_MS));
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                            backoff = Math.min(backoff * 2, MAX_BACKOFF_MS);
                            continue;
                        } else {
                            break;
                        }
                    } else if (code >= 500) {
                        // Server error: retry with backoff
                        retries++;
                        logger.warn("Server error {} for {}. Retry {}/{}. Backing off {} ms", code, url, retries, MAX_RETRIES, backoff);
                        if (retries < MAX_RETRIES) {
                            try {
                                long jitter = (long) (Math.random() * 500);
                                Thread.sleep(Math.min(backoff + jitter, MAX_BACKOFF_MS));
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                            backoff = Math.min(backoff * 2, MAX_BACKOFF_MS);
                            continue;
                        } else {
                            break;
                        }
                    } else {
                        // Non-retryable client error (other 4xx) or other non-success: do not retry
                        logger.warn("Failed to fetch {}: HTTP {}", url, code);
                        break;
                    }
                }
            } catch (IOException e) {
                retries++;
                logger.warn("Attempt {} failed for URL {}: {}", retries, url, e.getMessage());
                if (retries < MAX_RETRIES) {
                    try {
                        long jitter = (long) (Math.random() * 500);
                        Thread.sleep(Math.min(backoff + jitter, MAX_BACKOFF_MS));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    backoff = Math.min(backoff * 2, MAX_BACKOFF_MS);
                }
            }
        }
        logger.error("Failed to fetch {} after {} retries", url, MAX_RETRIES);
        return null;
    }
}
