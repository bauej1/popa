package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.popa.popa.R;
import com.popa.popa.model.BodyPain;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PainLocationActivity extends AppCompatActivity {

    ToggleButton shoulderButton;
    ToggleButton lowerBackButton;
    ToggleButton sacrumButton;
    ToggleButton otherAreaButton;
    ToggleButton noPainButton;
    ImageView elementBody;
    ArrayList<BodyPain> bodyImageArray;
    ArrayList<BodyPain> filteredList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_painlocation);

        shoulderButton = (ToggleButton) findViewById(R.id.b_shoulder);
        lowerBackButton = (ToggleButton) findViewById(R.id.b_lowerBack);
        sacrumButton = (ToggleButton) findViewById(R.id.b_sacrum);
        otherAreaButton = (ToggleButton) findViewById(R.id.b_other);
        noPainButton = (ToggleButton) findViewById(R.id.b_noPain);
        elementBody = (ImageView) findViewById(R.id.elementBody);

        initializeBodyImages();

        shoulderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getButtonValue(); }
        });

        lowerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonValue();
            }
        });

        sacrumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonValue();
            }
        });

        otherAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonValue();
            }
        });

        noPainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (noPainButton.isChecked()) {
                    shoulderButton.setChecked(false);
                    lowerBackButton.setChecked(false);
                    sacrumButton.setChecked(false);
                    otherAreaButton.setChecked(false);
                }
                getButtonValue();
            }
        });
    }

    public void initializeBodyImages(){
        bodyImageArray = new ArrayList<>();

        BodyPain a = new BodyPain("a", true, false, false, false);
        bodyImageArray.add(a);

        BodyPain ab = new BodyPain("ab", true, true, false, false);
        bodyImageArray.add(ab);

        BodyPain abc = new BodyPain("abc", true, true, true, false);
        bodyImageArray.add(abc);

        BodyPain abcd = new BodyPain("abcd", true, true, true, true);
        bodyImageArray.add(abcd);

        BodyPain abd = new BodyPain("abd", true, true, false, true);
        bodyImageArray.add(abd);

        BodyPain ac = new BodyPain("ac", true, false, true, false);
        bodyImageArray.add(ac);

        BodyPain acd = new BodyPain("acd", true, false, true, true);
        bodyImageArray.add(acd);

        BodyPain ad = new BodyPain("ad", true, false, false, true);
        bodyImageArray.add(ad);

        BodyPain b = new BodyPain("b", false, true, false, false);
        bodyImageArray.add(b);

        BodyPain bc = new BodyPain("bc", false, true, true, false);
        bodyImageArray.add(bc);

        BodyPain bcd = new BodyPain("bcd", false, true, true, true);
        bodyImageArray.add(bcd);

        BodyPain bd = new BodyPain("bd", false, true, false, true);
        bodyImageArray.add(bd);

        BodyPain c = new BodyPain("c", false, false, true, false);
        bodyImageArray.add(c);

        BodyPain cd = new BodyPain("cd", false, false, true, true);
        bodyImageArray.add(cd);

        BodyPain d = new BodyPain("d", false, false, false, true);
        bodyImageArray.add(d);

        BodyPain body = new BodyPain("body", false, false, false, false);
        bodyImageArray.add(body);
    }

    public void getButtonValue(){
        filteredList = bodyImageArray.stream()
                .filter(p -> p.isLowerBack() == lowerBackButton.isChecked() && p.isOtherPain() == otherAreaButton.isChecked() && p.isSacrum() == sacrumButton.isChecked() && p.isShoulder() == shoulderButton.isChecked())
                .collect(Collectors.toCollection(ArrayList::new));
        String imagePath = filteredList.get(0).getPath();
        int id = getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
        Log.d("pathString", ""+id);
        elementBody.setImageResource(id);

    }

}
