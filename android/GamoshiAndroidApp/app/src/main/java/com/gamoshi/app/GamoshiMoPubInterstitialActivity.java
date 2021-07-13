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

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class GamoshiMoPubInterstitialActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener {

    MoPubInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInterstitial = new MoPubInterstitial(this, Constants.GAMOSHI_MOPUB_VIDEO_ADUNIT_ID_300x250);
        mInterstitial.setInterstitialAdListener(this);
        mInterstitial.load();
    }

    @Override
    protected void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
    }

    // Defined by your application, indicating that you're ready to show an interstitial ad.
    void showInterstitial() {
        if (mInterstitial.isReady()) {
            mInterstitial.show();
        } else {
            // Caching is likely already in progress if `isReady()` is false.
            // Avoid calling `load()` here and instead rely on the callbacks as suggested below.
        }
    }

    // InterstitialAdListener methods
    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        // The interstitial has been cached and is ready to be shown.
        this.showInterstitial();
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        // The interstitial has failed to load. Inspect errorCode for additional information.
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog
                    .Builder(
                            GamoshiMoPubInterstitialActivity.this,
                    android.R.style.Theme_Material_Dialog_Alert
            );
        } else {
            builder = new AlertDialog.Builder(GamoshiMoPubInterstitialActivity.this);
        }
        builder.setTitle("Failed to load MoPub interstitial ad")
                .setMessage("Error code: " + errorCode.toString())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        // The interstitial has been shown. Pause / save state accordingly.
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {}

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        // The interstitial has being dismissed. Resume / load state accordingly.
    }
}
