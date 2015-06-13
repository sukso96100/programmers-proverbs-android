package com.youngbin.programmers.proverbs;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.youngbin.programmers.proverbs.data.Favorites;

import java.util.ArrayList;


public class FavoritesActivity extends AppCompatActivity {
    RxBus rxBus;
    ApplicationClass AC;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mContext = FavoritesActivity.this;

        SharedPreferences SP = getSharedPreferences("pref",MODE_PRIVATE);
        final SharedPreferences.Editor SPEDIT = SP.edit();

        AC = (ApplicationClass)getApplicationContext();
        rxBus = AC.rxBus;

        Favorites Fav = new Favorites(mContext);
        ArrayList<String> FavList = Fav.getAllFavorites();

        ListView LV = (ListView)findViewById(R.id.listview);
        final ArrayAdapter<String> AA = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1,FavList);
        LV.setAdapter(AA);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String proverb = AA.getItem(position);
                rxBus.send(proverb);
                Log.d("RxBus", "Event Posted");
                SPEDIT.putString("proverb", proverb);
                SPEDIT.commit();
                Intent intent = new Intent(mContext, ProverbsWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                sendBroadcast(intent);
                finish();
            }
        });
    }



}
