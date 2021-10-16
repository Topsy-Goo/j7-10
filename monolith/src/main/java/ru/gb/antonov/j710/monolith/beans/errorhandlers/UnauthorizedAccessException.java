package ru.gb.antonov.j710.monolith.beans.errorhandlers;

/** {@code HttpStatus.UNAUTHORIZED = 401} */
public class UnauthorizedAccessException extends RuntimeException
{
    public UnauthorizedAccessException (String text) { super (text); }
}
