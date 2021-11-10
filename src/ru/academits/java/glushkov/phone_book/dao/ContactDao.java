package ru.academits.java.glushkov.phone_book.dao;

import ru.academits.java.glushkov.phone_book.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Anna on 17.06.2017.
 */
public class ContactDao {
    private List<Contact> contactList = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("Иван");
        contact.setLastName("Иванов");
        contact.setPhone("9123456789");
        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public List<Contact> getFilteredContacts(String searchText) {
        String text = searchText.toUpperCase();

        return searchText.equals("")
                ? contactList
                : contactList.stream().filter(contact ->
                contact.getLastName().toUpperCase().contains(text) ||
                        contact.getFirstName().toUpperCase().contains(text) ||
                        contact.getPhone().toUpperCase().contains(text))
                .collect(Collectors.toList());
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public void deleteContacts(Set<Integer> contactsIdsToRemove) {
        contactList = contactList.stream().filter(contact -> !contactsIdsToRemove.contains(contact.getId()))
                .collect(Collectors.toList());
    }
}