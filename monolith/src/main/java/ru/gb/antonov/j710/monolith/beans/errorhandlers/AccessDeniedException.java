package ru.gb.antonov.j710.monolith.beans.errorhandlers;

/** {@code HttpStatus.FORBIDDEN = 403} */
public class AccessDeniedException extends RuntimeException
{
/** {@code HttpStatus.FORBIDDEN = 403} */
    public AccessDeniedException (String messageText) { super (messageText); }
}
