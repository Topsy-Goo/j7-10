package ru.gb.antonov.j710.monolith.beans.errorhandlers;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException (String messageText) { super (messageText); }
}
