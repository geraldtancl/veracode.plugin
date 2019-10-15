package com.veracode.cliang.sastPlugin.runConfig.repoSupport;

import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitRepo implements CodeRepo {

    private Repository repo;

    @Override
    public String obtainBranchName() {

        try {
            System.out.println("Current branch name: " + repo.getBranch());
            return repo.getBranch();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }


        return false;
    }
}
