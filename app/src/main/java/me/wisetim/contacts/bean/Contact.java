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
package me.wisetim.contacts.bean;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import me.wisetim.contacts.util.HanziToPinyinUtil;

import java.util.Objects;
import java.util.UUID;

public class Contact implements Comparable<Contact>,Parcelable {
    private String name;
    private String phoneNumber;
    private UUID id;
    private String url;
    private String pinyin;
    private char firstChar;

    public Contact(UUID id) {
        this.id = id;
    }

    public Contact() {
        id = UUID.randomUUID();
    }

    public String getPinyin() {
        return pinyin;
    }

    private void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String first = pinyin.substring(0, 1);
        if (first.matches("[A-Za-z]")) {
            firstChar = first.toUpperCase().charAt(0);
        } else {
            firstChar = '#';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setPinyin(HanziToPinyinUtil.getPinYin(name));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public char getFirstChar() {
        return firstChar;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int compareTo(@NonNull Contact another) {
        return this.pinyin.compareTo(another.getPinyin());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {

        return Objects.hash(id);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
        dest.writeSerializable(this.id);
        dest.writeString(this.url);
        dest.writeString(this.pinyin);
        dest.writeInt(this.firstChar);
    }

    protected Contact(Parcel in) {
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.id = (UUID) in.readSerializable();
        this.url = in.readString();
        this.pinyin = in.readString();
        this.firstChar = (char) in.readInt();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
