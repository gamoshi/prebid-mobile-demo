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


class Constants {

    private Constants() {
    }

    static final String AD_TYPE_NAME = "adType";
    static final String AD_SERVER_NAME = "adServer";
    static final String AD_SIZE_NAME = "adSize";
    static final String AUTO_REFRESH_NAME = "autoRefresh";
    static final String SUPPLY_PARTNER_ID_NAME = "supplyPartnerId";
    static final String AD_UNIT_ID_NAME = "adUnitId";

    static final String GAMOSHI_HOST_URL ="https://rtb.gamoshi.io/pbs/v1/267/auction" ;
    static final String GAMOSHI_ACCOUNT_ID_PREFIX = "";
    static final String GAMOSHI_BANNER_ACCOUNT_ID = "6625";
    static final String GAMOSHI_VIDEO_ACCOUNT_ID = "6625";


    // MoPub ad unit id:
    static final String GAMOSHI_MOPUB_BANNER_ADUNIT_ID_300x250 = "db213b75a2524330900c05e17e79f815";
    static final String GAMOSHI_MOPUB_BANNER_ADUNIT_ID_320x50 = "16c799c4bce345d8af45538b1c856d39";
    static final String GAMOSHI_MOPUB_BANNER_ADUNIT_ID_728x90 = "8ca53eb71b964c18b13cbaabebf16c92";

    static final String GAMOSHI_MOPUB_VIDEO_ADUNIT_ID_300x250 = "562a20cf83b24922a8fb91515313b85a";
    static final String GAMOSHI_MOPUB_VIDEO_DYNAMIC_ADUNIT_ID = "c0c99992ffac4712b5013d65b30076d2";


    

    // DFP ad unit id:
    static final String GAMOSHI_DFP_BANNER_ADUNIT_ID_ALL_SIZES = "/21794425054/hb_pb";
}
