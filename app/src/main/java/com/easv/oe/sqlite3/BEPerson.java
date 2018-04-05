package com.easv.oe.sqlite3;

public class BEPerson {

    long m_id;
    String m_name;
    String m_mail;
    String m_website;
    String m_phone;
    String m_birthday;
    String m_address;

    public BEPerson(long id, String name, String mail, String website, String phone, String birthday, String address) {
        m_id = id;
        m_name = name;
        m_mail = mail;
        m_website = website;
        m_phone = phone;
        m_birthday = birthday;
        m_address = address;

    }

    public String toString() {
        return "" + m_id + ": " + m_name + m_mail + m_website + m_phone + m_birthday + m_address;
    }

}
