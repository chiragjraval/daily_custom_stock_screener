package com.chirag.stockscreener.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for managing Git operations like committing generated files
 */
public class GitService {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class);
    private static final String COMMIT_MESSAGE_TEMPLATE = "Update screener data - %s";

    /**
     * Commit changes in the repository
     * @param repositoryPath Path to the Git repository root
     * @param filePattern File pattern to add (e.g., "screener-data/*")
     * @return true if commit was successful, false otherwise
     */
    public static boolean commitAndPushChanges(String repositoryPath, String filePattern) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();

            try (Repository repository = builder.setGitDir(new File(repositoryPath, ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build(); Git git = new Git(repository)) {

                // Add files matching pattern
                git.add()
                        .addFilepattern(filePattern)
                        .call();

                logger.info("Added files matching pattern: {}", filePattern);

                // Check if there are changes to commit
                if (git.status().call().hasUncommittedChanges()) {
                    String commitMessage = String.format(COMMIT_MESSAGE_TEMPLATE,
                            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

                    git.commit()
                            .setMessage(commitMessage)
                            .call();

                    git.push()
                            .setCredentialsProvider(getGitToken())
                            .call();

                    logger.info("Committed changes with message: {}", commitMessage);
                } else {
                    logger.info("No changes to commit");
                }
                return true;
            }
        } catch (IOException e) {
            logger.error("IO error during Git operations", e);
            return false;
        } catch (GitAPIException e) {
            logger.error("Git API error during commit", e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during Git operations", e);
            return false;
        }
    }

    private static CredentialsProvider getGitToken() {
        // Prefer GitHub Actions token
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken != null && !githubToken.isBlank()) {
            String actor = System.getenv("GITHUB_ACTOR");
            if (actor == null || actor.isBlank()) {
                actor = "x-access-token";
            }
            return new UsernamePasswordCredentialsProvider(actor, githubToken);
        }

        // Fallback to explicit username/password env vars for local use
        String user = System.getenv("GIT_USERNAME");
        String pass = System.getenv("GIT_PASSWORD");
        if (user != null && !user.isBlank() && pass != null && !pass.isBlank()) {
            return new UsernamePasswordCredentialsProvider(user, pass);
        }

        // No credentials provided; caller will try unauthenticated push (SSH or credential helper may succeed)
        return null;
    }

    /**
     * Check if directory is a Git repository
     * @param repositoryPath Path to check
     * @return true if it's a Git repository
     */
    public static boolean isGitRepository(String repositoryPath) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(repositoryPath, ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            repository.close();
            return true;
        } catch (Exception e) {
            logger.debug("Not a Git repository: {}", repositoryPath);
            return false;
        }
    }
}

