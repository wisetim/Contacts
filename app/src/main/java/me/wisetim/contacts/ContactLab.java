package me.wisetim.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import me.wisetim.contacts.bean.Contact;
import me.wisetim.contacts.database.ContactBaseHelper;
import me.wisetim.contacts.database.ContactCursorWrapper;
import me.wisetim.contacts.database.ContactDbSchema.ContactTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ContactLab {
    private static ContactLab sContactLab;

    private SQLiteDatabase mDatabase;
    private boolean mIsModified = true;
    private List<Contact> mContacts = new ArrayList<>();


    public static ContactLab get(Context context) {
        if (sContactLab == null) {
            sContactLab = new ContactLab(context);
        }
        return sContactLab;
    }

    private ContactLab(Context context) {
        Context applicationContext = context.getApplicationContext();
        mDatabase = new ContactBaseHelper(applicationContext).getWritableDatabase();
    }

    public void addContact(Contact c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(ContactTable.NAME, null, values);
        mIsModified = true;
    }

    public void deleteContact(Contact Contact) {
        mDatabase.delete(
                ContactTable.NAME, ContactTable.Cols.CONTACT_ID + " = ?",
                new String[]{Contact.getId().toString()}
        );
        mIsModified = true;
    }

    public List<Contact> getContacts() {
        //若表未修改过，则直接返回上一次查询的结果集
        //默认值为true，则首次请求必查询一次数据酷
        if (!mIsModified) return mContacts;
        mContacts = new ArrayList<>();
        try (ContactCursorWrapper cursor
                     = queryContacts(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mContacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        }
        Collections.sort(mContacts);
        mIsModified = false;

        return mContacts;
    }

    public Contact getContact(UUID id) {

        try (ContactCursorWrapper cursor = queryContacts(
                ContactTable.Cols.CONTACT_ID + " = ?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) return null;

            cursor.moveToFirst();
            return cursor.getContact();
        }
    }

    public void updateContact(Contact contact) {
        String contactId = contact.getId().toString();
        ContentValues values = getContentValues(contact);

        mDatabase.update(ContactTable.NAME, values,
                ContactTable.Cols.CONTACT_ID + " = ?",
                new String[]{contactId});
        mIsModified = true;
    }

    private static ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactTable.Cols.CONTACT_ID, contact.getId().toString());
        values.put(ContactTable.Cols.CONTACT_NAME, contact.getName());
        values.put(ContactTable.Cols.CONTACT_PHONE, contact.getPhoneNumber());

        return values;
    }

    private ContactCursorWrapper queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ContactTable.NAME,
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
