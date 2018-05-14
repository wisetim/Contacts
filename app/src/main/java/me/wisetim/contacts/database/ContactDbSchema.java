package me.wisetim.contacts.database;

/**
 * Created by Tim on 2018/3/3.
 */

public class ContactDbSchema {
    public static final class ContactTable {
        public static final String NAME = "contacts";

        public static final class Cols {
            public static final String CONTACT_ID = "contact_id";
            public static final String CONTACT_NAME = "contact_name";
            public static final String CONTACT_PHONE = "contact_phone";
        }
    }
}
