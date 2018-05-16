package me.wisetim.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.UUID;

import me.wisetim.R;

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
