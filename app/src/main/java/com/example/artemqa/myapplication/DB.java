package com.example.artemqa.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

 class DB extends SQLiteOpenHelper {

     public static final String TABLE = "mytable";
     public static final String DB_NAME = "DB";



     public DB(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String [][] data = new String[5][3];
        data[0][0] = "Токио";
        data[0][1] = "35.6895000";
        data[0][2] = "139.6917100";

        data[1][0] = "Москва";
        data[1][1] = "55.75222";
        data[1][2] = "37.61556";

        data[2][0] = "Сан Франциско";
        data[2][1] = "37.77493008";
        data[2][2] = "-122.4194200";

        data[3][0] = "Владивосток";
        data[3][1] = "43.1056200";
        data[3][2] = "131.8735300";

        data[4][0] = "Дурбан";
        data[4][1] = "-29.8579000";
        data[4][2] = "31.0292000";



        ContentValues content = new ContentValues();

        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "city text,"
                + "lat text,"
                + "lot text,"
                + "weather text" + ");");


        for(int i =0; i<5; i++) {
            content.put("city", data[i][0]);
            content.put("lat", data[i][1]);
            content.put("lot", data[i][2]);
            content.put("weather", "");
            db.insert(TABLE, null, content);
            content.clear();
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
