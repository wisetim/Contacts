/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.wisetim.contacts;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.kymjs.contacts.R;
import me.wisetim.contacts.bean.Contact;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class ContactAdapter2 extends KJAdapter<Contact> implements SectionIndexer {
    private List<Contact> mContacts;

    public ContactAdapter2(AbsListView view, List<Contact> contacts) {
        super(view, contacts, R.layout.item_list_contact);
        mContacts = contacts;
        if (mContacts == null) {
            mContacts = new ArrayList<>();
        }
        Collections.sort(mContacts);
    }

    @Override
    public void convert(AdapterHolder holder, Contact item, boolean isScrolling) {
    }

    @Override
    public void convert(AdapterHolder holder, Contact contact, boolean isScrolling, int position) {

        holder.setText(R.id.contact_title, contact.getName());
        ImageView headImg = holder.getView(R.id.contact_head);

        TextView tvLetter = holder.getView(R.id.contact_catalog);
        TextView tvLine = holder.getView(R.id.contact_line);

        if (position == 0) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText(String.valueOf(contact.getFirstChar()));
            tvLine.setVisibility(View.VISIBLE);
        } else {
            Contact prevData = mContacts.get(position - 1);
            if (contact.getFirstChar() != prevData.getFirstChar()) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(String.valueOf(contact.getFirstChar()));
                tvLine.setVisibility(View.VISIBLE);
            } else {
                tvLetter.setVisibility(View.GONE);
                tvLine.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        Contact item = mContacts.get(position);
        return item.getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = mContacts.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
