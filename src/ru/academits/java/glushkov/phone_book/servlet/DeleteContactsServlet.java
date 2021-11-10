package ru.academits.java.glushkov.phone_book.servlet;

import ru.academits.java.glushkov.phone_book.service.ContactService;
import ru.academits.java.glushkov.phone_book.PhoneBook;
import ru.academits.java.glushkov.phone_book.converter.IdsListConverter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DeleteContactsServlet extends HttpServlet {
    private final IdsListConverter idsListConverter = PhoneBook.idsListConverter;
    private final ContactService phoneBookService = PhoneBook.phoneBookService;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String contactsIdsToDeleteJson = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            HashSet<Integer> contactsIdsToDelete = idsListConverter.convertFromJson(contactsIdsToDeleteJson);
            phoneBookService.deleteContacts(contactsIdsToDelete);
        } catch (Exception e) {
            System.out.println("error in DeleteContactsServlet POST: ");
            e.printStackTrace();
        }
    }
}