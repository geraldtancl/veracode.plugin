//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.14 at 08:42:26 AM MYT 
//


package com.veracode.cliang.sastPlugin.objects.raw.detailedReport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for License complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="License">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="spdx_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="license_url" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="risk_rating" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "License")
public class License {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "spdx_id", required = true)
    protected String spdxId;
    @XmlAttribute(name = "license_url", required = true)
    protected String licenseUrl;
    @XmlAttribute(name = "risk_rating", required = true)
    protected String riskRating;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the spdxId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpdxId() {
        return spdxId;
    }

    /**
     * Sets the value of the spdxId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpdxId(String value) {
        this.spdxId = value;
    }

    /**
     * Gets the value of the licenseUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * Sets the value of the licenseUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseUrl(String value) {
        this.licenseUrl = value;
    }

    /**
     * Gets the value of the riskRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskRating() {
        return riskRating;
    }

    /**
     * Sets the value of the riskRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskRating(String value) {
        this.riskRating = value;
    }

}
