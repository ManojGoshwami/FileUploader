package com.sushi.fileUploader.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final Object Lock = new Object();
    public static final String DATABASE_NAME = "FileUploader.db";
    public static final int Version = 1;
    private static DatabaseHelper instance;

    //==============================All Tables===================

    public static final String IMAGE_FILE_MST = "IMAGE_FILE_MST";


    //====================Camera Image Parameters===================

    public static final String ID = "ID";
    public static final String UNIQUE_ID = "UNIQUE_ID";
    public static final String IMAGE_NAME_PATH = "IMAGE_NAME_PATH";
    public static final String IMAGE_NAME = "IMAGE_NAME";


    public SQLiteDatabase openDatabase(boolean isWriteAble) {
        if (isWriteAble)
            return getWritableDatabase();
        else
            return getReadableDatabase();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Version);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String FILE_MST = "CREATE TABLE " + IMAGE_FILE_MST + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UNIQUE_ID + " TEXT," +              //May be Account no or cin no etc.
                IMAGE_NAME + " TEXT," +
                IMAGE_NAME_PATH + " TEXT" +
                ");";


        db.execSQL(FILE_MST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
