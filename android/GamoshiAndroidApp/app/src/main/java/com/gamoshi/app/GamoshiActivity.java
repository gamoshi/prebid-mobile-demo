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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import org.prebid.mobile.AdUnit;
import org.prebid.mobile.BannerAdUnit;
import org.prebid.mobile.Host;
import org.prebid.mobile.InterstitialAdUnit;
import org.prebid.mobile.PrebidMobile;
import org.prebid.mobile.ResultCode;
import org.prebid.mobile.VideoAdUnit;
import org.prebid.mobile.VideoInterstitialAdUnit;
import org.prebid.mobile.addendum.AdViewUtils;
import org.prebid.mobile.addendum.PbFindSizeError;

import static com.gamoshi.app.Constants.GAMOSHI_MOPUB_INTERSTITIAL_ADUNIT_ID;

public class GamoshiActivity extends AppCompatActivity {


    int refreshCount;

    AdUnit adUnit;
    MoPubInterstitial mpInterstitial;
    MoPubView adView;

    private PublisherAdView amBanner;

    ResultCode resultCode;
    String supplyPartnerId;
    String gamoshiSupplyPartnerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamoshi);

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
        this.setTitle(this.getTitle() + " ( " + adTypeName + " )");

        // Get ad types constants:
        String adTypeBanner = getString(R.string.adTypeBanner);

        //Get ad server constants:
        String adServerMoPub = getString(R.string.adServerMoPub);
        String adServerAdManager = getString(R.string.adServerAdManager);

        if (adServerMoPub.equals(adServerName)) {
            if (adTypeBanner.equals(adTypeName)) {

                String adUnitId = intent.getStringExtra(Constants.AD_UNIT_ID_NAME);

                String[] wAndH = adSizeName.split("x");
                int width = Integer.valueOf(wAndH[0]);
                int height = Integer.valueOf(wAndH[1]);
                setupAndLoadMPBanner(width, height, adUnitId);
            }
        } else if (adServerAdManager.equals(adServerName)) {
            if (adTypeBanner.equals(adTypeName)) {
                String adUnitId = intent.getStringExtra(Constants.AD_UNIT_ID_NAME);
                String[] wAndH = adSizeName.split("x");
                int width = Integer.valueOf(wAndH[0]);
                int height = Integer.valueOf(wAndH[1]);

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
        amBanner = new PublisherAdView(this);
        amBanner.setAdUnitId(adUnitId);
        amBanner.setAdSizes(new AdSize(width, height));

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
                        amBanner.setAdSizes(new AdSize(width, height));
                    }

                    @Override
                    public void failure(@NonNull PbFindSizeError error) {
                        Log.d("MyTag", "error: " + error);
                    }
                });
            }
        });

        final PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
        }
        final PublisherAdRequest request = builder.build();

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
        adUnit = new VideoAdUnit(gamoshiSupplyPartnerId, width, height, VideoAdUnit.PlacementType.IN_BANNER);
        adUnit.setAutoRefreshPeriodMillis(getIntent().getIntExtra(Constants.AUTO_REFRESH_NAME, 0));
        adUnit.fetchDemand(adView, resultCode -> {
            GamoshiActivity.this.resultCode = resultCode;
            adView.loadAd();
            refreshCount++;
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
}
