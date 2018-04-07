package com.easv.oe.sqlite3;

import android.graphics.Bitmap;

public class BEPerson {

    long m_id;
    String m_name;
    String m_mail;
    String m_website;
    String m_phone;
    String m_birthday;
    String m_address;
    Bitmap m_picture;

    public BEPerson(long id, String name, String mail, String website, String phone, String birthday, String address, Bitmap picture) {
        m_id = id;
        m_name = name;
        m_mail = mail;
        m_website = website;
        m_phone = phone;
        m_birthday = birthday;
        m_address = address;
        m_picture = picture;
    }

    public String toString() {
        return "" + m_name;
    }

}
