package ru.gb.antonov.j710.monolith.beans.errorhandlers;

import java.util.logging.Logger;

/** {@code HttpStatus.BAD_REQUEST = 400}<br>
    Умолчальный текст сообщения: " Не удалось создать пользователя. " */
public class UserCreationException extends IllegalArgumentException {

    final static String messageDefault = " Не удалось создать пользователя. ";
    final static Logger LOGGER         = Logger.getLogger("ru.gb.antonov.j710.monolith.beans.errorhandlers.UserCreationException");

/** {@code HttpStatus.BAD_REQUEST = 400}<br>
    Умолчальный текст сообщения: " Не удалось создать пользователя. " */
    public UserCreationException (String messageText) {

        super (messageText = messageText == null ? messageDefault : messageText);
        LOGGER.severe ("ОШИБКА: "+ messageText);
    }
}
