package com.example.searchablenotepad;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter extends SQLiteOpenHelper{
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "searchBase";
 
    // Contacts table name
    private static final String TABLE_NAME = "searchKeywords";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String DATA_NAME = "Character";
    private static final String KEY_FILE = "File_name";
    
    public DatabaseAdapter(Context context){
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db){
    	String CREATE_DATA_TABLE = "CREATE TABLE "+TABLE_NAME +"("+KEY_ID + " INTEGER PRIMARY KEY," 
    + DATA_NAME +" TEXT, "+ KEY_FILE +" TEXT"+ ")";
    	db.execSQL(CREATE_DATA_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    	db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    	onCreate(db);
    }
    
    public void addData(Data data){
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(DATA_NAME, data.getData()); // Contact Name
        values.put(KEY_FILE, data.getFile()); // Contact Phone Number
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    
    public List<Data> getAllData() {
        List<Data> dataList = new ArrayList<Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Data d = new Data();
                d.setId(Integer.parseInt(cursor.getString(0)));
                d.setData(cursor.getString(1));
                d.setFile(cursor.getString(2));
                // Adding contact to list
                dataList.add(d);
            } while (cursor.moveToNext());
        }
        db.close();
        return dataList;
    }
    
    public void deleteData(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }
}