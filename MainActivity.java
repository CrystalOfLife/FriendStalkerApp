package com.easv.oe.sqlite3;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    EditText etName;
    Button insertButton;
    Button deleteButton;
    ListView lvNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DAO.setContext(this);



        etName = (EditText) findViewById(R.id.etName);
        insertButton = (Button) findViewById(R.id.btnInsert);
        deleteButton = (Button) findViewById(R.id.btnDelete);
        lvNames = (ListView) findViewById(R.id.lvNames);
        fillList();
        lvNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent x = new Intent();
                x.setClass(MainActivity.this, SingleActivity.class);
                x.putExtra("index", position);
                startActivity(x);
                //Toast.makeText(MainActivity.this, "Say hi to " + DAO.getInstance().getByIndex(position).m_name, Toast.LENGTH_LONG).show();
            }
        });


        insertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onClickInsert();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onClickDeleteAll();
            }
        });


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        fillList();
    }

    void onClickInsert() {
        DAO dao = DAO.getInstance();
        String name = etName.getText().toString();
        String mail = etName.getText().toString();
        String website = etName.getText().toString();
        String phone = etName.getText().toString();
        String birthday = etName.getText().toString();
        String address = etName.getText().toString();
        dao.insert(new BEPerson(0, name, mail, website, phone, birthday, address));
        etName.setText("");
        fillList();
    }

    void onClickDeleteAll() {
        DAO dao = DAO.getInstance();
        dao.deleteAll();
        fillList();
    }

    void fillList() {
        DAO dao = DAO.getInstance();

        ArrayAdapter<BEPerson> a =
                new ArrayAdapter<BEPerson>(this,
                        android.R.layout.simple_list_item_1,
                        dao.getAll() );
        lvNames.setAdapter(a);
    }
}