package com.youngbin.programmers.proverbs.data;

import io.realm.RealmObject;

/**
 * Created by youngbin on 15. 6. 13.
 */
public class Model extends RealmObject{
    private String proverb;

    public String getProverb() {
        return proverb;
    }

    public void setProverb(String proverb) {
        this.proverb = proverb;
    }
}
