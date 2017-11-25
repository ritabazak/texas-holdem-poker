
package xml_game_config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}HandsCount"/>
 *         &lt;element ref="{}Buy"/>
 *         &lt;element ref="{}Blindes"/>
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
    "handsCount",
    "buy",
    "blindes"
})
@XmlRootElement(name = "Structure")
public class Structure {

    @XmlElement(name = "HandsCount")
    protected byte handsCount;
    @XmlElement(name = "Buy")
    protected byte buy;
    @XmlElement(name = "Blindes", required = true)
    protected Blindes blindes;

    /**
     * Gets the value of the handsCount property.
     * 
     */
    public byte getHandsCount() {
        return handsCount;
    }

    /**
     * Sets the value of the handsCount property.
     * 
     */
    public void setHandsCount(byte value) {
        this.handsCount = value;
    }

    /**
     * Gets the value of the buy property.
     * 
     */
    public byte getBuy() {
        return buy;
    }

    /**
     * Sets the value of the buy property.
     * 
     */
    public void setBuy(byte value) {
        this.buy = value;
    }

    /**
     * Gets the value of the blindes property.
     * 
     * @return
     *     possible object is
     *     {@link Blindes }
     *     
     */
    public Blindes getBlindes() {
        return blindes;
    }

    /**
     * Sets the value of the blindes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Blindes }
     *     
     */
    public void setBlindes(Blindes value) {
        this.blindes = value;
    }

}
