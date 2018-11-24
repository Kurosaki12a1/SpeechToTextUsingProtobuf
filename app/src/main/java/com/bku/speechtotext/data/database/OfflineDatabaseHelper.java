package com.bku.speechtotext.data.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_language";

    private Context context;

    public OfflineDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SettingsLanguage.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SettingsLanguage.TABLE_NAME);
        onCreate(db);
    }

    public int addLanguage(SettingsLanguage settingsLanguage){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(SettingsLanguage.DEFAULT_LANGUAGE,settingsLanguage.getLanguage());
        long id = db.insert(SettingsLanguage.TABLE_NAME, null, value);
        db.close();
        return  (int) id;
    }

    public void deleteLanguage(String language){
        SQLiteDatabase db = getWritableDatabase();
        //  delete the job id in table Jon
        db.delete(SettingsLanguage.TABLE_NAME, "Language = ?", new String[]{(language)});
        db.close();
    }


    public SettingsLanguage getLanguageFromData(){
        String selectQuery = "SELECT * FROM " + SettingsLanguage.TABLE_NAME ;
        SettingsLanguage settingsLanguage = new SettingsLanguage("default");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
             settingsLanguage.setLanguage(cursor.getString(cursor.getColumnIndex(SettingsLanguage.DEFAULT_LANGUAGE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return settingsLanguage;

    }
}
