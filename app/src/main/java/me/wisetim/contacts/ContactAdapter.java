package me.wisetim.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import me.wisetim.R;
import me.wisetim.contacts.bean.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wisetim.contacts.widget.ContactListItemView;


public class ContactAdapter extends ArrayAdapter<Contact>
        implements AbsListView.OnScrollListener, SectionIndexer {

    private int mLayoutId;
    private AbsListView.OnScrollListener listener;

    ContactAdapter(@NonNull Context context,
                   int resource,
                   @NonNull List<Contact> objects) {
        super(context, resource, objects);
        mLayoutId = resource;
    }


    public void addOnScrollListener(AbsListView.OnScrollListener l) {
        this.listener = l;
    }

    public void refresh(List<Contact> contacts) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        clear();
        addAll(contacts);
        this.notifyDataSetChanged();
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = getItem(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        Contact item = getItem(position);
        assert item != null;
        return item.getFirstChar();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
            this.notifyDataSetChanged();
        }

        if (this.listener != null) {
            this.listener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.listener != null) {
            this.listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ContactHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mLayoutId, null);
            holder = new ContactHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ContactHolder) view.getTag();
        }
        holder.bindContact(position);
        return view;
    }

    class ContactHolder {
        Contact mContact;

        @BindView(R.id.contact_list_item_view)
        ContactListItemView mItem;

        ContactHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bindContact(int position) {
            mContact = getItem(position);
            if (mContact == null) return;
            mItem.setContactNameText(mContact.getName());

            if (position == 0) {
                mItem.setCatalogAndLineVisibility(View.VISIBLE);
                mItem.setCatalogLetter(String.valueOf(mContact.getFirstChar()));
            } else {
                Contact prevData = getItem(position - 1);
                if (mContact.getFirstChar() != prevData.getFirstChar()) {
                    mItem.setCatalogAndLineVisibility(View.VISIBLE);
                    mItem.setCatalogLetter(String.valueOf(mContact.getFirstChar()));
                } else {
                    mItem.setCatalogAndLineVisibility(View.GONE);
                }
            }
        }
    }

}
