//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 12:50:20 AM WIB 
//


package com.veracode.cliang.sastPlugin.objects.raw.buildInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="build" type="{https://analysiscenter.veracode.com/schema/4.0/buildinfo}BuildType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="buildinfo_version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="account_id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="app_id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="sandbox_id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="build_id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="dynamic_scan_type" type="{https://analysiscenter.veracode.com/schema/4.0/buildinfo}DynamicScanType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "build"
})
@XmlRootElement(name = "buildinfo")
public class Buildinfo {

    @XmlElement(required = true)
    protected BuildType build;
    @XmlAttribute(name = "buildinfo_version", required = true)
    protected String buildinfoVersion;
    @XmlAttribute(name = "account_id", required = true)
    protected long accountId;
    @XmlAttribute(name = "app_id", required = true)
    protected long appId;
    @XmlAttribute(name = "sandbox_id")
    protected Long sandboxId;
    @XmlAttribute(name = "build_id", required = true)
    protected long buildId;
    @XmlAttribute(name = "dynamic_scan_type")
    protected DynamicScanType dynamicScanType;

    /**
     * Gets the value of the build property.
     * 
     * @return
     *     possible object is
     *     {@link BuildType }
     *     
     */
    public BuildType getBuild() {
        return build;
    }

    /**
     * Sets the value of the build property.
     * 
     * @param value
     *     allowed object is
     *     {@link BuildType }
     *     
     */
    public void setBuild(BuildType value) {
        this.build = value;
    }

    /**
     * Gets the value of the buildinfoVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildinfoVersion() {
        return buildinfoVersion;
    }

    /**
     * Sets the value of the buildinfoVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildinfoVersion(String value) {
        this.buildinfoVersion = value;
    }

    /**
     * Gets the value of the accountId property.
     * 
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     */
    public void setAccountId(long value) {
        this.accountId = value;
    }

    /**
     * Gets the value of the appId property.
     * 
     */
    public long getAppId() {
        return appId;
    }

    /**
     * Sets the value of the appId property.
     * 
     */
    public void setAppId(long value) {
        this.appId = value;
    }

    /**
     * Gets the value of the sandboxId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSandboxId() {
        return sandboxId;
    }

    /**
     * Sets the value of the sandboxId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSandboxId(Long value) {
        this.sandboxId = value;
    }

    /**
     * Gets the value of the buildId property.
     * 
     */
    public long getBuildId() {
        return buildId;
    }

    /**
     * Sets the value of the buildId property.
     * 
     */
    public void setBuildId(long value) {
        this.buildId = value;
    }

    /**
     * Gets the value of the dynamicScanType property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicScanType }
     *     
     */
    public DynamicScanType getDynamicScanType() {
        return dynamicScanType;
    }

    /**
     * Sets the value of the dynamicScanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicScanType }
     *     
     */
    public void setDynamicScanType(DynamicScanType value) {
        this.dynamicScanType = value;
    }

}
