package com.example.haneum_d;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VER = 1;
    public static final String DATABASE_NAME = "E_HaneumD.db";

    private Context context;

    //ID , TOPIC , CHAPTER , STEP_NUM, TEXT_IDX , SENTENCE_A, AUDIOFILE_STR, RECORDFILE_STR, SENTENCE_Q, SUB_TOPIC, KEYWWORD_IDX
    public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ContentsInfo.TABLE_NAME + " (" +
                        ContentsInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ContentsInfo.COLUMN_TOPIC + " TEXT,"+
                        ContentsInfo.COLUMN_CHAPTER + " TEXT,"+
                        ContentsInfo.COLUMN_STEP_NUM + " TEXT,"+
                        ContentsInfo.COLUMN_TEXT_IDX + " TEXT,"+
                        ContentsInfo.COLUMN_SENTENCE_A + " TEXT,"+
                        ContentsInfo.COLUMN_AUDIOFILE_STR + " TEXT,"+
                        ContentsInfo.COLUMN_RECORDFILE_STR + " TEXT,"+
                        ContentsInfo.COLUMN_SENTENCE_Q + " TEXT,"+
                        ContentsInfo.COLUMN_SUB_TOPIC + " TEXT,"+
                        ContentsInfo.COLUMN_KEYWORD_IDX + " TEXT," +
                        ContentsInfo.COLUMN_HCHECK + " INTEGER DEFAULT 0)";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ContentsInfo.TABLE_NAME;

    public DBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VER);
        this.context = context;

        this.getReadableDatabase();
        dbCopy();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    private void dbCopy() {
        String E_DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        String E_DB_NAME = DATABASE_NAME;
        String A_DB_NAME = "Asset_HaneumD.db";
        try {

            File folder = new File(E_DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = context.getAssets().open(A_DB_NAME);
            String out_filename = E_DB_PATH + E_DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy","IOException 발생함");
        }
    }

    private void dataBaseCheck() {
        String E_DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        String E_DB_NAME = DATABASE_NAME;
        File dbFile = new File(E_DB_PATH + E_DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d("DB","Database is copied.");
        }
    }

}
