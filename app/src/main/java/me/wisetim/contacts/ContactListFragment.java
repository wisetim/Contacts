package me.wisetim.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import me.wisetim.R;
import me.wisetim.contacts.bean.Contact;
import me.wisetim.contacts.widget.SideBar;

public class ContactListFragment extends Fragment
        implements SideBar.OnTouchingLetterChangedListener {
    static final int REQUEST_NEW_CONTACT = 0;
    static final int REQUEST_MODIFY_CONTACT = 1;

    @BindView(R.id.contact_list_view)
    ListView mListView;
    @BindView(R.id.contact_list_sidebar)
    SideBar mSideBar;
    @BindView(R.id.contact_first_char)
    TextView mDialog;
    @BindView(R.id.contact_search_input)
    EditText mSearchInput;
    @BindView(R.id.contact_list_fab)
    FloatingActionButton mFloatingActionButton;
    TextView mFooterView;

    private Unbinder mUnbinder;
    private ContactAdapter mAdapter;
    private boolean mIsMultiChoiceMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list,
                container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);

        mFooterView = (TextView) View.inflate(getActivity(),
                R.layout.footer_contact_list_count, null);
        mListView.addFooterView(mFooterView);
        mListView.setMultiChoiceModeListener(new MultiChoiceModeCallback());

        List<Contact> contacts = ContactLab.get(getActivity()).getContacts();
        updateList(contacts, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.normal_mode_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        //暂时没有用到dialog，所以用不着更新
//        List<Contact> contacts = ContactLab.get(getActivity()).getContacts();
//        updateList(contacts, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_NEW_CONTACT) {
            Contact contact = data.getParcelableExtra(ContactFragment.EXTRA_CONTACT);
            ContactLab lab = ContactLab.get(getActivity());
            lab.addContact(contact);
            updateList(lab.getContacts(), false);
        } else if (requestCode == REQUEST_MODIFY_CONTACT) {
            Contact contact = data.getParcelableExtra(ContactFragment.EXTRA_CONTACT);
            ContactLab lab = ContactLab.get(getActivity());
            lab.updateContact(contact);
            updateList(lab.getContacts(), false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void updateList(List<Contact> contacts, boolean isCopy) {
        if (!isCopy) {
            //使用列表的拷贝，防止操作影响原始数据
            contacts = new ArrayList<>(contacts);
        }
        if (mAdapter == null) {
            assert getActivity() != null;
            if (!mIsMultiChoiceMode) {
                mAdapter = new ContactAdapter(getActivity(),
                        R.layout.item_contact_list_normal,
                        contacts);
            } else {
                mAdapter = new ContactAdapter(getActivity(),
                        R.layout.item_contact_list_multi_choice,
                        contacts);
            }
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(contacts);
        }
        mFooterView.setText(getString(R.string.contact_num, contacts.size()));
    }


    private void deleteContacts() {
        SparseBooleanArray array = mListView.getCheckedItemPositions();
        ContactLab contactLab = ContactLab.get(getActivity());
        List<Contact> contacts = contactLab.getContacts();
        for (int i = 0; i < array.size(); ++i) {
            contactLab.deleteContact(contacts.get(array.keyAt(i)));
        }
        updateList(contacts, false);
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    private class MultiChoiceModeCallback implements ListView.MultiChoiceModeListener {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.multi_choice_mode_menu, menu);
            List<Contact> contacts = ContactLab.get(getActivity()).getContacts();
            mAdapter = new ContactAdapter(Objects.requireNonNull(getActivity()),
                    R.layout.item_contact_list_multi_choice,
                    contacts);
            mListView.setAdapter(mAdapter);
            mode.setTitle(getString(R.string.selected_num,
                    mListView.getCheckedItemCount()));
            mIsMultiChoiceMode = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mIsMultiChoiceMode = true;
            return true;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_item_delete) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("确认删除联系人？")
                        .setPositiveButton("删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteContacts();
                                        mode.finish();
                                    }
                                })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mListView.clearChoices();
            List<Contact> contacts = ContactLab.get(getActivity()).getContacts();
            mAdapter = new ContactAdapter(Objects.requireNonNull(getActivity()),
                    R.layout.item_contact_list_normal,
                    contacts);
            mListView.setAdapter(mAdapter);
            mIsMultiChoiceMode = false;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            mode.setTitle(getString(R.string.selected_num, mListView.getCheckedItemCount()));
        }
    }

    @OnItemClick(R.id.contact_list_view)
    public void onContactListItemClicked(View view) {
        ContactAdapter.ContactHolder holder = (ContactAdapter.ContactHolder) view.getTag();
        Intent intent = ContactActivity.newIntent(getContext(), holder.mContact.getId());
        Objects.requireNonNull(getActivity()).startActivityForResult(intent,
                REQUEST_MODIFY_CONTACT);
    }

    @OnTextChanged(R.id.contact_search_input)
    public void onSearchInputChanged(CharSequence s) {
        List<Contact> contacts = ContactLab.get(getActivity()).getContacts();
        List<Contact> contactsCopy = new ArrayList<>(contacts);
        String lowerS = s.toString().toLowerCase();
        for (Contact data : contacts) {
            if (!data.getName().contains(s) && !data.getPinyin().contains(lowerS)) {
                contactsCopy.remove(data);
            }
        }
        updateList(contactsCopy, true);
    }

    @OnClick(R.id.contact_list_fab)
    public void onFloatingActionButtonClicked() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        startActivityForResult(intent, REQUEST_NEW_CONTACT);
    }
}
