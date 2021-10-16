package ru.gb.antonov.j710.monolith.beans.errorhandlers;

/** {@code HttpStatus.INTERNAL_SERVER_ERROR = 500}  */
public class UnableToPerformException extends IllegalArgumentException
{
/** {@code HttpStatus.INTERNAL_SERVER_ERROR = 500}  */
    public UnableToPerformException (String messageText) { super (messageText); }
}
