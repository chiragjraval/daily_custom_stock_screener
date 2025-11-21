package com.chirag.stockscreener.enums;

public enum Environment {
    TEST(1, false,
            "src/main/resources/screener-data/%s", "src/main/resources/screener-data/%s/"),
    DEV(1, true,
            "src/main/resources/screener-data/%s", "src/main/resources/screener-data/%s/"),
    PROD(Integer.MAX_VALUE, true,
            "src/main/resources/screener-data/%s", "src/main/resources/screener-data/%s/");

    private final Integer maxPagesToScan;
    private final Boolean gitCommitEnabled;
    private final String outputDirectory;
    private final String gitFilePattern;

    private Environment(int maxPagesToScan, boolean gitCommitEnabled,
                       String outputDirectoryFormat, String gitFilePatternFormat) {
        this.maxPagesToScan = maxPagesToScan;
        this.gitCommitEnabled = gitCommitEnabled;
        this.outputDirectory = String.format(outputDirectoryFormat, this.name().toLowerCase());
        this.gitFilePattern = String.format(gitFilePatternFormat, this.name().toLowerCase());
    }

    public Integer getMaxPagesToScan() {
        return maxPagesToScan;
    }

    public Boolean isGitCommitEnabled() {
        return gitCommitEnabled;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getGitFilePattern() {
        return gitFilePattern;
    }
}
