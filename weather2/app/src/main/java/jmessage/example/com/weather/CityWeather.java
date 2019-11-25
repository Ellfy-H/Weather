package jmessage.example.com.weather;


//城市天气的构造器类
public class CityWeather {

    private boolean isToday;           //用于判断，显示的天气是今天的还是未来三天的
    private String city_name;          //所在城市
    private String city_id;            //城市ID（备用）
    private String date;               //日期
    private String station;            //天气状况
    private String stationCode;        //天气状况代码（对应天气图标）
    private String temperature;          //气温
    private String windDir;            //风向
    private String windSc;             //风力
    private String pm;                 //PM2.5
    private String week;               //星期
    private String icon;               //天气图标
    private String airQlty;            //空气质量

    public boolean isToday() {
        return isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCityID() {
        return city_id;
    }

    public void setCityID(String city_id) {
        this.city_id = city_id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSc() {
        return windSc;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getPM() {
        return pm;
    }

    public void setPM(String pm) {
        this.pm = pm;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCityName(String city) {
        city_name = city;
    }

    public String getCityName() {
        return city_name;
    }

    public String getAirQlty() {
        return airQlty;
    }

    public void setAirQlty(String airQlty) {
        this.airQlty = airQlty;
    }
}
