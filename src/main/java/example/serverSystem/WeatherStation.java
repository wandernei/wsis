package example.serverSystem;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

public class WeatherStation {
    //region vars
    @SerializedName("stationId")
    private int stationId;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("temp")
    private int temp;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("irrigate")
    private boolean irrigate;

    @SerializedName("configuration")
    private String configuration;
//endregion

    //region GetSetters
    private void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStationId() {
        return this.stationId;
    }

    private void setDatetime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDatetime() {
        return this.dateTime;
    }

    private void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTemp() {
        return this.temp;
    }

    private void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public boolean isIrrigating() {
        return this.irrigate;
    }

    public void doIrrigate(boolean i) {
        this.irrigate = i;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getConfiguration() {
        return this.configuration;
    }
//endregion

    public WeatherStation(int stationId, boolean irrigate, String configuration) {
        this.setStationId(stationId);
        this.doIrrigate(irrigate);
        this.setConfiguration(configuration);
    }

    public WeatherStation(int stationId) {
        this.setStationId(stationId);
    }

    public WeatherStation setWeatherStation(String ws) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        WeatherStation w = gson.fromJson(ws, WeatherStation.class);
        this.setStationId(w.getStationId());
        this.setDatetime(w.getDatetime());
        this.setTemp(w.getTemp());
        this.setHumidity(w.getHumidity());
        this.setConfiguration(w.getConfiguration());
        return this;
    }

    public JsonObject setWeatherStation(String stationId, String dateTime, String temp, String humidity, String irrigate, String configuration) {
/*
{
    "_id": "1f23d773c3fe435ea2b6ee75bf9e85b7",
    "_rev": "1-7ee5d373dc48664833b1ab15d39b6b4a",
    "stationId": "43",
    "dateTime": "2018-10-13T00:43:53.807Z",
    "temp": "20",
    "humidity": "80",
    "irrigate": "0"
} */
        this.stationId = Integer.parseInt(stationId);
        this.dateTime = dateTime;
        this.temp = temp.equals("") ? 0 : Integer.parseInt(temp);
        this.humidity = temp.equals("") ? 0 : Integer.parseInt(humidity);
        this.irrigate = Boolean.parseBoolean(irrigate);
        this.configuration = configuration;

        Gson gson = new Gson();
        String wsString = gson.toJson(this, WeatherStation.class);
        JsonObject wsJsonObject = gson.fromJson(wsString, JsonObject.class);
        return wsJsonObject;
    }

    public JsonObject getWeatherStation(){
        Gson gson = new Gson();
        String wsString = gson.toJson(this, WeatherStation.class);
        JsonObject wsJsonObject = gson.fromJson(wsString, JsonObject.class);
        return wsJsonObject;
    }

    @Override
    public String toString() {
        return "{" +
                "\"stationId\"" + ":" + "\"" +stationId + "\"" +
                ", \"dateTime\""+ ":" + "\"" + dateTime + "\"" +
                ", \"temp\"" + ":" +  "\"" +temp + "\"" +
                ", \"humidity\"" + ":" + "\"" + humidity + "\"" +
                ", \"irrigate\"" + ":" +  "\"" +irrigate + "\"" +
                ", \"configuration\""+ ":" + "\"" + configuration + "\"" +
                "}";
    }

}

