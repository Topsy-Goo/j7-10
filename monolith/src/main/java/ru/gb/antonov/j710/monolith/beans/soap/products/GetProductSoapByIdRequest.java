package ru.gb.antonov.j710.monolith.beans.soap.products;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name="", propOrder={"id"})
@XmlRootElement (name="getProductSoapByIdRequest")
public class GetProductSoapByIdRequest
{
    @XmlElement (required=true)    protected long id;
}
