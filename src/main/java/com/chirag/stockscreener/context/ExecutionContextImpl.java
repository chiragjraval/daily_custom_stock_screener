package com.chirag.stockscreener.context;

import com.chirag.stockscreener.enums.Environment;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExecutionContextImpl implements ExecutionContext {

    private final Environment environment;
    private final Integer maxPagesToScan;
    private final Boolean gitCommitEnabled;
    private final String screenerQueryLink;
    private final String outputDirectory;
    private final String gitFilePattern;

    public ExecutionContextImpl(String[] args) throws ParseException, IOException {
        Options cliOptions = getCliOptions();
        CommandLine cmd = getParsedCommandLine(args, cliOptions);

        this.environment = getOptionValueOrDefault(cmd, "e", val -> Environment.valueOf(val.toUpperCase()), () -> Environment.TEST);
        this.screenerQueryLink = getOptionValueOrDefault(cmd, "sql", Function.identity(), () -> "https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin");
        this.maxPagesToScan = this.environment.getMaxPagesToScan();
        this.gitCommitEnabled = this.environment.isGitCommitEnabled();
        this.outputDirectory = this.environment.getOutputDirectory();
        this.gitFilePattern = this.environment.getGitFilePattern();
    }

    private <T> T getOptionValueOrDefault(CommandLine cmd, String option, Function<String, T> optionToMapper, Supplier<T> defaultSupplier) {
        if (cmd.hasOption(option)) {
            String value = cmd.getOptionValue(option);
            return optionToMapper.apply(value);
        }
        return defaultSupplier.get();
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public int getMaxPagesToScan() {
        return maxPagesToScan;
    }

    @Override
    public boolean isGitCommitEnabled() {
        return gitCommitEnabled;
    }

    @Override
    public String getScreenerQueryLink() {
        return screenerQueryLink;
    }

    @Override
    public String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public String getGitFilePattern() {
        return gitFilePattern;
    }

    @Override
    public String toString() {
        return "ExecutionContextImpl{" +
                "environment=" + environment.name() +
                ", maxPagesToScan=" + maxPagesToScan +
                ", gitCommitEnabled=" + gitCommitEnabled +
                ", screenerLink='" + screenerQueryLink + '\'' +
                ", outputDirectory='" + outputDirectory + '\'' +
                ", gitFilePattern='" + gitFilePattern + '\'' +
                '}';
    }

    private Options getCliOptions() {
        Options options = new Options();
        options.addOption("e", "environment", true, "Execution environment (TEST, DEV, PROD)");
        options.addOption("sql", "screenerQueryLink", true, "Screener Query link to be processed");
        return options;
    }

    private CommandLine getParsedCommandLine(String[] args, Options options) throws ParseException {
        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
}
