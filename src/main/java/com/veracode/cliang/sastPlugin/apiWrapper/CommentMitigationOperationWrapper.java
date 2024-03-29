package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.apiwrapper.wrappers.MitigationAPIWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.mitigationInfo.Mitigationinfo;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class CommentMitigationOperationWrapper {

    private static final Class c = CommentMitigationOperationWrapper.class;

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Mitigationinfo updateMitigation(String scanId, String action, String comment, String flawList) {
        MitigationAPIWrapper mitigationWp = new MitigationAPIWrapper();
        mitigationWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Mitigationinfo mitigationInfo = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = mitigationWp.updateMitigationInfo(scanId, action, comment, flawList);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Mitigationinfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            mitigationInfo = (Mitigationinfo) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }
        return mitigationInfo;
    }


}
