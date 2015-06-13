package com.youngbin.programmers.proverbs.data;

import android.content.Context;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import java.util.ArrayList;

/**
 * Created by youngbin on 15. 6. 13.
 */
public class Favorites {
    String TAG = "Favorites";
    Realm realm;

    public Favorites(Context context){
        realm = Realm.getInstance(context);
    }

    public boolean isStarred(String proverb){
        Log.d(TAG, "Checking");
        Model Result = realm.where(Model.class).equalTo("proverb",proverb).findFirst();
        if(Result!=null){
            return true;
        }else{
            return false;
        }
    }

    public void addToOrRemoveFromFavorite(String proverb){
        if(isStarred(proverb)){
            Log.d(TAG, "Removing");
            realm.beginTransaction();
            Model Result = realm.where(Model.class).equalTo("proverb",proverb).findFirst();
            Result.removeFromRealm();
            realm.commitTransaction();
        }else{
            Log.d(TAG, "Adding");
            realm.beginTransaction();

            Model model = realm.createObject(Model.class);
            model.setProverb(proverb);

            Model realmModel = realm.copyToRealm(model);

            realm.commitTransaction();
        }
    }

    public ArrayList<String> getAllFavorites(){
        RealmResults<Model> RR = realm.where(Model.class).findAll();
        ArrayList<String> FavList = new ArrayList<String>();
        for(Model Fav : RR){
            Log.d(TAG,Fav.getProverb());
            FavList.add(Fav.getProverb());
        }
        return FavList;
    }

    public String getRandomFav(){
        RealmResults<Model> RR = realm.where(Model.class).findAll();
        if(RR.size()==0) {
            return null;
        }else{
            int random = (int) (Math.random() * RR.size());
            Log.d(TAG,"randomFav:"+random);
            String RandomFav = RR.get(random).getProverb();
            return RandomFav;
        }
    }
}
