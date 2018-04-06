package com.easv.oe.sqlite3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DAO {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_NAME = "friend_table";

    private static Context context;

    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;




    private static DAO m_instance;

    public static void setContext(Context c)
    {
        context = c;
    }

    public static DAO getInstance()
    {
        if (m_instance == null) m_instance = new DAO(context);
        return m_instance;
    }

    private DAO(Context context) {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    //add new BEPerson
    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, mail, website, phone, birthday, address) values (?, ?, ?, ?, ?, ?)";

    public long insert(BEPerson p) {
        this.insertStmt.bindString(1, p.m_name);
        this.insertStmt.bindString(2, p.m_mail);
        this.insertStmt.bindString(3, p.m_website);
        this.insertStmt.bindString(4, p.m_phone);
        this.insertStmt.bindString(5, p.m_birthday);
        this.insertStmt.bindString(6, p.m_address);
        long id = this.insertStmt.executeInsert();
        p.m_id = id;
        return id;
    }

    // deletes all BEPerson
    public void deleteAll() {

        this.db.delete(TABLE_NAME, null, null);
    }

    // deletes a s specific BEPerson
    public void deleteById(long id)
    {
        this.db.delete(TABLE_NAME, "id = " + id, null);
    }

    public void update(BEPerson p)
    {

    }
    // gets the whole list of BEPerson
    public List<BEPerson> getAll() {
        List<BEPerson> list = new ArrayList<BEPerson>();
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"id", "name", "mail", "website", "phone", "birthday", "address"},
                null, null,
                null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new BEPerson(cursor.getInt(0),
                                      cursor.getString(1),
                                      cursor.getString(2),
                                      cursor.getString(3),
                                      cursor.getString(4),
                                      cursor.getString(5),
                                      cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    // gets BePerson by their index
    public BEPerson getByIndex(int index)
    {
        return getAll().get(index);
    }


    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creates a new database with the defined rows
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, name TEXT, mail TEXT, website TEXT, phone TEXT, birthday TEXT, address TEXT)");

        }

        // deletes the previous database and creates a new one
        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
