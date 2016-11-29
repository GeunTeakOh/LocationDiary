package com.taek_aaa.locationdiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 Table 생성
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitude REAL , longitude REAL, TodoOrEvent TEXT, category INTEGER, HowLong INTEGER, num TEXT, text TEXT, time TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists database;");
        onCreate(db);
    }

    public void insert(double latitude, double longitude, String todoOrEvent, int category, int howLong, int num, String text, String time) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO database VALUES(NULL, " + latitude + ", " + longitude + ", '" + todoOrEvent + "', " + category + ", " + howLong + ", " + num + ", '" + text + "', '" + time + "');");  //string넣을때는 '' 하고그안에""해야
        db.close();
    }

    public void getResult(LinkedList<DBData> sllDBData) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int t=0;

        // c = cursor;
        while (cursor.moveToNext()) {
            double latitudecur = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitudecur = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String toDoOrEventcur = cursor.getString(cursor.getColumnIndex("TodoOrEvent"));
            int categorycur = cursor.getInt(cursor.getColumnIndex("category"));
            int howLongcur = cursor.getInt(cursor.getColumnIndex("HowLong"));
            String numcur = cursor.getString(cursor.getColumnIndex("num"));
            String textcur = cursor.getString(cursor.getColumnIndex("text"));
            String timecur = cursor.getString(cursor.getColumnIndex("time"));

            Log.i("tt",String.valueOf(latitudecur));
            Log.i("tt",String.valueOf(longitudecur));
            Log.i("tt",toDoOrEventcur);
            Log.i("tt",String.valueOf(categorycur));
            Log.i("tt",String.valueOf(howLongcur));
            Log.i("tt",numcur);
            Log.i("tt",textcur);
            Log.i("tt",timecur);

            DBData dbdata = new DBData();
            dbdata.curlatitude = latitudecur;
            dbdata.curlongitude = longitudecur;
            dbdata.curTodoOrEvent = toDoOrEventcur;
            dbdata.curCategory = categorycur;
            dbdata.curHowLong = howLongcur;
            dbdata.curNum = numcur;
            dbdata.curText = textcur;
            dbdata.curTime = timecur;
            Log.i("tttt",String.valueOf(dbdata.curlatitude));
            Log.i("tttt",String.valueOf(dbdata.curlongitude));
            Log.i("tttt",dbdata.curTodoOrEvent);
            Log.i("tttt",String.valueOf(dbdata.curCategory));
            Log.i("tttt",String.valueOf(dbdata.curHowLong));
            Log.i("tttt",dbdata.curNum);
            Log.i("tttt",dbdata.curText);
            Log.i("tttt",dbdata.curTime);

            sllDBData.add(dbdata);
            t++;
        }
        cursor.close();
        db.close();
    }

    public int getIter(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int tempdbiter=0;
        while (cursor.moveToNext()) {
            tempdbiter = cursor.getInt(cursor.getColumnIndex("num"));
            Log.e("qwe", String.valueOf(tempdbiter));
        }
        return tempdbiter;
    }


}
