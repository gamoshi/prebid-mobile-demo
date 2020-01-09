package com.gamoshi.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Default values
    private String adType = "";
    private String adServer = "";
    private String adSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Get all the components
        Spinner adTypeSpinner = findViewById(R.id.adTypeSpinner);

        // Ad Type Spinner set up
        ArrayAdapter<CharSequence> adTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.adTypeArray,
                android.R.layout.simple_spinner_item);
        adTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adTypeSpinner.setAdapter(adTypeAdapter);

        final LinearLayout adSizeRow = findViewById(R.id.adSizeRow);
        //final LinearLayout adRefreshRow = findViewById(R.id.autoRefreshRow);
        adTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            List<String> adTypes = Arrays.asList(getResources().getStringArray(R.array.adTypeArray));

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                adSizeRow.setVisibility(View.INVISIBLE);
                //adRefreshRow.setVisibility(View.INVISIBLE);

                if (pos > adTypes.size()) {
                    return;
                }
                adType = adTypes.get(pos);
                if (adType.equals("Banner")) {
                    // show size and refresh millis

                    adSizeRow.setVisibility(View.VISIBLE);
                    //adRefreshRow.setVisibility(View.VISIBLE);
                } else if (adType.equals("Interstitial")) {
                    //adRefreshRow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Ad Server Spinner (i.e. drop-down)
        Spinner adServerSpinner = findViewById(R.id.adServerSpinner);
        ArrayAdapter<CharSequence> adServerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.adServerArray,
                        android.R.layout.simple_spinner_item);
        adServerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adServerSpinner.setAdapter(adServerAdapter);
        adServerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            List<String> adServers = Arrays.asList(getResources().getStringArray(R.array.adServerArray));

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos > adServers.size()) {
                    return;
                }
                adServer = adServers.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Ad Size Spinner
        Spinner adSizeSpinner = findViewById(R.id.adSizeSpinner);
        ArrayAdapter<CharSequence> adSizeAdapter = ArrayAdapter.createFromResource(
                this, R.array.adSizeArray,
                android.R.layout.simple_spinner_item);
        adSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adSizeSpinner.setAdapter(adSizeAdapter);
        adSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            List<String> adSizes = Arrays.asList(getResources().getStringArray(R.array.adSizeArray));

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos > adSizes.size()) {
                    return;
                }
                adSize = adSizes.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void showAd(View view) {
        Intent gamoshiActivityIntent = new Intent(this, GamoshiActivity.class);
        gamoshiActivityIntent.putExtra(Constants.AD_SERVER_NAME, adServer);
        gamoshiActivityIntent.putExtra(Constants.AD_TYPE_NAME, adType);

        if ("Banner".equals(adType)) {
            gamoshiActivityIntent.putExtra(Constants.AD_SIZE_NAME, adSize);
        }

        // set auto refresh interval (in milli seconds)
        gamoshiActivityIntent.putExtra(Constants.AUTO_REFRESH_NAME, 0);

        EditText supplyPartnerIdText = findViewById(R.id.supplyPartnerIdInput);
        String supplyPartnerIdString = supplyPartnerIdText.getText().toString();
        if (!TextUtils.isEmpty(supplyPartnerIdString)) {
            gamoshiActivityIntent.putExtra(Constants.SUPPLY_PARTNER_ID_NAME, supplyPartnerIdString);
        }

        EditText adUnitIdText = findViewById(R.id.adUnitIdInput);
        String adUnitIdString = adUnitIdText.getText().toString();
        if (!TextUtils.isEmpty(adUnitIdString)) {
            gamoshiActivityIntent.putExtra(Constants.AD_UNIT_ID_NAME, adUnitIdString);
        }

        startActivity(gamoshiActivityIntent);
    }
}
