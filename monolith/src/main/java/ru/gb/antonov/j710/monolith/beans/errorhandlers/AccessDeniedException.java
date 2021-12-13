package ru.gb.antonov.j710.monolith.beans.errorhandlers;

import java.util.logging.Logger;

/** {@code HttpStatus.FORBIDDEN = 403}<br>
    Умолчальный текст сообщения: " Доступ запрещён. " */
public class AccessDeniedException extends RuntimeException {

    final static String messageDefault = " Доступ запрещён. ";
    final static Logger LOGGER         = Logger.getLogger("ru.gb.antonov.j710.monolith.beans.errorhandlers.AccessDeniedException");

/** {@code HttpStatus.FORBIDDEN = 403}<br>
    Умолчальный текст сообщения: " Доступ запрещён. " */
    public AccessDeniedException (String messageText) {

        super (messageText = messageText == null ? messageDefault : messageText);
        LOGGER.severe ("ОШИБКА: "+ messageText);
    }
}
