package com.example.thomas.augmento;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage
{
    Context context;
    SharedPreferences sharedPreferences;

    public LocalStorage(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("com.example.thomas.augmento", Context.MODE_PRIVATE);
    }
    public void addStorage(String key, String value)
    {
        sharedPreferences.edit().putString(key, value).apply();
    }
    public String getStorage(String key)
    {
        return sharedPreferences.getString(key,null);
    }

    public void clear()
    {
        sharedPreferences.edit().clear();
        sharedPreferences.edit().commit();
    }
}
