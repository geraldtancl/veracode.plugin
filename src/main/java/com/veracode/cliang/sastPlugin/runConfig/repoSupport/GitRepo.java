package com.veracode.cliang.sastPlugin.runConfig.repoSupport;

import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitRepo implements CodeRepo {

    private static final Class c = GitRepo.class;

    private Repository repo;

    @Override
    public String obtainBranchName() {

        try {
            PluginLogger.info(c, "Current branch name: " + repo.getBranch());
            return repo.getBranch();
        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean isIAmRepoInUsed() {

        File currProjectDir = new File(JetbrainsIdeUtil.getCurrentActiveProject().getBasePath());

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder = repositoryBuilder.setMustExist(true);
        repositoryBuilder = repositoryBuilder.findGitDir(currProjectDir);

        try {
            repo = repositoryBuilder.build();

            return true;
        } catch (Exception e) {
            PluginLogger.error(c, e.getMessage(), e);
        }


        return false;
    }
}
