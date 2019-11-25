package jmessage.example.com.weather;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 *添加用户APIkey
 */
public class Apikey extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*735b15ed8d2d1e8207e2f0593ec6e4b9*/
        ApiStoreSDK.init(this,"939ff8d443e42d3b495ffe266b863407");
    }


}
