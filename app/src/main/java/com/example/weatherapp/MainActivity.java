package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = BuildConfig.apiKey;
    EditText cityEditText;
    TextView weatherTextView;
    Button weatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        weatherTextView = findViewById(R.id.weatherTextView);
        weatherButton = findViewById(R.id.weatherButton);

        weatherButton.setOnClickListener(view -> {
            String cityName = cityEditText.getText().toString();
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY;

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String weather = "";
                        JSONObject mainObject = response.getJSONObject("main");
                        JSONArray arrayRequest = response.getJSONArray("weather");
                        for (int i=0; i<arrayRequest.length(); i++) {
                            JSONObject jsonObject = arrayRequest.getJSONObject(i);
                            weather += jsonObject.getString("description");
                        }
                        String temp = mainObject.getString("temp");
                        temp = Double.toString(Double.parseDouble(temp)-273.15);
                        weather += "\n   " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(temp)) + " degrees";
                        weatherTextView.setText(weather);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Request Error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        });
    }
}