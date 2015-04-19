package com.youngbin.programmers.proverbs;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by youngbin on 15. 4. 13.
 */
public class ApplicationClass extends Application{
    RxBus rxBus = new RxBus();

    @Override
    public void onCreate() {
        super.onCreate();

        rxBus = new RxBus();
    }
}
