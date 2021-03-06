/*
 *    Copyright 2018-2019 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gamoshi.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import org.prebid.mobile.AdUnit;
import org.prebid.mobile.BannerAdUnit;
import org.prebid.mobile.Host;
import org.prebid.mobile.InterstitialAdUnit;
import org.prebid.mobile.OnCompleteListener2;
import org.prebid.mobile.PrebidMobile;
import org.prebid.mobile.ResultCode;
import org.prebid.mobile.VideoAdUnit;
import org.prebid.mobile.addendum.AdViewUtils;
import org.prebid.mobile.addendum.PbFindSizeError;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GamoshiActivity extends AppCompatActivity {


    int refreshCount;

    AdUnit adUnit;
    MoPubInterstitial mpInterstitial;
    MoPubView adView;

    private AdView amBanner;

    ResultCode resultCode;
    String supplyPartnerId;
    String gamoshiSupplyPartnerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamoshi);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setCustomView(R.layout.custom_title_bar);
            bar.setDisplayOptions(bar.DISPLAY_SHOW_CUSTOM);
        }


        // Get data from main-activity:
        Intent intent = getIntent();

        // Build supply partner id
        supplyPartnerId = intent.getStringExtra(Constants.SUPPLY_PARTNER_ID_NAME);
        gamoshiSupplyPartnerId = Constants.GAMOSHI_ACCOUNT_ID_PREFIX + supplyPartnerId;

        // Set globally required prebid parameters :
        Host.CUSTOM.setHostUrl(Constants.GAMOSHI_HOST_URL);
        PrebidMobile.setPrebidServerHost(Host.CUSTOM);
        PrebidMobile.setPrebidServerAccountId(gamoshiSupplyPartnerId);
        PrebidMobile.setStoredAuctionResponse("");

        // Get ad type, ad server and ad size data from the main activity:
        String adTypeName = intent.getStringExtra(Constants.AD_TYPE_NAME);
        String adServerName = intent.getStringExtra(Constants.AD_SERVER_NAME);
        String adSizeName = intent.getStringExtra(Constants.AD_SIZE_NAME);

        String subTitle = getString(R.string.app_name);
        subTitle = subTitle + " ( " + adTypeName + " )";

        TextView title = findViewById(R.id.adTitle);
        title.setText(subTitle);

        // Get ad types constants:
        String adTypeBanner = getString(R.string.adTypeBanner);
        String adTypeVideo = getString(R.string.adTypeVideo);

        //Get ad server constants:
        String adServerMoPub = getString(R.string.adServerMoPub);
        String adServerAdManager = getString(R.string.adServerAdManager);

        if (adServerMoPub.equals(adServerName)) {
            String adUnitId = intent.getStringExtra(adSizeName);

            assert adSizeName != null;
            String[] wAndH = adSizeName.split("x");
            int width = Integer.parseInt(wAndH[0]);
            int height = Integer.parseInt(wAndH[1]);
            if (adTypeBanner.equals(adTypeName)) {
                setupAndLoadMPBanner(width, height, adUnitId);
            }
            else if(adTypeVideo.equals(adTypeName)){
                setupAndLoadMPVideo(width, height, adUnitId);
            }
        } else if (adServerAdManager.equals(adServerName)) {
            if (adTypeBanner.equals(adTypeName)) {
                String adUnitId = intent.getStringExtra(Constants.AD_UNIT_ID_NAME);
                assert adSizeName != null;
                String[] wAndH = adSizeName.split("x");
                int width = Integer.parseInt(wAndH[0]);
                int height = Integer.parseInt(wAndH[1]);

                // setupAMBanner(width, height, Constants.DFP_BANNER_ADUNIT_ID_ALL_SIZES);
                setupAMBanner(width, height, adUnitId);

            }
        }
    }



    //*** Banner Configurations: ***

    // 1. MoPub configurations
    void setupAndLoadMPBanner(int width, int height, String adUnitId) {

        FrameLayout adFrame = findViewById(R.id.adFrame);
        adFrame.removeAllViews();

        adView = new MoPubView(this);
        adView.setAdUnitId(adUnitId);

        adView.setMinimumWidth(width);
        adView.setMinimumHeight(height);
        adFrame.addView(adView);

        adUnit = new BannerAdUnit(gamoshiSupplyPartnerId, width, height);
        adUnit.setAutoRefreshPeriodMillis(getIntent().getIntExtra(Constants.AUTO_REFRESH_NAME, 0));
        adUnit.fetchDemand(adView, resultCode -> {
            GamoshiActivity.this.resultCode = resultCode;
            adView.loadAd();
            refreshCount++;
        });
    }

    // 2. Ad Manager configurations
    private void setupAMBanner(int width, int height, String adUnitId) {

        adUnit = new BannerAdUnit(gamoshiSupplyPartnerId, width, height);
        amBanner = new AdView(this);
        amBanner.setAdUnitId(adUnitId);
        amBanner.setAdSize(new AdSize(width, height));

        loadAMBanner();
    }

    private void loadAMBanner() {
        FrameLayout adFrame = findViewById(R.id.adFrame);
        adFrame.removeAllViews();
        adFrame.addView(amBanner);

        amBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                AdViewUtils.findPrebidCreativeSize(amBanner, new AdViewUtils.PbFindSizeListener() {
                    @Override
                    public void success(int width, int height) {
                        amBanner.setAdSize(new AdSize(width, height));
                    }

                    @Override
                    public void failure(@NonNull PbFindSizeError error) {
                        Log.d("MyTag", "error: " + error);
                    }
                });
            }
        });

        final AdRequest.Builder builder = new AdRequest.Builder();
        /*if (BuildConfig.DEBUG) {
            //builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
        }*/
        final AdRequest request = builder.build();

        //region PrebidMobile Mobile API 1.0 usage
        int millis = getIntent().getIntExtra(Constants.AUTO_REFRESH_NAME, 0);
        adUnit.setAutoRefreshPeriodMillis(millis);
        adUnit.fetchDemand(request, resultCode -> {
            GamoshiActivity.this.resultCode = resultCode;
            amBanner.loadAd(request);
            refreshCount++;
        });
    }

    //*** End Of Banner Configurations: ***


    //*** Video Configurations (for future release): ***

    // 1. MoPub Configurations:
    void setupAndLoadMPVideo(int width, int height, String adUnitId) {
        FrameLayout adFrame = findViewById(R.id.adFrame);
        adFrame.removeAllViews();
        adView = new MoPubView(this);
        adView.setAdUnitId(adUnitId);

        adView.setMinimumWidth(width);
        adView.setMinimumHeight(height);
        adFrame.addView(adView);

        //adUnit = new VideoAdUnit(gamoshiSupplyPartnerId, width, height, VideoAdUnit.PlacementType.IN_FEED);
        adUnit = new VideoAdUnit(gamoshiSupplyPartnerId, width, height);

        adUnit.setAutoRefreshPeriodMillis(getIntent().getIntExtra(Constants.AUTO_REFRESH_NAME, 0));
//        adUnit.fetchDemand(adView, resultCode -> {
//            GamoshiActivity.this.resultCode = resultCode;
//            adView.loadAd();
//            refreshCount++;
//        });

        adUnit.fetchDemand(new GamoshiOnComplete(adView,this));

    }

    void setupAndLoadMPInterstitialVideo(int width, int height, String adUnitId) {
        FrameLayout adFrame = findViewById(R.id.adFrame);
        adFrame.removeAllViews();
        adView = new MoPubView(this);
        adView.setAdUnitId(adUnitId);

        adView.setMinimumWidth(width);
        adView.setMinimumHeight(height);
        adFrame.addView(adView);

        adUnit = new InterstitialAdUnit(Constants.GAMOSHI_MOPUB_VIDEO_ADUNIT_ID_300x250);

        final MoPubInterstitial mpInterstitial = new MoPubInterstitial(this, Constants.GAMOSHI_MOPUB_VIDEO_ADUNIT_ID_300x250);
        mpInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                interstitial.show();
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(GamoshiActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(GamoshiActivity.this);
                }
                builder.setTitle("Failed to load MoPub interstitial ad")
                        .setMessage("Error code: " + errorCode.toString())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {

            }
        });

        int millis = getIntent().getIntExtra(Constants.AUTO_REFRESH_NAME, 0);
        adUnit.setAutoRefreshPeriodMillis(millis);

        adUnit.fetchDemand(mpInterstitial, resultCode -> {

            System.out.println(resultCode);
            mpInterstitial.load();


            refreshCount++;
            GamoshiActivity.this.resultCode = resultCode;
        });


    }

    //*** End of Video Configurations: ***


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (mpInterstitial != null) {
            mpInterstitial.destroy();
        }

        super.onDestroy();
    }


    public static class GamoshiOnComplete implements OnCompleteListener2 {

        private final MoPubView adView;
        private final GamoshiActivity activity;

        public GamoshiOnComplete(MoPubView adView,GamoshiActivity activity) {
            this.adView = adView;
            this.activity=activity;
        }

        @Override
        public void onComplete(ResultCode resultCode, Map<String, String> unmodifiableMap) {


            Set<AdSize> sizes = new HashSet<>();
            sizes.add(new AdSize(640, 480));
        //    String uri = Util.generateInstreamUriForGam(Constants.DFP_VAST_ADUNIT_ID_RUBICON, sizes, unmodifiableMap);
            //adsLoader = new ImaAdsLoader(RubiconInstreamVideoIMADemoActivity.this, Uri.parse(uri));
           // initializePlayer();

            activity.resultCode = resultCode;
            adView.loadAd();
            activity.refreshCount++;
        }
    }
}
