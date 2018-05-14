package me.wisetim.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.wisetim.contacts.bean.Contact;
import me.wisetim.contacts.database.ContactBaseHelper;
import me.wisetim.contacts.database.ContactCursorWrapper;
import me.wisetim.contacts.database.ContactDbSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ContactLab {
    private static ContactLab sContactLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static List<Contact> mContacts;

    static {
        mContacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setName("张三");
        mContacts.add(contact1);

        Contact contact2 = new Contact();
        contact2.setName("李四");
        mContacts.add(contact2);

        Contact contact3 = new Contact();
        contact3.setName("Li Wu");
        mContacts.add(contact3);

        Collections.sort(mContacts);
    }

    public static ContactLab get(Context context) {
        if (sContactLab == null) {
            sContactLab = new ContactLab(context);
        }
        return sContactLab;
    }

    private ContactLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ContactBaseHelper(mContext).getWritableDatabase();
    }

    public void addContact(Contact c) {
        mContacts.add(c);
        Collections.sort(mContacts);
//        ContentValues values = getContentValues(c);
//
//        mDatabase.insert(ContactTable.NAME, null, values);
    }

    public void deleteContact(Contact Contact) {
        mDatabase.delete(
                ContactDbSchema.ContactTable.NAME,
                ContactDbSchema.ContactTable.Cols.CONTACT_ID + " = ?",
                new String[]{Contact.getId().toString()}
        );
    }

    public List<Contact> getContacts() {

//        ContactCursorWrapper cursor = queryContacts(null, null);
//
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                Contacts.add(cursor.getContact());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }

        return mContacts;
    }

    public Contact getContact(UUID id) {
        for (Contact contact : mContacts) {
            if (contact.getId().equals(id)) return contact;
        }
        return null;

//        ContactCursorWrapper cursor = queryContacts(
//                ContactTable.Cols.CONTACT_ID + " = ?",
//                new String[]{id.toString()}
//        );
//
//        try {
//            if (cursor.getCount() == 0) return null;
//
//            cursor.moveToFirst();
//            return cursor.getContact();
//        } finally {
//            cursor.close();
//        }
    }

    public void updateContact(Contact contact) {
        String uuidString = contact.getId().toString();
        ContentValues values = getContentValues(contact);

        mDatabase.update(ContactDbSchema.ContactTable.NAME, values,
                ContactDbSchema.ContactTable.Cols.CONTACT_ID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactDbSchema.ContactTable.Cols.CONTACT_ID, contact.getId().toString());
        values.put(ContactDbSchema.ContactTable.Cols.CONTACT_NAME, contact.getName());
        values.put(ContactDbSchema.ContactTable.Cols.CONTACT_PHONE, contact.getPhoneNumber());

        return values;
    }

    private ContactCursorWrapper queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ContactDbSchema.ContactTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ContactCursorWrapper(cursor);
    }
}
