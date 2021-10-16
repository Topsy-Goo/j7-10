package ru.gb.antonov.j710.monolith.beans.errorhandlers;

/** {@code HttpStatus.BAD_REQUEST = 400} */
public class BadCreationParameterException extends IllegalArgumentException
{
/** {@code HttpStatus.BAD_REQUEST = 400} */
    public BadCreationParameterException (String messageText) { super (messageText); }
}
