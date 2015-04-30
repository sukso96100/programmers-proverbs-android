package com.youngbin.programmers.proverbs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {
    private String Proverb;
    private ShareActionProvider mShareActionProvider;
    TextView TV;
    RxBus rxBus;
    ApplicationClass AC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AC = (ApplicationClass)getApplicationContext();
        rxBus = AC.rxBus;

        SharedPreferences SP = getSharedPreferences("pref", MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView CV = (CardView)findViewById(R.id.cardview);
        TV = (TextView)findViewById(R.id.textView);

        TV.setText(SP.getString("proverb","Small bug becomes a huge problem"));
        Proverb = SP.getString("proverb","Small bug becomes a huge problem");
        CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProverbUpdateService.class));
            }
        });

        rxBus.toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(final Object event) {
                        Log.d("RxBus", "Event Received");
                        if (event instanceof String) {
                            Log.d("RxBus", "Event Received");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TV.setText(event.toString());
                                    Proverb = event.toString();
                                    mShareActionProvider.setShareIntent(createShareIntent());
                                }
                            });

                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ShareActionProvider 를 가지고 있는 MenuItem 을 찾는다
        MenuItem item = menu.findItem(R.id.action_share);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        // ShareActionProvider 얻기
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareIntent());
        } else {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        //액션은 ACTION_SEND 로 합니다.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //Flag 를 설정해 줍니다. 공유하기 위해 공유에 사용할 다른 앱의 하나의 Activity 만 열고,
        //다시 돌아오면 열었던 Activity 는 꺼야 하기 때문에
        //FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET 로 해줍니다.
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        //공유할 것의 형태입니다. 우리는 텍스트를 공유합니다.
        shareIntent.setType("text/plain");
        String mealData;

        //보낼 데이터를 Extra 로 넣어줍니다.
        shareIntent.putExtra(Intent.EXTRA_TEXT,Proverb);
        Log.d("SHARE","Creating Share Intent:"+Proverb);
        return shareIntent;
    }
}
