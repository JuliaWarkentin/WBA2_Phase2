//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.14 at 01:25:07 AM CEST 
//


package JAXBClasses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="profiles">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="profile" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="birthdate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                             &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "profiles"
})
@XmlRootElement(name = "frigdeManagerStorage")
public class FrigdeManagerStorage {

    @XmlElement(required = true)
    protected FrigdeManagerStorage.Profiles profiles;

    /**
     * Gets the value of the profiles property.
     * 
     * @return
     *     possible object is
     *     {@link FrigdeManagerStorage.Profiles }
     *     
     */
    public FrigdeManagerStorage.Profiles getProfiles() {
        return profiles;
    }

    /**
     * Sets the value of the profiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrigdeManagerStorage.Profiles }
     *     
     */
    public void setProfiles(FrigdeManagerStorage.Profiles value) {
        this.profiles = value;
    }


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
     *         &lt;element name="profile" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="birthdate" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                   &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "profile"
    })
    public static class Profiles {

        @XmlElement(required = true)
        protected List<FrigdeManagerStorage.Profiles.Profile> profile;

        /**
         * Gets the value of the profile property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the profile property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProfile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FrigdeManagerStorage.Profiles.Profile }
         * 
         * 
         */
        public List<FrigdeManagerStorage.Profiles.Profile> getProfile() {
            if (profile == null) {
                profile = new ArrayList<FrigdeManagerStorage.Profiles.Profile>();
            }
            return this.profile;
        }


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
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="birthdate" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "birthdate",
            "gender",
            "height",
            "weight"
        })
        public static class Profile {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar birthdate;
            @XmlElement(required = true)
            protected String gender;
            protected float height;
            protected float weight;

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
             * Gets the value of the birthdate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getBirthdate() {
                return birthdate;
            }

            /**
             * Sets the value of the birthdate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setBirthdate(XMLGregorianCalendar value) {
                this.birthdate = value;
            }

            /**
             * Gets the value of the gender property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getGender() {
                return gender;
            }

            /**
             * Sets the value of the gender property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setGender(String value) {
                this.gender = value;
            }

            /**
             * Gets the value of the height property.
             * 
             */
            public float getHeight() {
                return height;
            }

            /**
             * Sets the value of the height property.
             * 
             */
            public void setHeight(float value) {
                this.height = value;
            }

            /**
             * Gets the value of the weight property.
             * 
             */
            public float getWeight() {
                return weight;
            }

            /**
             * Sets the value of the weight property.
             * 
             */
            public void setWeight(float value) {
                this.weight = value;
            }

        }

    }

}