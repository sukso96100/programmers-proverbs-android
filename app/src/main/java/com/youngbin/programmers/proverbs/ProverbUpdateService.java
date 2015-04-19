package com.youngbin.programmers.proverbs;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.squareup.okhttp.*;

import java.io.IOException;

public class ProverbUpdateService extends Service {
    private RxBus rxBus;
    Context mContext;
    ApplicationClass AC;
    public ProverbUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();

        SharedPreferences SP = getSharedPreferences("pref",MODE_PRIVATE);
        final SharedPreferences.Editor SPEDIT = SP.edit();

        mContext = ProverbUpdateService.this;
        AC = (ApplicationClass)getApplicationContext();
        rxBus = AC.rxBus;
        String URL = "http://proverbs-app.antjan.us/random";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
                Log.e("FAIL", "Failed to execute " + request, e);
                stopSelf();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
//                System.out.println(response.body().string());
                String proverb = response.body().string();
                rxBus.send(proverb);
                Log.d("RxBus", "Event Posted");
                SPEDIT.putString("proverb", proverb);
                SPEDIT.commit();
                Intent intent = new Intent(ProverbUpdateService.this, ProverbsWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                sendBroadcast(intent);
                stopSelf();
            }
        });



    }

}
