package com.fateczl.times_app_android.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "TIME.DB";
    private static final int DATABASEVER = 1;

    private static final String CREATE_TABLE_TIME=
            "CREATE TABLE time (" +
            "    codigo INT NOT NULL PRIMARY KEY," +
            "    nome VARCHAR(100) NOT NULL," +
            "    cidade VARCHAR(100) NOT NULL" +
            ");";

    private static final String CREATE_TABLE_JOG=
            "CREATE TABLE jogador (" +
            "    id INT NOT NULL PRIMARY KEY," +
            "    nome VARCHAR(100) NOT NULL," +
            "    dataNasc DATE NOT NULL," +
            "    altura REAL NOT NULL," +
            "    peso REAL NOT NULL," +
            "    time_id INT NOT NULL," +
            "    FOREIGN KEY (time_codigo) REFERENCES Time(codigo)" +
            ");";

    public GenericDao(Context context){
        super(context, DATABASE, null, DATABASEVER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TIME);
        sqLiteDatabase.execSQL(CREATE_TABLE_JOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS jogador");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS time");
            onCreate(sqLiteDatabase);
        }
    }
}
