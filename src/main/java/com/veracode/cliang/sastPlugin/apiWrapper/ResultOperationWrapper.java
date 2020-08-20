package com.veracode.cliang.sastPlugin.apiWrapper;

import com.intellij.openapi.components.ServiceManager;
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.Detailedreport;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class ResultOperationWrapper {

    private static final Class c = ResultOperationWrapper.class;

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    public static Detailedreport downloadFullResult(String scanId) {
        ResultsAPIWrapper resultWp = new ResultsAPIWrapper();
        resultWp.setUpApiCredentials(API_CREDENTIAL_HOLDER_SERVICE.getApiId(), API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        String xmlOutput = null;
        Detailedreport detailedReport = null;
        JAXBContext jaxbContext = null;

        try {
            xmlOutput = resultWp.detailedReport(scanId);

            PluginLogger.info(c, xmlOutput);

            jaxbContext = JAXBContext.newInstance(Detailedreport.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            detailedReport = (Detailedreport) unmarshaller.unmarshal(new StringReader(xmlOutput));

        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);
        } catch (JAXBException e) {
            PluginLogger.error(c, e.getMessage(), e);
        }
        return detailedReport;
    }

}
