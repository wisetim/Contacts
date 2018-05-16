package me.wisetim.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.wisetim.R;
import me.wisetim.contacts.bean.Contact;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class ContactFragment extends Fragment {
    private static final String ARG_CONTACT_ID = "contact_id";
    private static final String TAG = "ContactFragment";
    public static final String EXTRA_CONTACT = "cn.edu.zjut.contact";

    @BindView(R.id.contact_name)
    EditText mContactName;
    @BindView(R.id.contact_phone)
    EditText mContactPhone;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Unbinder mUnbinder;
    private Contact mContact;
    private boolean mIsNameChanged;
    private boolean mIsPhoneChanged;

    public static ContactFragment newInstance(UUID contactId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT_ID, contactId);

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        UUID contactId = null;
        if (args != null) {
            contactId = (UUID) args.getSerializable(ARG_CONTACT_ID);
        }
        if (contactId != null) {
            mContact = ContactLab.get(getActivity()).getContact(contactId);
        } else {
            mContact = new Contact();
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (mContact != null) {
            mContactName.setText(mContact.getName());
            mContactPhone.setText(mContact.getPhoneNumber());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_contact, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                if (getActivity() != null) {
                    Contact newContact = new Contact(mContact.getId());
                    newContact.setName(mContactName.getText().toString());
                    newContact.setPhoneNumber(mContactPhone.getText().toString());
                    Intent data = new Intent();
                    data.putExtra(EXTRA_CONTACT, newContact);
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                }
//            case R.id.home:
//                NavUtils.navigateUpFromSameTask(getActivity());
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnTextChanged(R.id.contact_name)
    public void onContactNameChanged(CharSequence s) {
        mIsNameChanged = !s.toString().equals(mContact.getName());
    }

    @OnTextChanged(R.id.contact_phone)
    public void onContactPhoneChanged(CharSequence s) {
        mIsPhoneChanged = !s.toString().equals(mContact.getPhoneNumber());
    }
}
