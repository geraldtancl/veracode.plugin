package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildInfo.Buildinfo;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.Buildlist;
import com.veracode.cliang.sastPlugin.objects.raw.fileList.Filelist;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class ScanOperationWrapper {

    private static final Class c = ScanOperationWrapper.class;

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Buildinfo createSandboxScan(String appId, String scanName, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildinfo buildsInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.createBuild(appId, scanName, null, null, null, null, null, sandboxId);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
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

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
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

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Filelist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            fileList = (Filelist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
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

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
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

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildsInfo = (Buildinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }

        return buildsInfo;
    }

    public static Buildlist deletePolicyScan(String appId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildlist buildList = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.deleteBuild(appId);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildlist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildList = (Buildlist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }

        return buildList;
    }

    public static Buildlist deleteSandboxScan(String appId, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildlist buildList = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.deleteBuild(appId, sandboxId);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildlist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            buildList = (Buildlist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }

        return buildList;
    }
}
