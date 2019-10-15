package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.apiwrapper.wrappers.*;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.Buildlist;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class ApplicationOperationWrapper {

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Applist getApplicationListing() {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Applist applications = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.getAppList("true");

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Applist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            applications = (Applist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return applications;
    }

    public static Buildlist getScansListing(String appId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildlist scansListing = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.getBuildList(appId);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildlist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            scansListing = (Buildlist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return scansListing;
    }

    public static Buildlist getScansListing(String appId, String sandboxId) {
        UploadAPIWrapper uploadWp = new UploadAPIWrapper();
        uploadWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Buildlist scansListing = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = uploadWp.getBuildList(appId, sandboxId);

            System.out.println(xmlOutput);

            jaxbContext = JAXBContext.newInstance(Buildlist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            scansListing = (Buildlist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return scansListing;
    }

}
