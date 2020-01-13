package com.example.anna;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.Gateway;


import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int CODE_REQUEST_CAMERA = 2810;
    private static final int CODE_REQUEST_RECORD_AUDIO = 2811;
    private static final int CODE_REQUEST_READ_PHONE_STATE = 2812;
    private boolean isCaptionEnabled;
    private FrameLayout captionLayout;
    private Intent recordService = null;
    private TextView captionText;
    private String vcToken;
    private Connector vc;
    private FrameLayout videoFrame;
    private User user;
    private TextView timeText;
    private int timeSecs;
    private List<String> sentences;
    private List<String> topics;
    SharedPreferences.Editor sfEditor;
    private long userType;
    private String eMail;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    // how to show video,
    // 1: single,
    // 2: partner,
    // 3: instructor
    private int videoType;

    class ReceiverRecord extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String doc = intent.getExtras().getString("text");
            sentences.add(doc);
            doc.replace(" ","_");
            putTranscribe(doc);
            if (videoType == 1)
                captionText.setText(doc);
            if (videoType > 1){

            }
        }
    }

    private void putTranscribe(String doc) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devcchotot.herokuapp.com/putTranscribe/" + doc;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PUT INFO", "put transcribe ok ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "no transcribe put");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        checkAndRequestPermissions();
        findViews();
        topics = new ArrayList<String>();
        Intent intent = getIntent();
        userType = intent.getIntExtra("userType",0);
        videoType = intent.getIntExtra("videoType",1);
        eMail = intent.getStringExtra("Email");
        String topic = intent.getStringExtra("topic");
        if (videoType == 0)
            return;
        user = new User();
        user = user.getInstance();
        Log.i("INFO", "onCreate: ok");
        mVisible = true;

        isCaptionEnabled = false;
        String userName = null;
        sfEditor = getSharedPreferences(getResources().getString(R.string.sharedPref), MODE_PRIVATE).edit();
        getToken(userName);
        sentences = new ArrayList<String>();
// Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        recordService = new Intent(this, Speech2TextService.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("get_text_action");
        Log.i("OnCreate", "Video type: "+String.valueOf(videoType));
        if (videoType == 1) {
            registerReceiver(new ReceiverRecord(), intentFilter);
            videoFrame.setVisibility(FrameLayout.GONE);
        }
        else {
            ConnectorPkg.setApplicationUIContext(this);
            ConnectorPkg.initialize();
            Log.i("CONNECTOR STATE", "INITIALIZED");
        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.start_button).setOnTouchListener(mDelayHideTouchListener);
//
//
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(ReceivondeserRecord);
    }

    private void GetTopics() {
        topics.add("What is your most favorite aspect about your school?");
    }

    private void findViews() {
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        captionText = (TextView) findViewById(R.id.caption_text);
        videoFrame = (FrameLayout) findViewById(R.id.backvideo_layout);
        captionLayout = (FrameLayout) findViewById(R.id.caption_layout);
        timeText = (TextView) findViewById(R.id.time_text);
    }
    @Override
    public void onBackPressed(){
        Disconnect((View)findViewById(R.id.caption_text));
    }

    private void getToken(String userName) {
        // Instantiate the RequestQueue.
        if (userName == null){
            userName = String.valueOf(new Random().nextInt(2810));
        }
        Log.i("INFO","userName: "+userName);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devcchotot.herokuapp.com/" + eMail;
        if (eMail == null)
            url = "http://devcchotot.herokuapp.com/demoUser";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        vcToken = response;
                        Log.i("TOKEN INFO", "vcToken: " + vcToken);
                        sfEditor.putString("token", vcToken);
                        sfEditor.apply();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "no vcToken updated");
