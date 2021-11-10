"use strict";

function get(url, data) {
    return $.get(url, data);
}

function post(url, data) {
    return $.post({
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data)
    });
}

function PhoneBookService() {
    this.baseUrl = "/phonebook/";

    this.loadFilteredData = function (searchText) {
        return get(this.baseUrl + "get/filtered", {searchText: searchText});
    }

    this.addContact = function (contact) {
        return post(this.baseUrl + "add", contact);
    }

    this.deleteRecords = function (ids) {
        return post(this.baseUrl + "delete/contacts", ids);
    }
}

function Contact(firstName, lastName, phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.checked = false;
    this.shown = true;
}

new Vue({
    el: "#app",
    data: {
        validation: false,
        serverValidation: false,
        firstName: "",
        lastName: "",
        phone: "",
        rows: [],
        serverError: "",
        searchText: "",
        contactIdToDelete: -1,
        dialogMessage: "Вы действительно хотите удалить выделенные записи?",
        service: new PhoneBookService(),
        disabled: "disabled",
        isSelectedAllContacts: false
    },

    watch: {
        contactIdToDelete: function () {
            this.dialogMessage = this.contactIdToDelete === -1
                ? "Вы действительно хотите удалить выделенные записи?"
                : "Вы действительно хотите удалить выбранную запись?";
        }
    },

    methods: {
        contactToString: function (contact) {
            var note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phone;
            note += ")";
            return note;
        },
        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phone: contact.phone,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },
        addContact: function () {
            if (this.hasError) {
                this.validation = true;
                this.serverValidation = false;
                return;
            }

            var self = this;
            var contact = new Contact(this.firstName, this.lastName, this.phone);

            this.service.addContact(contact).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadFilteredData();
            });

            self.firstName = "";
            self.lastName = "";
            self.phone = "";
            self.validation = false;
        },
        loadFilteredData: function () {
            var self = this;

            this.service.loadFilteredData(this.searchText).done(function (response) {
                var contactListFromServer = JSON.parse(response);
                self.rows = self.convertContactList(contactListFromServer);
            });

            this.disabled = "disabled";
        },
        resetFilter: function () {
            this.searchText = "";
            this.loadFilteredData();
        },
        deleteContacts: function () {
            var idsToDelete = this.contactIdToDelete === -1
                ? this.getSelectedContactsIds()
                : [this.contactIdToDelete];
            var self = this;

            this.service.deleteRecords(idsToDelete).done(function () {
                self.serverValidation = false;
            }).always(function () {
                self.loadFilteredData();
            });

            this.contactIdToDelete = -1;
        },

        setContactIdToDelete: function (contactId) {
            this.contactIdToDelete = contactId;
        },

        resetContactIdToDelete: function () {
            this.contactIdToDelete = -1;
        },

        getSelectedContactsIds: function () {
            return this.rows.filter(function (e) {
                return e.checked;
            }).map(function (e) {
                return e.id;
            });
        },

        checkForSelectedContacts: function () {
            if (this.getSelectedContactsIds().length > 0) {
                this.disabled = "";
                return;
            }

            this.disabled = "disabled";
        },

        selectAllContacts: function () {
            var isSelected = this.isSelectedAllContacts;

            this.rows.forEach(function (contact) {
                contact.checked = isSelected;
            });
        }
    },
    computed: {
        firstNameError: function () {
            if (this.firstName) {
                return {
                    message: "",
                    error: false
                };
            }

            return {
                message: "Поле Имя должно быть заполнено.",
                error: true
            };
        },
        lastNameError: function () {
            if (!this.lastName) {
                return {
                    message: "Поле Фамилия должно быть заполнено.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        phoneError: function () {
            if (!this.phone) {
                return {
                    message: "Поле Телефон должно быть заполнено.",
                    error: true
                };
            }

            var self = this;

            var sameContact = this.rows.some(function (c) {
                return c.phone === self.phone;
            });

            if (sameContact) {
                return {
                    message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        hasError: function () {
            return this.lastNameError.error || this.firstNameError.error || this.phoneError.error;
        }
    },
    created: function () {
        this.loadFilteredData();
    }
});