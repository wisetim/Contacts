package me.wisetim.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ContactActivity extends SingleFragmentActivity {
    private static final String EXTRA_CONTACT_ID
            = "me.wisetim.contact_id";

    public static Intent newIntent(Context packageContext, UUID contactId) {
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
        if (contactId == null) {
            return new ContactFragment();
        } else {
            return ContactFragment.newInstance(contactId);
        }
    }

}
