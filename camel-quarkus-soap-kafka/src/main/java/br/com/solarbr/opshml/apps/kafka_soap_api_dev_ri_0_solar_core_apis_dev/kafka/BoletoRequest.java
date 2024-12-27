
package br.com.solarbr.opshml.apps.kafka_soap_api_dev_ri_0_solar_core_apis_dev.kafka;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="topic" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="boleto"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="NRONOTAFISCAL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="DATAEMISSAO" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="VALORTOTAL" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="VENCIMENTOREAL" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="NUMERODOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="VALORBOLETO" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODIGOBARRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="PIXCOPIAECOLA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "topic",
    "key",
    "boleto"
})
@XmlRootElement(name = "BoletoRequest")
public class BoletoRequest {

    @XmlElement(required = true)
    protected String topic;
    @XmlElement(required = true)
    protected String key;
    @XmlElement(required = true)
    protected BoletoRequest.Boleto boleto;

    /**
     * Obtém o valor da propriedade topic.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Define o valor da propriedade topic.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Obtém o valor da propriedade key.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Define o valor da propriedade key.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Obtém o valor da propriedade boleto.
     * 
     * @return
     *     possible object is
     *     {@link BoletoRequest.Boleto }
     *     
     */
    public BoletoRequest.Boleto getBoleto() {
        return boleto;
    }

    /**
     * Define o valor da propriedade boleto.
     * 
     * @param value
     *     allowed object is
     *     {@link BoletoRequest.Boleto }
     *     
     */
    public void setBoleto(BoletoRequest.Boleto value) {
        this.boleto = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="NRONOTAFISCAL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="DATAEMISSAO" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="VALORTOTAL" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="VENCIMENTOREAL" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="NUMERODOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="VALORBOLETO" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODIGOBARRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="PIXCOPIAECOLA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nronotafiscal",
        "status",
        "dataemissao",
        "valortotal",
        "vencimento",
        "vencimentoreal",
        "numerodocumento",
        "valorboleto",
        "codigobarra",
        "pixcopiaecola"
    })
    public static class Boleto {

        @XmlElement(name = "NRONOTAFISCAL", required = true)
        protected String nronotafiscal;
        @XmlElement(name = "STATUS", required = true)
        protected String status;
        @XmlElement(name = "DATAEMISSAO", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataemissao;
        @XmlElement(name = "VALORTOTAL", required = true)
        protected BigDecimal valortotal;
        @XmlElement(name = "VENCIMENTO", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar vencimento;
        @XmlElement(name = "VENCIMENTOREAL", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar vencimentoreal;
        @XmlElement(name = "NUMERODOCUMENTO", required = true)
        protected String numerodocumento;
        @XmlElement(name = "VALORBOLETO", required = true)
        protected BigDecimal valorboleto;
        @XmlElement(name = "CODIGOBARRA", required = true)
        protected String codigobarra;
        @XmlElement(name = "PIXCOPIAECOLA", required = true)
        protected String pixcopiaecola;

        /**
         * Obtém o valor da propriedade nronotafiscal.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNRONOTAFISCAL() {
            return nronotafiscal;
        }

        /**
         * Define o valor da propriedade nronotafiscal.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNRONOTAFISCAL(String value) {
            this.nronotafiscal = value;
        }

        /**
         * Obtém o valor da propriedade status.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTATUS() {
            return status;
        }

        /**
         * Define o valor da propriedade status.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTATUS(String value) {
            this.status = value;
        }

        /**
         * Obtém o valor da propriedade dataemissao.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAEMISSAO() {
            return dataemissao;
        }

        /**
         * Define o valor da propriedade dataemissao.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAEMISSAO(XMLGregorianCalendar value) {
            this.dataemissao = value;
        }

        /**
         * Obtém o valor da propriedade valortotal.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVALORTOTAL() {
            return valortotal;
        }

        /**
         * Define o valor da propriedade valortotal.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVALORTOTAL(BigDecimal value) {
            this.valortotal = value;
        }

        /**
         * Obtém o valor da propriedade vencimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getVENCIMENTO() {
            return vencimento;
        }

        /**
         * Define o valor da propriedade vencimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setVENCIMENTO(XMLGregorianCalendar value) {
            this.vencimento = value;
        }

        /**
         * Obtém o valor da propriedade vencimentoreal.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getVENCIMENTOREAL() {
            return vencimentoreal;
        }

        /**
         * Define o valor da propriedade vencimentoreal.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setVENCIMENTOREAL(XMLGregorianCalendar value) {
            this.vencimentoreal = value;
        }

        /**
         * Obtém o valor da propriedade numerodocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMERODOCUMENTO() {
            return numerodocumento;
        }

        /**
         * Define o valor da propriedade numerodocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMERODOCUMENTO(String value) {
            this.numerodocumento = value;
        }

        /**
         * Obtém o valor da propriedade valorboleto.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVALORBOLETO() {
            return valorboleto;
        }

        /**
         * Define o valor da propriedade valorboleto.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVALORBOLETO(BigDecimal value) {
            this.valorboleto = value;
        }

        /**
         * Obtém o valor da propriedade codigobarra.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODIGOBARRA() {
            return codigobarra;
        }

        /**
         * Define o valor da propriedade codigobarra.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODIGOBARRA(String value) {
            this.codigobarra = value;
        }

        /**
         * Obtém o valor da propriedade pixcopiaecola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPIXCOPIAECOLA() {
            return pixcopiaecola;
        }

        /**
         * Define o valor da propriedade pixcopiaecola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPIXCOPIAECOLA(String value) {
            this.pixcopiaecola = value;
        }

    }

}
