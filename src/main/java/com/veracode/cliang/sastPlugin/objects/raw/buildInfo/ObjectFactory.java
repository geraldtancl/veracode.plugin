//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 12:50:20 AM WIB 
//


package com.veracode.cliang.sastPlugin.objects.raw.buildInfo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the https.analysiscenter_veracode_com.schema._4_0.buildinfo package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.analysiscenter_veracode_com.schema._4_0.buildinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Buildinfo }
     * 
     */
    public Buildinfo createBuildinfo() {
        return new Buildinfo();
    }

    /**
     * Create an instance of {@link BuildType }
     * 
     */
    public BuildType createBuildType() {
        return new BuildType();
    }

    /**
     * Create an instance of {@link AnalysisUnitType }
     * 
     */
    public AnalysisUnitType createAnalysisUnitType() {
        return new AnalysisUnitType();
    }

}
