package com.gamoshi.app;

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
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setCustomView(R.layout.custom_title_bar);
            bar.setDisplayOptions(bar.DISPLAY_SHOW_CUSTOM);
        }

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        TextView title = findViewById(R.id.adTitle);
        title.setText(getString(R.string.app_name));

        // Ad Type Spinner set up
        adTypeSpinnerSetUp();

        // Ad Server Spinner set up
        adServerSpinnerSetUp();

        // Ad Size Spinner
        adSizeSpinnerSetUp();
    }

    private void adSizeSpinnerSetUp() {
        Spinner adSizeSpinner = findViewById(R.id.adSizeSpinner);
        ArrayAdapter<CharSequence> adSizeAdapter =
                ArrayAdapter.createFromResource(
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

    private void adServerSpinnerSetUp() {
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
    }

    private void adTypeSpinnerSetUp() {
        Spinner adTypeSpinner = findViewById(R.id.adTypeSpinner);

        ArrayAdapter<CharSequence> adTypeAdapter =
                ArrayAdapter
                        .createFromResource(this, R.array.adTypeArray,
                                android.R.layout.simple_spinner_item);

        adTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adTypeSpinner.setAdapter(adTypeAdapter);

        final LinearLayout adSizeRow = findViewById(R.id.adSizeRow);
        final EditText supplyPartnerIdText = findViewById(R.id.supplyPartnerIdInput);

        adTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            final List<String> adTypes = Arrays.asList(getResources().getStringArray(R.array.adTypeArray));

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                adSizeRow.setVisibility(View.INVISIBLE);

                if (pos > adTypes.size()) {
                    return;
                }
                adType = adTypes.get(pos);
                String adTypeBanner = getString(R.string.adTypeBanner);
                String adTypeVideo = getString(R.string.adTypeVideo);

                if (adType.equals(adTypeBanner)) {

                    // show size and refresh millis
                    adSizeRow.setVisibility(View.VISIBLE);
                    supplyPartnerIdText.setText(Constants.GAMOSHI_BANNER_ACCOUNT_ID);

                } else if (adType.equals(adTypeVideo)) {

                    // adRefreshRow.setVisibility(View.VISIBLE);
                    adSizeRow.setVisibility(View.VISIBLE);
                    supplyPartnerIdText.setText(Constants.GAMOSHI_VIDEO_ACCOUNT_ID);
                }
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

        String adTypeBanner = getString(R.string.adTypeBanner);
        String adTypeVideo = getString(R.string.adTypeVideo);
        if (adType.equals(adTypeBanner) || adType.equals(adTypeVideo) ) {
            gamoshiActivityIntent.putExtra(Constants.AD_SIZE_NAME, adSize);
        }

        // set auto refresh interval (in milli seconds)
        gamoshiActivityIntent.putExtra(Constants.AUTO_REFRESH_NAME, 0);

        EditText supplyPartnerIdText = findViewById(R.id.supplyPartnerIdInput);
        String supplyPartnerIdString = supplyPartnerIdText.getText().toString();
        if (!TextUtils.isEmpty(supplyPartnerIdString)) {
            gamoshiActivityIntent.putExtra(Constants.SUPPLY_PARTNER_ID_NAME, supplyPartnerIdString);
        }

        String adServerMoPub = getString(R.string.adServerMoPub);
        String adServerAdManager = getString(R.string.adServerAdManager);
        String adUnitIdString = null;
        if (adServer.equals(adServerMoPub)) {
            if(adType.equals(adTypeBanner)){
            adUnitIdString = Constants.GAMOSHI_MOPUB_BANNER_ADUNIT_ID_300x250;
            } else if(adType.equals(adTypeVideo)){
                    adUnitIdString = Constants.GAMOSHI_MOPUB_VIDEO_ADUNIT_ID_300x250;
            }
        } else if (adServer.equals(adServerAdManager)) {
            adUnitIdString = Constants.GAMOSHI_DFP_BANNER_ADUNIT_ID_ALL_SIZES;
        }

        if (!TextUtils.isEmpty(adUnitIdString)) {
            gamoshiActivityIntent.putExtra(Constants.AD_UNIT_ID_NAME, adUnitIdString);
        }

        startActivity(gamoshiActivityIntent);
    }
}
