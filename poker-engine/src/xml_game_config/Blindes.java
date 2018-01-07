
package xml_game_config;

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
 *         &lt;element ref="{}Big"/>
 *         &lt;element ref="{}Small"/>
 *       &lt;/sequence>
 *       &lt;attribute name="max-total-rounds" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="fixed" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="additions" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "big",
    "small"
})
@XmlRootElement(name = "Blindes")
public class Blindes {

    @XmlElement(name = "Big")
    protected int big;
    @XmlElement(name = "Small")
    protected int small;
    @XmlAttribute(name = "max-total-rounds")
    protected int maxTotalRounds;
    @XmlAttribute(name = "fixed", required = true)
    protected boolean fixed;
    @XmlAttribute(name = "additions")
    protected int additions;

    /**
     * Gets the value of the big property.
     * 
     */
    public int getBig() {
        return big;
    }

    /**
     * Sets the value of the big property.
     * 
     */
    public void setBig(byte value) {
        this.big = value;
    }

    /**
     * Gets the value of the small property.
     * 
     */
    public int getSmall() {
        return small;
    }

    /**
     * Sets the value of the small property.
     * 
     */
    public void setSmall(byte value) {
        this.small = value;
    }

    /**
     * Gets the value of the maxTotalRounds property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public int getMaxTotalRounds() {
        return maxTotalRounds;
    }

    /**
     * Sets the value of the maxTotalRounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setMaxTotalRounds(Byte value) {
        this.maxTotalRounds = value;
    }

    /**
     * Gets the value of the fixed property.
     * 
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Sets the value of the fixed property.
     * 
     */
    public void setFixed(boolean value) {
        this.fixed = value;
    }

    /**
     * Gets the value of the additions property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public int getAdditions() {
        return additions;
    }

    /**
     * Sets the value of the additions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setAdditions(Byte value) {
        this.additions = value;
    }

}
