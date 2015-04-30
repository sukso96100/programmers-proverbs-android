package com.youngbin.programmers.proverbs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.squareup.okhttp.*;

import java.io.IOException;

public class ProverbUpdateService extends Service {
    private RxBus rxBus;
    Context mContext;
    ApplicationClass AC;
    boolean notify;
    public ProverbUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        notify = intent.getBooleanExtra("notify",false);
        return START_STICKY;
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
                Intent intent = new Intent(mContext, ProverbsWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                sendBroadcast(intent);

                if(notify){
                    Intent intent1 = new Intent(mContext, MainActivity.class);
                    PendingIntent PI = PendingIntent.getActivity(mContext,1,intent1,0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                            .setContentTitle(mContext.getResources().getString(R.string.app_name))
                            .setContentText(proverb)
                            .setContentIntent(PI)
                            .setSmallIcon(R.drawable.ic_proverb);
                    NotificationManager notificationManager =
                            (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
                stopSelf();
            }
        });



    }

}
