package edu.monash.iforme;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ykha0002 on 23/4/18.
 */

public class SearchWeatherAPI {

    private static final String API_KEY = "225cd6f57f7758ca76662d47c4952dec";
    /**
     * to search for JSON with co-ordinates to get weather
     * @param params
     * @param values
     * @return
     */
    public static String search(String[] params, String[] values) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter = "";
        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }

        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?APPID="+API_KEY+query_parameter);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }
    /**
     * parse JSON to get temperature
     * @param result
     * @return snippet
     */
    public static String getSnippet(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONObject js = (JSONObject) jsonObject.get("main");
            if(js != null) {
                snippet =js.getString("temp");
            }
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
        //return Math.round((Double.valueOf(snippet)/Double.valueOf(snippet)*100)*10)/10.0;
    }


}

