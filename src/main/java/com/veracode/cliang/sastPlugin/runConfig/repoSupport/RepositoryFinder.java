package com.veracode.cliang.sastPlugin.runConfig.repoSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RepositoryFinder {

    public static CodeRepo findRepoInUsed() {

        // Add supported repo here
        List<CodeRepo> supportedRepos = new ArrayList<>();
        supportedRepos.add(new GitRepo());

        for (CodeRepo repo: supportedRepos) {
            if (repo.isIAmRepoInUsed()) {
                return repo;
            }
        }

        return null;
    }

}
