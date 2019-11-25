package jmessage.example.com.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper  extends SQLiteOpenHelper {

    //建表语句
    private static final String CREATE_CITY_WEATHER = "create table city_weather_table("
            + "_id integer primary key autoincrement,"
            + "city_name text,"
            + "city_id text,"
            + "date text,"
            + "week text,"
            + "station text,"
            + "station_code text,"
            + "temperature text,"
            + "windDir text,"
            + "windSc text,"
            + "pm text,"
            + "icon text"
            +")";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
