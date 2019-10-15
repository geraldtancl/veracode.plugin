package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildInfo.Buildinfo;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.Buildlist;
import com.veracode.cliang.sastPlugin.objects.raw.fileList.Filelist;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class ScanOperationWrapper {

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Buildinfo createSandboxScan(String appId, String scanName, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildinfo buildsInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.createBuild(appId, scanName, null, null, null, null, null, sandboxId);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return buildsInfo;
    }

    public static Buildinfo createPolicyScan(String appId, String scanName) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildinfo buildsInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.createBuild(appId, scanName);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return buildsInfo;
    }

    public static Filelist uploadFile(String appId, String sandboxId, String filePath) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Filelist fileList = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.uploadFile(appId, filePath, sandboxId);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Filelist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            fileList = (Filelist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    public static Buildinfo beginPrescanAndStaticScan(String appId, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildinfo buildsInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.beginPreScan(appId, sandboxId, "true");

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return buildsInfo;
    }

    public static Buildinfo getScanStatus(String appId, String scanId, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildinfo buildsInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.getBuildInfo(appId, scanId, sandboxId);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return buildsInfo;
    }
}
