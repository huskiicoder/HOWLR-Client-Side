package edu.uw.tcss450.howlr.ui.weather;

/**
 * The Weather class that generates the current weather (hourly and daily).
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class Weather {

    /**
     * The timezone.
     */
    private String typeTime;

    /**
     * The time value.
     */
    private int time;

    /**
     * The temperature low.
     */
    private double lowTemp;

    /**
     * The temperature high.
     */
    private double highTemp;

    /**
     * The current temperature in Fahrenheit.
     */
    private double currentTemp;

    /**
     * The current weather.
     */
    private String currentWeather;

    /**
     * The date.
     */
    private String date;

    /**
     * The city name.
     */
    private String city;

    /**
     * The weather icon.
     */
    private String icon;

    /**
     * The humidity.
     */
    private int humidity;

    /**
     * Creates the weather using the weather information.
     * @param typeTime The timezone
     * @param time The time
     * @param lowTemp The temperature low
     * @param highTemp The temperature high
     * @param currentTemp The current temperature
     * @param currentWeather The current weather
     * @param date The date
     * @param city The city name
     * @param icon The weather icon
     * @param humidity The humidity
     */
    public Weather(String typeTime, int time, double lowTemp, double highTemp, double currentTemp,
                    String currentWeather, String date, String city, String icon, int humidity) {
        this.typeTime = typeTime;
        this.time = time;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.currentTemp = currentTemp;
        this.currentWeather = currentWeather;
        this.date = date;
        this.city = city;
        this.icon = icon;
        this.humidity = humidity;
    }

    /**
     * Creates the weather using the weather information.
     * @param currentTemp The current temperature
     * @param currentWeather The current weather
     * @param date The date
     * @param city The city name
     * @param icon The weather icon
     * @param humidity The humidity
     */
    public Weather( double currentTemp, String currentWeather, String date, String city, String icon,
                    int humidity) {
        this.typeTime = "Current";
        this.time = 0;
        this.lowTemp = -1;
        this.highTemp = -1;
        this.currentTemp = currentTemp;
        this.currentWeather = currentWeather;
        this.date = date;
        this.city = city;
        this.icon = icon;
        this.humidity = humidity;
    }

    /**
     * Creates the weather using the weather information.
     * @param time The time
     * @param currentTemp The current temperature
     * @param currentWeather The current weather
     * @param icon The weather icon
     * @param humidity The humidity
     */
    public Weather(int time, double currentTemp, String currentWeather, String icon, int humidity) {
        this.typeTime = "Hourly";
        this.time = time;
        this.lowTemp = -1;
        this.highTemp = -1;
        this.currentTemp = currentTemp;
        this.currentWeather = currentWeather;
        this.date = "";
        this.city = "";
        this.icon = icon;
        this.humidity = humidity;
    }

    /**
     * Creates the weather using the weather information.
     * @param lowTemp The temperature low
     * @param highTemp The temperature high
     * @param date The date
     * @param icon The weather icon
     * @param humidity The humidity
     */
    public Weather(double lowTemp, double highTemp, String date, String icon, int humidity) {
        this.typeTime = "Daily";
        this.time =0;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.currentTemp = -1;
        this.currentWeather = "";
        this.date = date;
        this.city = "";
        this.icon = icon;
        this.humidity = humidity;
    }

    /**
     * Gets the timezone.
     * @return The timezone
     */
    public String getTypeTime() {
        return typeTime;
    }

    /**
     * Gets the time.
     * @return The time
     */
    public int getTime() {
        return time;
    }

    /**
     * Gets the temperature low.
     * @return The temperature low
     */
    public double getLowTemp() {
        return lowTemp;
    }

    /**
     * Gets the temperature high.
     * @return The temperature high
     */
    public double getHighTemp() {
        return highTemp;
    }

    /**
     * Gets the current temperature.
     * @return The current temperature
     */
    public double getCurrentTemp() {
        return currentTemp;
    }

    /**
     * Gets the current weather.
     * @return The current weather
     */
    public String getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Gets the date.
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the city name.
     * @return The city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the weather icon.
     * @return The weather icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Gets the humidity.
     * @return The humidity
     */
    public int getHumidity() {
        return humidity;
    }
}