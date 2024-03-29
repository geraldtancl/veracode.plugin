//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 07:48:55 AM WIB 
//


package com.veracode.cliang.sastPlugin.objects.raw.fileList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 The file type element contains the key elements and attributes
 *                 that reflect the data we store for a file.
 *             
 * 
 * <p>Java class for FileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="file_id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="file_name" type="{https://analysiscenter.veracode.com/schema/2.0/filelist}LongRequiredTextType" />
 *       &lt;attribute name="file_status" type="{https://analysiscenter.veracode.com/schema/2.0/filelist}StatusType" />
 *       &lt;attribute name="file_md5" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileType")
public class FileType {

    @XmlAttribute(name = "file_id")
    protected Long fileId;
    @XmlAttribute(name = "file_name")
    protected String fileName;
    @XmlAttribute(name = "file_status")
    protected StatusType fileStatus;
    @XmlAttribute(name = "file_md5")
    protected String fileMd5;

    /**
     * Gets the value of the fileId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * Sets the value of the fileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFileId(Long value) {
        this.fileId = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fileStatus property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getFileStatus() {
        return fileStatus;
    }

    /**
     * Sets the value of the fileStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setFileStatus(StatusType value) {
        this.fileStatus = value;
    }

    /**
     * Gets the value of the fileMd5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileMd5() {
        return fileMd5;
    }

    /**
     * Sets the value of the fileMd5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileMd5(String value) {
        this.fileMd5 = value;
    }

}
