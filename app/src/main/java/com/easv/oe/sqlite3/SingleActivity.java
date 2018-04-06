package com.easv.oe.sqlite3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleActivity extends Activity {

    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        dao = DAO.getInstance();

        int index = getIntent().getExtras().getInt("index");

        BEPerson current = dao.getAll().get(index);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtAddress = (TextView)findViewById(R.id.txtAddress);
        TextView txtPhone = (TextView)findViewById(R.id.txtPhone);
        TextView txtMail = (TextView)findViewById(R.id.txtMail);
        TextView txtBirthday = (TextView)findViewById(R.id.txtBirthday);
        TextView txtWebsite = (TextView)findViewById(R.id.txtWebsite);

        txtName.setText(current.m_name);
        txtAddress.setText(current.m_address);
        txtPhone.setText(current.m_phone);
        txtMail.setText(current.m_mail);
        txtBirthday.setText(current.m_birthday);
        txtWebsite.setText(current.m_website);
    }

    public void onClickHome(View v)
    {

    }

    public void onClickShow(View v)
    {

    }

    public void onClickDelete(View v)
    {
        dao = DAO.getInstance();

        int index = getIntent().getExtras().getInt("index");

        BEPerson current = dao.getAll().get(index);

        dao.deleteById(current.m_id);

        finish();
    }
}
