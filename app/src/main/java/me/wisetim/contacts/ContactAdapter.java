package me.wisetim.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.kymjs.contacts.R;
import me.wisetim.contacts.bean.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactAdapter extends ArrayAdapter<Contact>
        implements AbsListView.OnScrollListener,SectionIndexer {
    private AbsListView.OnScrollListener listener;

    public ContactAdapter(@NonNull Context context, int resource,
                          @NonNull List<Contact> contacts) {
        super(context, resource, contacts);
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_contact, null);
            holder = new ContactHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ContactHolder) view.getTag();
        }
        holder.bindContact(getItem(position), position);
        return view;
    }

    class ContactHolder {
        @BindView(R.id.contact_head)
        ImageView mContactHead;
        @BindView(R.id.contact_title)
        TextView mContactName;
        @BindView(R.id.contact_catalog)
        TextView mLetter;
        @BindView(R.id.contact_line)
        TextView mLine;

        ContactHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bindContact(Contact contact, int position) {
            mContactName.setText(contact.getName());

            if (position == 0) {
                mLetter.setVisibility(View.VISIBLE);
                mLetter.setText(String.valueOf(contact.getFirstChar()));
                mLine.setVisibility(View.VISIBLE);
            } else {
                Contact prevData = getItem(position - 1);
                if (contact.getFirstChar() != prevData.getFirstChar()) {
                    mLetter.setVisibility(View.VISIBLE);
                    mLetter.setText(String.valueOf(contact.getFirstChar()));
                    mLine.setVisibility(View.VISIBLE);
                } else {
                    mLetter.setVisibility(View.GONE);
                    mLine.setVisibility(View.GONE);
                }
            }
        }
    }

}
