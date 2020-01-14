# Prebid Mobile Demo Apps

This repository contains demo mobile apps for running mobile ads using prebid sdk

---

## Official Prebid Instructions

Before running this demo applications, please read carefully the official instructions of Prebid for how to integrate Prebid SDK in Android or iOS.

- [Instructions for Android](http://prebid.org/prebid-mobile/pbm-api/android/code-integration-android.html)

- [Instructions for iOS](http://prebid.org/prebid-mobile/pbm-api/ios/code-integration-ios.html)

## Running Gamoshi Demo Apps

### Android App

As of now, the only way to run Gamoshi's demo application is using Android Studio IDE.
In order to run the application on Android Studio please follow these steps:

1. Clone our repository using git cli:
   `git clone https://github.com/gamoshi/prebid-mobile-demo.git`
2. Open Android Studio IDE
3. In Android Studio, go to File -> Open... (a folder browser dialog should open)
4. In the folder browser dialog, search for the directory in which you cloned our repository
5. Select the folder `GamoshiAndroidApp` and press the `Open` button of the dialog
6. Wait for the Android Studio to load all dependencies, and once it is done you are free to run the application.

### iOS App

TBD

## Testing Ads

### Banner

Our application shipped with a default **supply partner id**: `1274` for testing **300x250** banner ads.

In order to test your own banner creatives in our system, please insert your supply id in the `Supply Id` field after running the application.
