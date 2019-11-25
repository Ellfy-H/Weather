package jmessage.example.com.weather;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//mainactivity类用于主界面展示
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //weather_detail.xml
    private Button refreshCity;
    private Button refreshWeather;
    private TextView txt_weather_detail_city;              //城市（可选择）
    private TextView txt_weather_detail_date;              //日期（可选择）
    private TextView txt_weather_detail_station;           //天气状况（如：晴天）
    private TextView txt_weather_detail_temperature;       //温度
    private TextView txt_weather_detail_windDir;           //风向
    private TextView txt_weather_detail_windSc;            //风力
    private TextView txt_weather_detail_week;              //星期（如：周五）
    private TextView txt_weather_detail_pm;                //PM2.5
    private TextView txt_weather_detail_airquality;        //空气质量
    private ImageView image_weather_detail_icon;           //天气状况图片

    //展示的是未来三天的天气
    //weather_day.xml
    //weather_day01
    private TextView txt_weather_day_week01;
    private TextView txt_weather_day_date01;
    private TextView txt_weather_day_temperature01;
    private TextView txt_weather_day_station01;
    private TextView txt_weather_day_windDir01;
    private TextView txt_weather_day_windSc01;
    private ImageView image_weather_day_icon01;

    //weather_day02
    private TextView txt_weather_day_week02;
    private TextView txt_weather_day_date02;
    private TextView txt_weather_day_temperature02;
    private TextView txt_weather_day_station02;
    private TextView txt_weather_day_windDir02;
    private TextView txt_weather_day_windSc02;
    private ImageView image_weather_day_icon02;

    //weather_day03
    private TextView txt_weather_day_week03;
    private TextView txt_weather_day_date03;
    private TextView txt_weather_day_temperature03;
    private TextView txt_weather_day_station03;
    private TextView txt_weather_day_windDir03;
    private TextView txt_weather_day_windSc03;
    private ImageView image_weather_day_icon03;

    //查询天气辅助工具类
    private WeatherAsynctaskSDK wasdk;
    //天气列表
    private List<CityWeather> listWeather  = new ArrayList<CityWeather>();

    //WeatherDB
    //初始化天气数据库对象
    private WeatherDB weatherDB;

    //默认选择城市
    private String citySelected = "beijing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化布局
        init();
        //获取天气信息
        getWeatherInfo();
    }

    /**
     * 初始化布局
     */
    private void init() {

        //weather_detail.xml
        refreshCity = (Button) findViewById(R.id.button1);
        refreshWeather = (Button) findViewById(R.id.button2);
        txt_weather_detail_city = (TextView) findViewById(R.id.txt_weather_detail_city);
        txt_weather_detail_date = (TextView) findViewById(R.id.txt_weather_detail_date);
        image_weather_detail_icon = (ImageView) findViewById(R.id.image_weather_detail_icon);
        txt_weather_detail_pm = (TextView) findViewById(R.id.txt_weather_detail_pm);
        txt_weather_detail_station = (TextView) findViewById(R.id.txt_weather_detail_station);
        txt_weather_detail_temperature = (TextView) findViewById(R.id.txt_weather_detail_temperature);
        txt_weather_detail_week = (TextView) findViewById(R.id.txt_weather_detail_week);
        txt_weather_detail_windDir = (TextView) findViewById(R.id.txt_weather_detail_windDir);
        txt_weather_detail_windSc = (TextView) findViewById(R.id.txt_weather_detail_windSc);
        txt_weather_detail_airquality = (TextView) findViewById(R.id.txt_weather_detail_airquality);

        txt_weather_detail_date.setOnClickListener(this);
        txt_weather_detail_city.setOnClickListener(this);

        //weather_day.xml
        //weather_day_01
        LinearLayout layoutWeather01 = (LinearLayout) findViewById(R.id.weather_day01);
        txt_weather_day_date01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon01 = (ImageView) layoutWeather01.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_station);

        //weather_day02
        LinearLayout layoutWeather02 = (LinearLayout) findViewById(R.id.weather_day02);
        txt_weather_day_date02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon02 = (ImageView) layoutWeather02.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_station);

        //weather_day03
        LinearLayout layoutWeather03 = (LinearLayout) findViewById(R.id.weather_day03);
        txt_weather_day_date03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon03 = (ImageView) layoutWeather03.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_station);
    }

    /**
     * 显示天气信息
     */
    private void initWeatherInfo() {

        listWeather = this.getWeatherList();
        if (listWeather.size() > 0) {

            //保证每次都是最新数据
            int count = listWeather.size();
            //weatherToday
            CityWeather weatherToday = listWeather.get(count-4);

            txt_weather_detail_date.setText(weatherToday.getDate());
            txt_weather_detail_week.setText(weatherToday.getWeek());
            txt_weather_detail_station.setText(weatherToday.getStation());
            txt_weather_detail_temperature.setText(weatherToday.getTemperature() + "℃");
            txt_weather_detail_windDir.setText(weatherToday.getWindDir());
            txt_weather_detail_windSc.setText(weatherToday.getWindSc() + "级");
            txt_weather_detail_pm.setText(weatherToday.getPM());
            this.init_background(weatherToday.getStationCode());

            CityWeather weatherForest01 = listWeather.get(count-3);
            txt_weather_day_date01.setText(weatherForest01.getDate());
            txt_weather_day_week01.setText(weatherForest01.getWeek());
            txt_weather_day_station01.setText(weatherForest01.getStation());
            txt_weather_day_temperature01.setText(weatherForest01.getTemperature() + "℃");
            txt_weather_day_windDir01.setText(weatherForest01.getWindDir());
            txt_weather_day_windSc01.setText(weatherForest01.getWindSc() + "级");

            CityWeather weatherForest02 = listWeather.get(count-2);
            txt_weather_day_date02.setText(weatherForest02.getDate());
            txt_weather_day_week02.setText(weatherForest02.getWeek());
            txt_weather_day_station02.setText(weatherForest02.getStation());
            txt_weather_day_temperature02.setText(weatherForest02.getTemperature() + "℃");
            txt_weather_day_windDir02.setText(weatherForest02.getWindDir());
            txt_weather_day_windSc02.setText(weatherForest02.getWindSc() + "级");

            CityWeather weatherForest03 = listWeather.get(count-1);
            txt_weather_day_date03.setText(weatherForest03.getDate());
            txt_weather_day_week03.setText(weatherForest03.getWeek());
            txt_weather_day_station03.setText(weatherForest03.getStation());
            txt_weather_day_temperature03.setText(weatherForest03.getTemperature() + "℃");
            txt_weather_day_windDir03.setText(weatherForest03.getWindDir());
            txt_weather_day_windSc03.setText(weatherForest03.getWindSc() + "级");

            //通过公开接口下载对应的天气图片
            String icon_url = "http://files.heweather.com/cond_icon/";
            new DownLoadImage(image_weather_detail_icon).execute(icon_url+weatherToday.getStationCode()+".png");
            new DownLoadImage(image_weather_day_icon01).execute(icon_url+weatherForest01.getStationCode()+".png");
            new DownLoadImage(image_weather_day_icon02).execute(icon_url+weatherForest02.getStationCode()+".png");
            new DownLoadImage(image_weather_day_icon03).execute(icon_url+weatherForest03.getStationCode()+".png");
        } else {
            Toast.makeText(this, "天气信息获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void getWeatherInfo() {
        weatherDB = WeatherDB.getInstance(MainActivity.this);
        //wasdk = new WeatherAsynctaskSDK(weatherDB);
        this.requestHttp(citySelected);
        //listWeather = wasdk.getWeatherList();
        //int size = listWeather.size();
        //Log.i("sdkdemo", "WeatherActivity中获取的天气列表长度为：" + size);
    }

    /**
     * 城市选择按钮监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_weather_detail_city:    //打开城市选择对话框
                citySelectDialog();
                break;
            case R.id.txt_weather_detail_date:  //打开日期选择对话框（定位到当前时间）
                break;
            case R.id.button1:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                String cityName = getIntent().getStringExtra("CityName");
                if (!TextUtils.isEmpty(cityName)) {
                    refreshWeather.setText("同步中...");
                    txt_weather_detail_city.setText(cityName);
                    refreshWeatherDialog();
                }
                break;
        }
    }


    /**
     * 刷新天气对话框
     */
    //自定义对话框
    public void refreshWeatherDialog() {

        //获取自定义布局
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.weather_city_refresh_dialog, null);

        //响应对话框中的点击事件
        /*// （需要findViewById()之前需要加view，表明是自定义view布局中的响应事件）
        final EditText edit = (EditText) view.findViewById(R.id.edt_weather_city_refresh_dialog);*/

        AlertDialog.Builder builderF = new AlertDialog.Builder(MainActivity.this);
        builderF.setTitle("刷新天气数据");
        //将自定义布局设置到对话框中
        builderF.setView(view);

        builderF.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                citySelected = getIntent().getStringExtra("CityName");/*txt_weather_detail_city.getText().toString();*/
                if (!TextUtils.isEmpty(citySelected)) {
                    getWeatherInfo();
                    refreshWeather.setText("更新");
                }

                dialog.dismiss();
                dialog.cancel();
            }
        });
        builderF.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "取消刷新天气数据", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                dialog.cancel();
            }
        });

        //所有关于alertdialog的设置，必须在show（）方法之前完成，才能有效果
        AlertDialog dialog = builderF.create();
        dialog.show();
    }

    /**
     * 城市选择对话框
     */
    //自定义对话框
    public void citySelectDialog() {

        //获取自定义布局
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.weather_city_select_dialog, null);

        //响应对话框中的点击事件
        // （需要findViewById()之前需要加view，表明是自定义view布局中的响应事件）
        final EditText edit = (EditText) view.findViewById(R.id.edt_weather_city_select_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入选择的城市");
        //将自定义布局设置到对话框中
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                citySelected = edit.getText().toString();
                Log.i("test", "选择的城市是：" + citySelected);
                if (!TextUtils.isEmpty(citySelected)) {

                    txt_weather_detail_city.setText(citySelected);
                    getWeatherInfo();
                }

                dialog.dismiss();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "您没有选择新的城市", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                dialog.cancel();
            }
        });

        //所有关于alertdialog的设置，必须在show（）方法之前完成，才能有效果
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class DownLoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownLoadImage(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap tmpBitmap = null;
            try {
                InputStream is = new java.net.URL(url).openStream();
                tmpBitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tmpBitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private void send_message(String phone, String message){

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(phone, null, message, pi, null);

    }

    private void init_background(String weatherCode){

        int code = Integer.parseInt(weatherCode);
        ImageView backgroungImage = (ImageView)findViewById(R.id.backfountdImage);

        int d;
        switch(code){
            case 100:
                d=R.drawable.bg_sunny;
                break;
            case 101: case 104:
                d=R.drawable.bg_overcast;
                break;
            case 102: case 103: case 201:
                d=R.drawable.bg_calm;
                break;
            case 200: case 202: case 203: case 204: case 205: case 206: case 207: case 208: case 209: case 210: case 211: case 212: case 213:
                d=R.drawable.bg_windy;
                break;
            case 300: case 301: case 302: case 303: case 304: case 305: case 306: case 307: case 308: case 309: case 310: case 311: case 312: case 313:
                d=R.drawable.bg_rain;
                break;
            case 400: case 401: case 402: case 403: case 404: case 405: case 406: case 407:
                d=R.drawable.bg_sonw;
                break;
            case 500: case 501: case 502: case 503: case 504: case 505: case 506: case 507: case 508:
                d=R.drawable.bg_foggy;
                break;
            default:
                d=R.drawable.bg_calm;
                break;
        }
        try {
            backgroungImage.setImageBitmap(readBitMap(MainActivity.this,d));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Bitmap readBitMap(Context context, int resId){
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
               //获取资源图片
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is,null,opt);
    }




    /**
     *
     * @param city       请求的城市
     * @return        天气情况的list结合
     */
    public void requestHttp(String city) {

        //参数
        Parameters para = new Parameters();
        para.put("city", city);
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free", ApiStoreSDK.GET, para, new ApiCallBack() {
            @Override
            public void onSuccess(int status, String responseString) {
                Log.i("sdkdemo", "onSuccess");

                //解析数据，获得天气信息，并保存
                jsonResult(responseString);

                initWeatherInfo();

            }

            @Override
            public void onComplete() {
                Log.i("sdkdemo", "onComplete");
            }

            @Override
            public void onError(int status, String responseString, Exception e) {
                Log.i("sdkdemo", "onError, status: " + status);
                Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
            }

        });

    }

    public List<CityWeather> getWeatherList(){

        List<CityWeather> list = this.weatherDB.loadCityWeather();
        Log.i("sdkdemo", "list的长度为：" + list.size());

        return list;
    }

    /**
     * 解析返回的json数据
     * @param responseString
     * @return
     */
    private void jsonResult(String responseString) {

        CityWeather weatherToday = new CityWeather();
        try {

            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);

            String isOk = jsonObject1.getString("status");

            Log.i("sdkdemo", "http返回值：" + isOk);

            if (isOk.equals("ok")) {

                //今日天气状况
                weatherDB.clearData();
                weatherToday = todayWeather(jsonObject1);
                //将数据保存到数据表中
                this.weatherDB.saveCityWeather(weatherToday);

                //未来三天的天气状况
                JSONArray jsonArray1 = jsonObject1.getJSONArray("daily_forecast");
                for (int i = 1; i < 4; i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
                    CityWeather cityWeather = forestWeather(jsonObject2);

                    //将数据保存到数据表中
                    this.weatherDB.saveCityWeather(cityWeather);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今天的天气状况
     * @param jsonObject1
     * @return
     * @throws JSONException
     */
    private CityWeather todayWeather(JSONObject jsonObject1) {

        CityWeather weatherToday = new CityWeather();
        try {
            {//天气状况基本信息

                JSONObject jsonBasic = jsonObject1.getJSONObject("basic");

                //城市名称
                String city_name = jsonBasic.getString("city");
                //城市id
                String city_id = jsonBasic.getString("id");

                //更新时间
                JSONObject jsonDate = jsonBasic.getJSONObject("update");
                //当地时间
                String date = jsonDate.getString("loc");

                //周几
                String week = Calendar(date);

                //将信息保存在CityWeather实例中
                weatherToday.setCityName(city_name);
                weatherToday.setCityID(city_id);
                weatherToday.setDate(date);
                weatherToday.setWeek(week);
            }
            {//实况天气

                JSONObject jsonNow = jsonObject1.getJSONObject("now");

                //天气状况
                JSONObject jsonStation = jsonNow.getJSONObject("cond");
                //天气状况代码
                String sta_code = jsonStation.getString("code");
                //天气状况描述
                String station = jsonStation.getString("txt");

                //温度
                String tempature = jsonNow.getString("tmp");

                //风力风向
                JSONObject jsonWind = jsonNow.getJSONObject("wind");
                //风向
                String windDir = jsonWind.getString("dir");
                //风力
                String windSc = jsonWind.getString("sc");

                weatherToday.setStation(station);
                weatherToday.setStationCode(sta_code);
                weatherToday.setTemperature(tempature);
                weatherToday.setWindDir(windDir);
                weatherToday.setWindSc(windSc);
            }
            {//空气质量，仅限国内部分城市

                JSONObject jsonAir = jsonObject1.getJSONObject("aqi");
                JSONObject jsonCity = jsonAir.getJSONObject("city");
                //PM2.5 1小时平均值(ug/m³)
                String pm = jsonCity.getString("pm25");
                //空气质量类别
                String airQ = jsonCity.getString("qlty");

                weatherToday.setPM(pm);
                weatherToday.setAirQlty(airQ);
                weatherToday.setIsToday(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherToday;
    }

    /**
     * 获取未来三天的天气状况
     * @param jsonObject2
     * @return
     */
    public CityWeather forestWeather(JSONObject jsonObject2) {

        CityWeather weatherForest = new CityWeather();

        try {

            //日期
            String date = jsonObject2.getString("date");

            //周几
            String week = Calendar(date);

            //天气状况（这里主要取白天）
            JSONObject jsonStation = jsonObject2.getJSONObject("cond");
            String sta_code = jsonStation.getString("code_d");
            String sta_txt = jsonStation.getString("txt_d");

            //温度
            JSONObject jsonTmp = jsonObject2.getJSONObject("tmp");
            String tmpMax = jsonTmp.getString("max");
            String tmpMin = jsonTmp.getString("min");

            //风力风向
            JSONObject jsonWind = jsonObject2.getJSONObject("wind");
            //风力
            String windSc = jsonWind.getString("sc");
            //风向
            String windDir = jsonWind.getString("dir");

            weatherForest.setIsToday(false);
            weatherForest.setDate(date);
            weatherForest.setWeek(week);
            weatherForest.setStation(sta_txt);
            weatherForest.setStationCode(sta_code);
            weatherForest.setTemperature(tmpMin + "~" + tmpMax);
            weatherForest.setWindSc(windSc);
            weatherForest.setWindDir(windDir);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherForest;
    }

    /**
     * 找出对应Date
     * @param dateTime
     * @return
     */
    public String Calendar(String dateTime) {

        String dayNames[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(dateTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dayNames[c.get(Calendar.DAY_OF_WEEK)-1];
    }

}
