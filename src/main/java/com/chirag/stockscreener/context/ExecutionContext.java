package com.chirag.stockscreener.context;

import com.chirag.stockscreener.enums.Environment;

public interface ExecutionContext {


    /* Get the current execution environment */
    Environment getEnvironment();

    /* Get the maximum number of pages to scan */
    int getMaxPagesToScan();

    /* Check if Git commit is enabled */
    boolean isGitCommitEnabled();

    /* Get the screener link to be processed */
    String getScreenerQueryLink();

    /* Get the output directory for scraped data */
    String getOutputDirectory();

    /* Get the Git file pattern for committing changes */
    String getGitFilePattern();
}
