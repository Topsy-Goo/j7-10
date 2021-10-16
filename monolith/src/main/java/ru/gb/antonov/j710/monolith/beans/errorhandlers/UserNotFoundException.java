package ru.gb.antonov.j710.monolith.beans.errorhandlers;

/** {@code HttpStatus.NOT_FOUND = 404} */
public class UserNotFoundException extends RuntimeException
{
/** {@code HttpStatus.NOT_FOUND = 404} */
    public UserNotFoundException (String messageText) { super (messageText); }
}
