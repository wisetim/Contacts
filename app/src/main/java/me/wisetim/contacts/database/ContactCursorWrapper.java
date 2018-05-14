package me.wisetim.contacts.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import me.wisetim.contacts.bean.Contact;
import me.wisetim.contacts.database.ContactDbSchema.ContactTable;

import java.util.UUID;

/**
 * Created by Tim on 2018/3/4.
 */

public class ContactCursorWrapper extends CursorWrapper {
    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        String uuidString = getString(getColumnIndex(ContactTable.Cols.CONTACT_ID));
        String contactName = getString(getColumnIndex(ContactTable.Cols.CONTACT_NAME));
        String phoneNumber = getString(getColumnIndex(ContactTable.Cols.CONTACT_PHONE));

        Contact contact = new Contact(UUID.fromString(uuidString));
        contact.setName(contactName);
        contact.setPhoneNumber(phoneNumber);

        return contact;
    }
}
