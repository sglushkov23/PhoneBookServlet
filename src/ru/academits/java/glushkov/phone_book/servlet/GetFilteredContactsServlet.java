package ru.academits.java.glushkov.phone_book.servlet;

import ru.academits.java.glushkov.phone_book.PhoneBook;
import ru.academits.java.glushkov.phone_book.converter.ContactConverter;
import ru.academits.java.glushkov.phone_book.model.Contact;
import ru.academits.java.glushkov.phone_book.service.ContactService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;

public class GetFilteredContactsServlet extends HttpServlet {
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String searchText = req.getParameter("searchText");
            List<Contact> contactList = phoneBookService.getFilteredContacts(searchText);
            String contactListJson = contactConverter.convertToJson(contactList);

            resp.getOutputStream().write(contactListJson.getBytes(Charset.forName("UTF-8")));
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
        } catch (Exception e) {
            System.out.println("error in GetFilteredContactsServlet GET: ");
            e.printStackTrace();
        }
    }
}