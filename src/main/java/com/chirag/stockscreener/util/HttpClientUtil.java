package com.chirag.stockscreener.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * HTTP client utility for fetching web pages with retry logic
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    private final int maxRetries;
    private final long initialBackoffMillis;
    private final long maxBackoffMillis;
    private final OkHttpClient client;

    public HttpClientUtil(int maxRetries, long initialBackoffMillis, long maxBackoffMillis) {
        this.maxRetries = maxRetries;
        this.initialBackoffMillis = initialBackoffMillis;
        this.maxBackoffMillis = maxBackoffMillis;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Fetch HTML content from a URL with retry logic
     * @param url The URL to fetch
     * @return HTML content as string, or null if fetch fails after retries
     */
    public String fetch(String url) {
        int retries = 0;
        long backoff = this.initialBackoffMillis;

        while (true) {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .header("User-Agent", USER_AGENT)
                        .build();

                try (Response response = this.client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String html = response.body().string();
                        logger.info("Successfully fetched: {}", url);
                        return html;
                    }

                    int code = response.code();

                    if (code == 429 || code >= 500) {
                        throw new IOException(String.format("Received %s for %s", code, url));
                    } else {
                        // Non-retryable client error (other 4xx) or other non-success: do not retry
                        logger.warn("Failed to fetch {}: HTTP {}", url, code);
                        break;
                    }
                }
            } catch (IOException e) {
                retries++;
                logger.warn("Attempt {} failed for URL {}: {}", retries, url, e.getMessage());
                if (retries < this.maxRetries) {
                    long jitterMillis = (long) (Math.random() * 500);
                    long sleepTimeMillis = Math.min(backoff + jitterMillis, this.maxBackoffMillis);
                    logger.info("Retrying after {} ms...", sleepTimeMillis);
                    LockSupport.parkNanos(sleepTimeMillis * 1_000_000);
                    backoff = Math.min(backoff * 2, this.maxBackoffMillis);
                } else {
                    break;
                }
            }
        }

        logger.error("Failed to fetch {} after {} retries", url, this.maxBackoffMillis);
        return null;
    }
}
