//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 12:50:20 AM WIB 
//


package com.veracode.cliang.sastPlugin.objects.raw.buildInfo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlatformType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PlatformType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Not Specified"/>
 *     &lt;enumeration value="Windows"/>
 *     &lt;enumeration value="Solaris"/>
 *     &lt;enumeration value="Linux"/>
 *     &lt;enumeration value="Java"/>
 *     &lt;enumeration value="Windows Mobile"/>
 *     &lt;enumeration value="J2ME"/>
 *     &lt;enumeration value="ColdFusion"/>
 *     &lt;enumeration value="PHP"/>
 *     &lt;enumeration value="Android"/>
 *     &lt;enumeration value="iOS"/>
 *     &lt;enumeration value="Ruby"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PlatformType")
@XmlEnum
public enum PlatformType {

    @XmlEnumValue("Not Specified")
    NOT_SPECIFIED("Not Specified"),
    @XmlEnumValue("Windows")
    WINDOWS("Windows"),
    @XmlEnumValue("Solaris")
    SOLARIS("Solaris"),
    @XmlEnumValue("Linux")
    LINUX("Linux"),
    @XmlEnumValue("Java")
    JAVA("Java"),
    @XmlEnumValue("Windows Mobile")
    WINDOWS_MOBILE("Windows Mobile"),
    @XmlEnumValue("J2ME")
    J_2_ME("J2ME"),
    @XmlEnumValue("ColdFusion")
    COLD_FUSION("ColdFusion"),
    PHP("PHP"),
    @XmlEnumValue("Android")
    ANDROID("Android"),
    @XmlEnumValue("iOS")
    I_OS("iOS"),
    @XmlEnumValue("Ruby")
    RUBY("Ruby");
    private final String value;

    PlatformType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlatformType fromValue(String v) {
        for (PlatformType c: PlatformType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
