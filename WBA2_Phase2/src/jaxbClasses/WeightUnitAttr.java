//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.25 at 06:17:42 PM CEST 
//


package jaxbClasses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for weightUnitAttr.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="weightUnitAttr">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="liter"/>
 *     &lt;enumeration value="litre"/>
 *     &lt;enumeration value="ml"/>
 *     &lt;enumeration value="g"/>
 *     &lt;enumeration value="kg"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "weightUnitAttr")
@XmlEnum
public enum WeightUnitAttr {

    @XmlEnumValue("liter")
    LITER("liter"),
    @XmlEnumValue("litre")
    LITRE("litre"),
    @XmlEnumValue("ml")
    ML("ml"),
    @XmlEnumValue("g")
    G("g"),
    @XmlEnumValue("kg")
    KG("kg");
    private final String value;

    WeightUnitAttr(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WeightUnitAttr fromValue(String v) {
        for (WeightUnitAttr c: WeightUnitAttr.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
