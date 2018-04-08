package com.easv.oe.sqlite3;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    Button deleteButton;
    ListView lvNames;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DAO.setContext(this);

        deleteButton = (Button) findViewById(R.id.btnDelete);
        lvNames = (ListView) findViewById(R.id.lvNames);
        fillList();

        /**
         * when clicking on item in list, navigate to SingleActivity with the position of the item
         */
        lvNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent x = new Intent();
                x.setClass(MainActivity.this, SingleActivity.class);
                x.putExtra("index", position);
                startActivity(x);
            }
        });

        /**
         * activates on button press, deletes everything in the list
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onClickDeleteAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {
            case R.id.addFriend:
               // openAddFriendActivity();

                if(isServicesOK()){
                    Intent i = new Intent(this, MapsActivity.class);
                    startActivity(i);
                }

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        fillList();
    }

    /**
     * Opens the addFriendActivity
     */
    private void openAddFriendActivity() {
        Intent x = new Intent();
        x.setClass(MainActivity.this, AddFriendActivity.class);
        startActivity(x);
    }

    /**
     * Deletes all items on the list and updates it
     */
    void onClickDeleteAll() {
        DAO dao = DAO.getInstance();
        dao.deleteAll();
        fillList();
    }

    /**
     * fills the list based on what we have defined in BEPerson
     */
    void fillList() {
        DAO dao = DAO.getInstance();

        ArrayAdapter<BEPerson> a =
                new ArrayAdapter<BEPerson>(this,
                        android.R.layout.simple_list_item_1,
                        dao.getAll() );
        lvNames.setAdapter(a);
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}