//                Log.i("ERROR",Log.getStackTraceString(error.getCause().getCause()));

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getTranscribe(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devcchotot.herokuapp.com/getTranscribe";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        captionText.setText(response);
                        getTranscribe();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "no transcribe :"+error.toString());
                System.out.print(error.getStackTrace());

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void ShowTopic(){
        ((TextView)findViewById(R.id.caption_text)).setText(topics.get(0));
    }

    private void checkAndRequestPermissions() {
        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.CAMERA)) {
//            } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CODE_REQUEST_CAMERA);
        } else {
            // Permission has already been granted
        }
        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECORD_AUDIO)) {
//            } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    CODE_REQUEST_RECORD_AUDIO);

        } else {
            // Permission has already been granted
        }


        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECORD_AUDIO)) {
//            } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    CODE_REQUEST_READ_PHONE_STATE);

        } else {
            // Permission has already been granted
        }
        new CountDownTimer(2000, 500) {
            public void onTick(long remain) {
                Log.d("INFO", "onTick: ");
            }
            public void onFinish() {
                GoStart();
            }
        }.start();

    }

    public void GoStart() {
        if (videoType == 1){
            captionText =  (TextView)findViewById(R.id.ready_text);
        } else {
            vc = new Connector(videoFrame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, 15, "warning info@VidyoClient info@VidyoConnector", "", 0);
            vc.showViewAt(videoFrame, 0, 0, videoFrame.getWidth(), videoFrame.getHeight());
        }
        GetTopics();
        ShowTopic();
        ((TextView)findViewById(R.id.ready_text)).setText(getResources().getString(R.string.ready_string));
        findViewById(R.id.start_button).setVisibility(View.VISIBLE);
    }

    public void ToggleCaption(View v){
        isCaptionEnabled = !isCaptionEnabled;
        if (isCaptionEnabled) {
            captionLayout.setVisibility(FrameLayout.VISIBLE);
        } else {
            captionLayout.setVisibility(FrameLayout.GONE);
        }
        if (videoType == 1){

        } else if(videoType > 1) {

        }
    }

    public void Connect(View v) {
        timeSecs = 0;
        new CountDownTimer(150000,1500){
            public void onTick(long remain) {
                timeSecs += 1;
                timeText.setText(String.valueOf(timeSecs/60)+" : "+String.valueOf(timeSecs%60));
            }
            public void onFinish() {
                Disconnect(null);
            }
        }.start();
        if (videoType == 1){
            startService(recordService);
            Log.i("RECORDSTATE", "Record: ok");
        }else if (videoType > 1){
            if (vcToken == null){
                Log.i("ERROR", "Connect: NO TOKEN");
                getToken(null);
                if (vcToken == null)
                    return;
            }
            Connector.IConnect cnn = new Connector.IConnect() {

                public void onSuccess() {
                    Log.i("CONNECTSTATE", "Connect: ok");
                }

                public void onFailure(Connector.ConnectorFailReason reason) {
                    Log.i("CONNECTSTATE", "Connect: FAILED " + reason.toString());
                }

                public void onDisconnected(Connector.ConnectorDisconnectReason reason) {
                    Log.i("CONNECTSTATE", "Connect: disconnected " + reason.toString());
                }
            };
            vc.connect("prod.vidyo.io", vcToken, "DemoUser", "DemoRoom", cnn);
            Log.i("VC CONNECT", "FUNCTION CALLED");
            LinearLayout ll = findViewById(R.id.ready_layout);
            ll.setVisibility(LinearLayout.GONE);
        }
        View goneLayout = findViewById(R.id.control_layout);
        goneLayout.setVisibility(LinearLayout.VISIBLE);
    }

    private int res = 0;
    private int gradeSentences(){
        res = 0;
        for(String sent: sentences){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://devcchotot.herokuapp.com/nlp?sentence=" + sent;

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int max = Integer.parseInt(response);
                            res += max;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ERROR", "no response");
                    //      Log.i("ERROR",Log.getStackTraceString(error.getCause().getCause()));

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        return (int) res / sentences.size();
    }

    public void Disconnect(@Nullable View v) {
        if (vc != null){
            vc.disconnect();
            Log.i("INFO", "VC DISCONNECTED");
        }
        //write sentences to file
        try {
            File root = new File(Environment.getExternalStorageDirectory().toString());
            File gpxfile = new File(root, "transcribe.txt");
            FileWriter writer = new FileWriter(gpxfile);
            for (String sentence : sentences) {
                writer.append(sentence);
            }
            writer.flush();
            writer.close();
            Log.d("INFO", "IO: succeed");
        } catch (IOException e){
            Log.d("EXCEPTION", "IO: "+e.toString());
        }
        //TODO - send to server
        //int diffScore = gradeSentences();
        int diffScore = 5;
        //END TODO

        //get score from server
        int score = diffScore * 10;

        List<String> comments = new ArrayList<String>();
        //open score activity
        Intent inte = new Intent(this,ScoreBoardActivity.class);
        inte.putExtra("videoType",videoType);
        inte.putExtra("Score",score);
        inte.putExtra("Email",eMail);
//        inte.putExtra("CommentCount",comments.size());
//        for (int i = 0; i < comments.size(); i++){
//            inte.putExtra("Comment_"+String.valueOf(i),comments.get(i));
//        }
        startActivity(inte);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    return;
                }
            }
            case CODE_REQUEST_RECORD_AUDIO:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    return;
                }
            }
            return;
        }

        // other 'switch' lines to check for other
        // permissions this app might request
    }

    public void onSuccess() {
    }

    public void onFailure(Connector.ConnectorFailReason reason) {
    }

    public void onDisconnected(Connector.ConnectorDisconnectReason reason) {
    }


    // Something to touch the frame layout
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}