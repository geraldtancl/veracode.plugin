//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.18 at 05:54:10 PM MYT 
//


package com.veracode.cliang.sastPlugin.objects.raw.mitigationInfo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the https.analysiscenter_veracode_com.schema.mitigationinfo._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.analysiscenter_veracode_com.schema.mitigationinfo._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mitigationinfo }
     * 
     */
    public Mitigationinfo createMitigationinfo() {
        return new Mitigationinfo();
    }

    /**
     * Create an instance of {@link IssueType }
     * 
     */
    public IssueType createIssueType() {
        return new IssueType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link MitigationActionType }
     * 
     */
    public MitigationActionType createMitigationActionType() {
        return new MitigationActionType();
    }

}
