package ru.gb.antonov.j710.monolith.beans.errorhandlers;

import java.util.logging.Logger;

public class ResourceNotFoundException extends RuntimeException {

    final static String messageDefault = " Ресурс не найден. ";
    final static Logger LOGGER         = Logger.getLogger("ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException");

    public ResourceNotFoundException (String messageText) {

        super (messageText = messageText == null ? messageDefault : messageText);
        LOGGER.severe ("ОШИБКА: "+ messageText);
    }
}
