package com.easv.oe.sqlite3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SingleActivity extends Activity {

    DAO dao;
    String extraEmail;

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
        ImageButton callBtn = (ImageButton)findViewById(R.id.callBtn);
        ImageButton mailBtn = (ImageButton)findViewById(R.id.mailBtn);
        ImageButton smsBtn = (ImageButton)findViewById(R.id.smsBtn);
        ImageButton websiteBtn = (ImageButton)findViewById(R.id.websiteBtn);
        txtName.setText(current.m_name);
        txtAddress.setText(current.m_address);
        txtPhone.setText(current.m_phone);
        txtMail.setText(current.m_mail);
        txtBirthday.setText(current.m_birthday);
        txtWebsite.setText(current.m_website);

        extraEmail = txtMail.getText().toString();


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{txtMail.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        startActivity(Intent.createChooser(intent, ""));


        mailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{extraEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }

    public void onClickMail()
    {

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
