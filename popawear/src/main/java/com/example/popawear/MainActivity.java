package com.example.popawear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

/**
 * Class that Contains the Controller of the Diary View of the Smartwatch.
 */
public class MainActivity extends WearableActivity {

    private final static String TAG = "Wear MainActivity";
    private TextView mTextView;
    private TextView textMood;
    private  int[] data;
    private int counter;
    Button myButton;
    int num = 0;
    String datapath = "/message_path";
    SeekBar seekBarMood;
    Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new int[3];
        counter = 0;
        mTextView = (TextView) findViewById(R.id.text);
        seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
        myButton = findViewById(R.id.myButton);
        seekBarMood.setMax(10);
        seekBarMood.setProgress(10);
        textMood = (TextView) findViewById(R.id.textMood);
        seekBarControl();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            buttonController(v);            }
            });



        // Enables Always-on
        setAmbientEnabled();
    }
    //The seekbars to control the value of the mood/sleep/pain
    public void seekBarControl(){
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMood.setText(String.valueOf(progress));
                num = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    //best Spaghetti code that has a counter which counts the Views that have been displayed. and thanks to it loads one view after the other. (resets after the third View)
    public void buttonController(View v) {
        if (counter == 0) {
            setContentView(R.layout.activity_pain);
            mTextView = (TextView) findViewById(R.id.text);
            seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
            myButton = findViewById(R.id.myButton);
            textMood = (TextView) findViewById(R.id.textMood);
            seekBarMood.setMax(10);
            seekBarMood.setProgress(10);
            seekBarControl();
            data[0] = num;
            counter++;
            String message = "M" + num;
            //Requires a new thread to avoid blocking the UI
            new SendThread(datapath, message).start();
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonController(v);
                }
            });

        } else if (counter == 1) {

            setContentView(R.layout.activity_sleep);
            myButton = findViewById(R.id.myButton);
            mTextView = (TextView) findViewById(R.id.text);
            seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
            textMood = (TextView) findViewById(R.id.textMood);
            seekBarMood.setMax(10);
            seekBarMood.setProgress(10);
            seekBarControl();
            data[0] = num;
            counter++;
            String message = "P" + num;
            //Requires a new thread to avoid blocking the UI
            new SendThread(datapath, message).start();
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonController(v);
                }
            });
        }else if (counter == 2) {

            intent = new Intent(v.getContext(), HomeActivity.class);
            startActivity(intent);
            data[0] = num;
            String message = "S" + num;
            //Requires a new thread to avoid blocking the UI
            new SendThread(datapath, message).start();
        }
        }





    //This actually sends the message to the wearable device.
    class SendThread extends Thread {
        String path;
        String message;

        //constructor
        SendThread(String p, String msg) {
            path = p;
            message = msg;
        }
        //sends the message via the thread.  this will send to all wearables connected, but
        //since there is (should only?) be one, so no problem.
        public void run() {
            //first get all the nodes, ie connected wearable devices.
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                // Block on a task and get the result synchronously (because this is on a background
                // thread).
                List<Node> nodes = Tasks.await(nodeListTask);

                //Now send the message to each device.
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());

                    try {
                        // Block on a task and get the result synchronously (because this is on a background
                        // thread).
                        Integer result = Tasks.await(sendMessageTask);
                        Log.v(TAG, "SendThread: message send to " + node.getDisplayName());

                    } catch (ExecutionException exception) {
                        Log.e(TAG, "Task failed: " + exception);

                    } catch (InterruptedException exception) {
                        Log.e(TAG, "Interrupt occurred: " + exception);
                    }

                }

            } catch (ExecutionException exception) {
                Log.e(TAG, "Task failed: " + exception);

            } catch (InterruptedException exception) {
                Log.e(TAG, "Interrupt occurred: " + exception);
            }
        }
    }
}