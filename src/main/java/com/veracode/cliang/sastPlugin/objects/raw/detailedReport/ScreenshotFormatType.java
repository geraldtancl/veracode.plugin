//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.14 at 08:42:26 AM MYT 
//


package com.veracode.cliang.sastPlugin.objects.raw.detailedReport;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScreenshotFormatType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ScreenshotFormatType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GIF"/>
 *     &lt;enumeration value="JPG"/>
 *     &lt;enumeration value="PNG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ScreenshotFormatType")
@XmlEnum
public enum ScreenshotFormatType {

    GIF,
    JPG,
    PNG;

    public String value() {
        return name();
    }

    public static ScreenshotFormatType fromValue(String v) {
        return valueOf(v);
    }

}
