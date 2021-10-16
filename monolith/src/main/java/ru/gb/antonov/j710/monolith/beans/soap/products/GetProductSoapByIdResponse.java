package ru.gb.antonov.j710.monolith.beans.soap.products;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"productSoap"})
@XmlRootElement(name="getProductSoapByIdResponse")
public class GetProductSoapByIdResponse {

    @XmlElement (required=true)    protected ProductSoap productSoap;
}
