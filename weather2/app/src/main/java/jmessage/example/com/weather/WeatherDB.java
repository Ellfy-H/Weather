package jmessage.example.com.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class WeatherDB {

    /**
     * 数据库名称
     */
    public static final String WEATHER_DB_NAME = "Weather";

    /**
     * 数据库版本
     */
    public static final int VERSON = 1;

    private static WeatherDB weatherDB;

    private SQLiteDatabase sqLiteDatabase;

    /**
     * 将构造方法私有化
     */
    private WeatherDB(Context context) {
        WeatherOpenHelper helper = new WeatherOpenHelper(context, WEATHER_DB_NAME, null, VERSON);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    /**
     * 获取WeatherDB的实例
     */
    public synchronized static WeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    /**
     * 将CityWeahter的实例存储到数据库
     * @param cityWeather
     */
    public void saveCityWeather(CityWeather cityWeather) {

        if (cityWeather != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", cityWeather.getCityName());
            values.put("city_id", cityWeather.getCityID());
            values.put("date", cityWeather.getDate());
            values.put("week", cityWeather.getWeek());
            values.put("station", cityWeather.getStation());
            values.put("station_code", cityWeather.getStationCode());
            values.put("windDir", cityWeather.getWindDir());
            values.put("windSc", cityWeather.getWindSc());
            values.put("pm", cityWeather.getPM());
            values.put("temperature", cityWeather.getTemperature());
//            values.put("icon", cityWeather.getIcon());

            sqLiteDatabase.insert("city_weather_table", null, values);
        }
    }
    public List<CityWeather> loadCityWeather() {

        List<CityWeather> list = new ArrayList<CityWeather>();
        Cursor cursor = sqLiteDatabase.query("city_weather_table", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                CityWeather cityWeather = new CityWeather();
                cityWeather.setCityID(cursor.getString(cursor.getColumnIndex("city_id")));
                cityWeather.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                cityWeather.setDate(cursor.getString(cursor.getColumnIndex("date")));
//                cityWeather.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                cityWeather.setPM(cursor.getString(cursor.getColumnIndex("pm")));
                cityWeather.setStation(cursor.getString(cursor.getColumnIndex("station")));
                cityWeather.setStationCode(cursor.getString(cursor.getColumnIndex("station_code")));
                cityWeather.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                cityWeather.setWeek(cursor.getString(cursor.getColumnIndex("week")));
                cityWeather.setWindDir(cursor.getString(cursor.getColumnIndex("windDir")));
                cityWeather.setWindSc(cursor.getString(cursor.getColumnIndex("windSc")));

                list.add(cityWeather);

            }while (cursor.moveToNext());

            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

    public void clearData(){
        sqLiteDatabase.execSQL("DELETE FROM city_weather_table");
    }

    /**
     * 关闭数据库
     */
    public void closeSQL() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}
