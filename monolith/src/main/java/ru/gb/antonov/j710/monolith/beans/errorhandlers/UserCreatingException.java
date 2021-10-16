package ru.gb.antonov.j710.monolith.beans.errorhandlers;

public class UserCreatingException extends IllegalArgumentException
{
    public UserCreatingException (String messageText) { super (messageText); }
}
