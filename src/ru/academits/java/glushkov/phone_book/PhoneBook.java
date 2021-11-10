package ru.academits.java.glushkov.phone_book;

import ru.academits.java.glushkov.phone_book.converter.ContactConverter;
import ru.academits.java.glushkov.phone_book.converter.ContactValidationConverter;
import ru.academits.java.glushkov.phone_book.dao.ContactDao;
import ru.academits.java.glushkov.phone_book.service.ContactService;
import ru.academits.java.glushkov.phone_book.converter.IdsListConverter;

/**
 * Created by Anna on 14.06.2017.
 */
public class PhoneBook {

    public static ContactDao contactDao = new ContactDao();

    public static ContactService phoneBookService = new ContactService();

    public static ContactConverter contactConverter = new ContactConverter();

    public static ContactValidationConverter contactValidationConverter = new ContactValidationConverter();

    public static IdsListConverter idsListConverter = new IdsListConverter();
}