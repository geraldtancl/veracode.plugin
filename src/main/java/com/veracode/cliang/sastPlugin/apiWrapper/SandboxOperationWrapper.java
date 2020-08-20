package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.apiwrapper.wrappers.SandboxAPIWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.Sandboxlist;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class SandboxOperationWrapper {

    private static final Class c = SandboxOperationWrapper.class;

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Sandboxlist getSandboxListing(String appId) {
        SandboxAPIWrapper sandboxWp = new SandboxAPIWrapper();
        sandboxWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Sandboxlist sandboxes = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = sandboxWp.getSandboxList(appId);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Sandboxlist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            sandboxes = (Sandboxlist) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }

        return sandboxes;
    }



}
