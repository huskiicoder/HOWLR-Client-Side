package edu.uw.tcss450.howlr.ui.weather;

public class Weather {

    private String typeTime;

    private int time;

    private double lowTemp;

    private double highTemp;

    private double currentTemp;

    private String curentWeather;

    private String date;

    private String city;

    private String icon;


    public Weather(String typeTime, int time, double lowTemp, double highTemp, double currentTemp,
                    String curentWeather, String date, String city, String icon) {
        this.typeTime = typeTime;
        this.time = time;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.currentTemp = currentTemp;
        this.curentWeather = curentWeather;
        this.date = date;
        this.city = city;
        this.icon = icon;
    }

    public Weather( double currentTemp, String curentWeather, String date, String city, String icon) {
        this.typeTime = "Current";
        this.time = 0;
        this.lowTemp = -1;
        this.highTemp = -1;
        this.currentTemp = currentTemp;
        this.curentWeather = curentWeather;
        this.date = date;
        this.city = city;
        this.icon = icon;
    }

    public Weather(int time, double currentTemp, String curentWeather, String icon) {
        this.typeTime = "Hourly";
        this.time = time;
        this.lowTemp = -1;
        this.highTemp = -1;
        this.currentTemp = currentTemp;
        this.curentWeather = curentWeather;
        this.date = "";
        this.city = "";
        this.icon = icon;
    }

    public Weather(double lowTemp, double highTemp, String date, String icon) {
        this.typeTime = "Daily";
        this.time =0;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.currentTemp = -1;
        this.curentWeather = "";
        this.date = date;
        this.city = "";
        this.icon = icon;
    }

    public String getTypeTime() {
        return typeTime;
    }

    public int getTime() {
        return time;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public String getCurentWeather() {
        return curentWeather;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public String getIcon() {
        return icon;
    }
}
