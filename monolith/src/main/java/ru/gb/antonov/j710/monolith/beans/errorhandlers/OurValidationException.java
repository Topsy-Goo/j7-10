package ru.gb.antonov.j710.monolith.beans.errorhandlers;

import java.util.Collections;
import java.util.List;

//Сласс создан для использования возможностей hibernate.validator'а.
//Об использовании см. MonolithExceptionHandler, ErrorMessage, ProductController.createProduct() и Product.
public class OurValidationException extends RuntimeException
{
    private final List<String> messages;

    public OurValidationException (List<String> strings) { messages = strings; }

    public List<String> getMessages() {   return Collections.unmodifiableList (messages);   }
}
