package ru.gb.antonov.j710.monolith.beans.soap.products;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name="", propOrder={"fromId", "toId"})
@XmlRootElement (name="getProductSoapRangeByIdRequest")
public class GetProductSoapRangeByIdRequest
{
    @XmlElement (required=true)   protected long fromId;
    @XmlElement (required=true)   protected long toId;
}
