package com.sooksinyip.weightwatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDBHelper extends SQLiteOpenHelper {

    private static final String name = "foods.sqlite3";
    private static final int version = 2;


    public FoodDBHelper(Context ctx) {
        super(ctx, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE food (" +
                "_id integer primary key autoincrement," +
                "fname text not null," +             // course code
                "famount int default 0," +           // credit
                "fcal text not null);";           // letter grade e.g. A, B+

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS food;";
        db.execSQL(sql);
        this.onCreate(db);
    }
}
