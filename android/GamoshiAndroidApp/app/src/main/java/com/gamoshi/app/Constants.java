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


public class Constants {




    private Constants() {
    }

    static final String AD_TYPE_NAME = "adType";
    static final String AD_SERVER_NAME = "adServer";
    static final String AD_SIZE_NAME = "adSize";
    static final String AUTO_REFRESH_NAME = "autoRefresh";
    static final String SUPPLY_PARTNER_ID_NAME = "supplyPartnerId";
    static final String AD_UNIT_ID_NAME = "adUnitId";

    // Gamoshi - MoPub ad unit ids:

    static final String GAMOSHI_HOST_URL ="https://prebid.gamoshi.io/openrtb2/auction" ;

    //static final String GAMOSHI_ACCOUNT_ID = "gamoshi-1274";
    static final String GAMOSHI_ACCOUNT_ID_PREFIX = "gamoshi-";
    static final String GAMOSHI_PBS_CONFIG_ID_300x250 = "gamoshi-1274";
    static final String GAMOSHI_MOPUB_BANNER_ADUNIT_ID_300x250 = "db213b75a2524330900c05e17e79f815"; // 13b14bd907414ffaadc6fe0095ed4714
    static final String GAMOSHI_MOPUB_INTERSTITIAL_ADUNIT_ID = "712327ca56e44d759c6e6eeb89a0f951";

    // AdManager constants:
    static final String DFP_BANNER_ADUNIT_ID_ALL_SIZES = "/21794425054/hb_pb";
}
