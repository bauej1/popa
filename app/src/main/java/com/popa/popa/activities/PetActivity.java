package com.popa.popa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.popa.popa.R;
import com.popa.popa.model.Pet;
import com.popa.popa.services.MyFirebaseMessagingService;
import com.popa.popa.services.PetService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetActivity extends AppCompatActivity implements
        View.OnClickListener  {

    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private MyFirebaseMessagingService messagingService;

    String datapath = "/message_path";
    Button mybutton;
   // TextView logger;
    protected Handler handler;
    String TAG = "Mobile MainActivity";
    int num = 1;

    //Stat Controls
    private TextView tStrPetValue;
    private TextView tDexPetValue;
    private TextView tAgiPetValue;
    private TextView tIntPetValue;
    private TextView tPetName;
    private TextView tPetlvl;
    private String primaryAttribute;
    private Pet pet;
    private ImageButton nextPet;
    private ImageButton previousPet;
    private ArrayList<String> petList;
    private int counter;
    private ImageView petView;
    private ProgressBar experience;
    private ProgressBar challenge1;
    private ProgressBar challenge2;

    SharedPreferences sp;
    SharedPreferences spStepper;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        counter = 0;
        petList = new ArrayList<>();
        petList.add("Gunter1");
        petList.add("Grimbal");
        petList.add("Bjorn");
        petList.add("Kuzco");


        //get Buttons to change Pet design
        nextPet = findViewById(R.id.nextPet);
        previousPet = findViewById(R.id.previousPet);
        petView = findViewById(R.id.petView);
        //    petView.setImageResource(R.drawable.gunter1);

        //Progressbar connect
        experience = findViewById(R.id.progressBar_Petlvl);
        challenge1 = findViewById(R.id.progressBar_challenge1);
        challenge2 = findViewById(R.id.progressBar_challenge2);



        //sets the klicklistener for the PetViewChange
        nextPet.setOnClickListener(v->{
            petForward();
        });
        previousPet.setOnClickListener(v->{
            petPrevious();
        });

        //get the widgets
        mybutton = findViewById(R.id.button_startBattle);
        mybutton.setOnClickListener(this);
      //  logger = findViewById(R.id.text_petName);

        //Stat Controls initializing
        tStrPetValue = findViewById(R.id.text_PetStr);
        tDexPetValue = findViewById(R.id.text_PetDex);
        tAgiPetValue = findViewById(R.id.text_PetAgi);
        tIntPetValue = findViewById(R.id.text_PetInt);
        tPetName = findViewById(R.id.text_petName);
        tPetlvl = findViewById(R.id.text_petLevel);

        //add the shared preference
        sp = this.getApplicationContext().getSharedPreferences("pet", 0);
        spStepper = this.getApplicationContext().getSharedPreferences("heartRate", 0);
        //loads the petData
        loadData();

    /**    //message handler for the send thread.
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                logthis(stuff.getString("logthis"));
                return true;
            }
        });

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
**/

        tIntPetValue.setText("40");

        messagingService = new MyFirebaseMessagingService(this);
        getPetFromSharedPreferences();

    }

    @Override
    protected void onResume() {
        super.onResume();
        counter = Integer.valueOf(sp.getString("petID","0"));
        counter--;
        petForward();

        //collects the steps for challenge 1
        String steps = spStepper.getString("step", "0");
        int step = Integer.valueOf(steps);
        int stepDone = Integer.valueOf(sp.getString("stepDone","0"));
        int progress = (((step-stepDone)*100)/6000);

        //Challenge1 Step will be done to reset the quest...Could have better Solution
        challenge1.setProgress(progress);
        if(progress >= 100) {
            levelUp();
            editor.putString("stepDone", "" + 6000);
            editor.commit();
        }

        //collects the diaryInputs for challenge 2
        int diaryInputs = Integer.valueOf(sp.getString("diaryInput","0"));
        int diaryProgress = (diaryInputs*100)/ 1;
        challenge2.setProgress(diaryProgress);
        Log.d("haha",""+diaryProgress);
        if(diaryProgress>= 100){
            levelUp();
            editor.putString("diaryInput","0");
            editor.commit();
        }
    }

    public void levelUp(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>Level Up!</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>Congratulations! <br> Your pet leveled up and got stronger!</font>"));

        // add the buttons
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.appBackground);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.tileBackgroundDark));

        int stats = Integer.valueOf(tStrPetValue.getText().toString());
        stats = stats + 2;
        tStrPetValue.setText("300");
        editor.putString("str", ""+stats);
        editor.putString("int", ""+stats);
        editor.putString("dex", ""+stats);
        editor.putString("agi", ""+stats);
        int lvl = Integer.valueOf(tPetlvl.getText().toString());
        lvl = lvl+1;
        editor.putString("lvl", ""+lvl);
        editor.commit();
        loadData();
    }

    public void loadData(){
        if(!sp.contains("str")){
            editor = sp.edit();
            editor.putString("str", "10");
            editor.putString("int", "10");
            editor.putString("dex", "10");
            editor.putString("agi", "10");
            editor.putString("petName", "Gunter");
            editor.putString("petID", "0");
            editor.putString("lvl", "1");
            editor.putInt("challenge1", 0);
            editor.putInt("challenge2", 0);
            editor.putInt("progressLvl", 0);

            tStrPetValue.setText(sp.getString("str", "0"));
            tDexPetValue.setText(sp.getString("dex", "0"));
            tAgiPetValue.setText(sp.getString("agi", "0"));
            tIntPetValue.setText(sp.getString("int", "0"));
            tPetName.setText(sp.getString("petName","Error"));
            petView.setImageResource(R.drawable.gunter1);
            tPetlvl.setText(sp.getString("lvl","0"));
            challenge2.setProgress(sp.getInt("challenge2",0));
            challenge1.setProgress(sp.getInt("challenge1",0));
            experience.setProgress(sp.getInt("progressLvl",0));
            editor.commit();



        }else{
            tStrPetValue.setText(sp.getString("str", "0"));
            tDexPetValue.setText(sp.getString("dex", "0"));
            tAgiPetValue.setText(sp.getString("agi", "0"));
            tIntPetValue.setText(sp.getString("int", "0"));
            tPetName.setText(sp.getString("petName","Error"));
            counter = Integer.valueOf(sp.getString("petID","0"));
            counter--;
            petForward();
            tPetlvl.setText(sp.getString("lvl","0"));
            challenge2.setProgress(sp.getInt("challenge2",0));
            challenge1.setProgress(sp.getInt("challenge1",0));
            experience.setProgress(sp.getInt("progressLvl",0));
        }
    }

    //Method to Change Pet View forward
    public void petForward(){
        editor = sp.edit();
        if(counter == 3){
            counter = 0;
        }else{
            counter++;
        }
        switch(counter){
            case 0:
                petView.setImageResource(R.drawable.gunter1);
                tPetName.setText("Gunter");
                editor.putString("petName", "Gunter");
                editor.putString("petID", "0");
                editor.commit();
                break;

            case 1:
                petView.setImageResource(R.drawable.grimbal);
                tPetName.setText("Grimbal");
                editor.putString("petName", "Grimbal");
                editor.putString("petID", "1");
                editor.commit();
                break;

            case 2:
                petView.setImageResource(R.drawable.bjorn);
                tPetName.setText("Björn");
                editor.putString("petName", "Björn");
                editor.putString("petID", "2");
                editor.commit();
                break;

            case 3:
                petView.setImageResource(R.drawable.kuzco);
                tPetName.setText("Kuzco");
                editor.putString("petName", "Kuzco");
                editor.putString("petID", "3");
                editor.commit();
                break;
        }
        editor.commit();

    }
    //Method to Change Pet View previous
    public void petPrevious(){
        editor = sp.edit();
        if(counter == 0){
            counter = 3;
        }else{
            counter --;
        }
        switch(counter){
            case 0:
                petView.setImageResource(R.drawable.gunter1);
                tPetName.setText("Gunter");
                editor.putString("petName", "Gunter");
                editor.putString("petID", "0");
                editor.commit();
                break;

            case 1:
                petView.setImageResource(R.drawable.grimbal);
                tPetName.setText("Grimbal");
                editor.putString("petName", "Grimbal");
                editor.putString("petID", "1");
                editor.commit();
                break;

            case 2:
                petView.setImageResource(R.drawable.bjorn);
                tPetName.setText("Björn");
                editor.putString("petName", "Björn");
                editor.putString("petID", "2");
                editor.commit();
                break;

            case 3:
                petView.setImageResource(R.drawable.kuzco);
                tPetName.setText("Kuzco");
                editor.putString("petName", "Kuzco");
                editor.putString("petID", "3");
                editor.commit();
                break;
        }
        editor.commit();
    }

    //button listener
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_startBattle:
                messagingService.sendNotification(URL, messagingService.getSendingData(pet, false), getApplicationContext());
                break;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("battleground");
        System.out.println("BattleMode: unsubscribed from battleground firebase topic");

        getPetStats();
        messagingService.storePetInSharedPreferences(pet);
    }


    private void getPetFromSharedPreferences(){
        pet = messagingService.getPetFromSharedPreferences();
        setPetStats();
    }

    private void setPetStats(){
        tPetName.setText(pet.getName());
        tStrPetValue.setText(pet.getStr() + "");
        tDexPetValue.setText(pet.getDex() + "");
        tAgiPetValue.setText(pet.getAgi() + "");
        tIntPetValue.setText(pet.getIntel() + "");
    }

    private void getPetStats(){
        pet.setStr(Integer.parseInt(tStrPetValue.getText() +""));
        pet.setDex(Integer.parseInt(tDexPetValue.getText() +""));
        pet.setAgi(Integer.parseInt(tAgiPetValue.getText() +""));
        pet.setIntel(Integer.parseInt(tIntPetValue.getText() +""));
    }
}
