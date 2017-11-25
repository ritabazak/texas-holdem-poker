
package xml_game_config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="total-players" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="game-title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DynamicPlayers")
public class DynamicPlayers {

    @XmlAttribute(name = "total-players", required = true)
    protected byte totalPlayers;
    @XmlAttribute(name = "game-title", required = true)
    protected String gameTitle;

    /**
     * Gets the value of the totalPlayers property.
     * 
     */
    public byte getTotalPlayers() {
        return totalPlayers;
    }

    /**
     * Sets the value of the totalPlayers property.
     * 
     */
    public void setTotalPlayers(byte value) {
        this.totalPlayers = value;
    }

    /**
     * Gets the value of the gameTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGameTitle() {
        return gameTitle;
    }

    /**
     * Sets the value of the gameTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGameTitle(String value) {
        this.gameTitle = value;
    }

}
