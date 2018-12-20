package com.popa.popa.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.popa.popa.model.Pet;

import org.json.JSONObject;

public class PetService {

    private SharedPreferences sp;
    private SharedPreferences tempSp;
    public Context activity;
    private SharedPreferences.Editor editor;

    public PetService(Context activity){
        this.activity = activity;
        sp = activity.getApplicationContext().getSharedPreferences("Pet", 0);
        tempSp = activity.getApplicationContext().getSharedPreferences("temp", 0);
    }

    /**
     * Loads a pet into the SharedPreferences of the Android device with all its stats.
     *
     * @param pet - Pet Object with all its stats
     */
    public void storePetInSharedPreferences(Pet pet){

        editor = sp.edit();
        editor.putString("name", pet.getName());
        editor.putInt("str", pet.getStr());
        editor.putInt("dex", pet.getDex());
        editor.putInt("agi", pet.getAgi());
        editor.putInt("intel", pet.getIntel());
        editor.putString("primaryAttribute", pet.getPrimaryAttribute());

        editor.commit();

    }

    /**
     * Returns the pet which is stored in the SharedPreferences of the Android Device.
     *
     * @return Pet Object - the stored Pet with its stats
     */
    public Pet getPetFromSharedPreferences(){

        editor = sp.edit();

        if(sp.contains("name")){
            return new Pet(sp.getString("name", "unknown"),sp.getInt("str", 0),sp.getInt("dex",0 ),sp.getInt("agi",0),sp.getInt("intel",0 ),sp.getString("primaryAttribute","unknown"));
        }

        return new Pet("Gunter", 10, 10, 10, 10, "dex");
    }

    public void storeRemoteStatsTemporarily(int remoteTotal){
        editor = tempSp.edit();
        editor.putInt("remoteTotal", remoteTotal);
        editor.commit();
    }

    public int getRemoteStatsTemporarily(){
        editor = tempSp.edit();
        if(tempSp.contains("remoteTotal")){
            return tempSp.getInt("remoteTotal", 0);
        }
        return 0;
    }


}
