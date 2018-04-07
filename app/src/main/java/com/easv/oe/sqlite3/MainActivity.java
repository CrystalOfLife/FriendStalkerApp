package com.easv.oe.sqlite3;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    Bitmap profilePicture;

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

        // when clicking on item in list, navigate to SingleActivity with the position of the item
        lvNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent x = new Intent();
                x.setClass(MainActivity.this, SingleActivity.class);
                x.putExtra("index", position);
                startActivity(x);
            }
        });

        // activates on button press, adds a BEPerson
        insertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onClickInsert();
            }
        });
        // activates on button press, deletes everything in the list
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

    // Creates a new BEPerson and adds it to the list, based on in the information provided below
    void onClickInsert() {
        DAO dao = DAO.getInstance();
        String name = etName.getText().toString();
        String mail = "12";
        String website = "google.com";
        String phone = "75424015";
        String birthday = etName.getText().toString();
        String address = etName.getText().toString();
        Bitmap picture = profilePicture;
        dao.insert(new BEPerson(0, name, mail, website, phone, birthday, address, picture));
        etName.setText("");
        fillList();
    }

    // Deletes all items on the list and updates it
    void onClickDeleteAll() {
        DAO dao = DAO.getInstance();
        dao.deleteAll();
        fillList();
    }

    // fills the list based on what we have defined in BEPerson
    void fillList() {
        DAO dao = DAO.getInstance();

        ArrayAdapter<BEPerson> a =
                new ArrayAdapter<BEPerson>(this,
                        android.R.layout.simple_list_item_1,
                        dao.getAll() );
        lvNames.setAdapter(a);
    }
}