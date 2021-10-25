package ru.gb.antonov.j710.monolith.entities.dtos;

public class StringResponse
{
    private String value;
//------------------------------------------------------------
    public StringResponse() {}
    public StringResponse (String value) { this.value = value; }
//------------------------------------------------------------
    public String getValue ()           {    return value;    }
    public void setValue (String value) {    this.value = value;    }
}
