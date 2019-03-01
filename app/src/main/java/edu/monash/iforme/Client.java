package edu.monash.iforme;

/**
 * Created by yashkhandha on 24/04/2018.
 */

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Client {

    /**
     * to fetch coordinates from geocoding address
     * @param params
     * @param values
     * @return textResult
     */
    public static String getCoOrdinates(String[] params, String[] values){
            //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        char[] c = null;
       String query_parameter = "address=";
       //parsing address in required format for google geocoding
        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                c = values[i].toCharArray();
                for (int j = 0; j < c.length;j++){
                    if( c[j] == ' '){
                        c[j] = '+';
                    }
                }
                char[] x = c;
                System.out.println(x);
                query_parameter += String.valueOf(c);
            }

            }


        //Making HTTP request
        try {
            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?" + query_parameter + "&key=AIzaSyCJ9HKJzuANUwAvA_hsi-azm-CiTX8K-Sg");

            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");

            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    /**
     * parse JSON to get coordinates
     * @param result
     * @return
     */
    public static String[] getCoOrdinatesFetch(String result){
        String coordinates[] = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray root = (JSONArray) jsonObject.get("results");
            JSONObject details = root.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            String latitude = details.getString("lat");
            String longitude = details.getString("lng");
            coordinates = new String[]{latitude, longitude};

        }catch (Exception e){
            e.printStackTrace();
        }
        return coordinates;
    }


}
