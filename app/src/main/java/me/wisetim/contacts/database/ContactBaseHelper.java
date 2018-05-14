package me.wisetim.contacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tim on 2018/3/3.
 */

public class ContactBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "contactBase.db";

    public ContactBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContactDbSchema.ContactTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ContactDbSchema.ContactTable.Cols.CONTACT_ID + ", " +
                ContactDbSchema.ContactTable.Cols.CONTACT_NAME + ", " +
                ContactDbSchema.ContactTable.Cols.CONTACT_PHONE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
