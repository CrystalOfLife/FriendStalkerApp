package com.example.nicolai;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easv.oe.sqlite3.R;

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

        txtName.setText(current.m_name);
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
