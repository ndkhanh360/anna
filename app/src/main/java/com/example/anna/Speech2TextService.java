package com.example.anna;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

//import static androidx.core.app.ActivityCompat.startActivityForResult;

public class Speech2TextService extends Service {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int DESTROY_FLAG = 1;
    private RecognitionListener recognitionListener = null;
    private SpeechRecognizer speechRecognizer = null;
    private Intent recognitionIntent;
    protected long mSpeechRecognizerStartListeningTime = 0;



    public Speech2TextService(){
    }

    private SpeechRecognizer getSpeechRecognizer(){
        if (speechRecognizer == null){
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getApplicationContext());
        }
        return speechRecognizer;
    }

    private RecognitionListener getRecognitionListener(){
        if (recognitionListener == null){
            recognitionListener = new RecognitionListener() {
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> voiceResults = results
                            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (voiceResults == null) {
                        System.out.println("No voice results");
                    }
                    else {
                        Intent done = new Intent();
                        done.setAction("get_text_action");
                        done.putExtra("text", voiceResults.get(0));
                        sendBroadcast(done);
//                    System.out.println("Printing matches: ");
//                    for (String match : voiceResults) {
//                        System.out.println(match);
//                    }
                    }

                }

                @Override
                public void onReadyForSpeech(Bundle params) {
                    System.out.println("Ready for speech");
                    Log.i("LISTEN", "Ready for speech");
                }

                /**
                 *  ERROR_NETWORK_TIMEOUT = 1;
                 *  ERROR_NETWORK = 2;
                 *  ERROR_AUDIO = 3;
                 *  ERROR_SERVER = 4;
                 *  ERROR_CLIENT = 5;
                 *  ERROR_SPEECH_TIMEOUT = 6;
                 *  ERROR_NO_MATCH = 7;
                 *  ERROR_RECOGNIZER_BUSY = 8;
                 *  ERROR_INSUFFICIENT_PERMISSIONS = 9;
                 *
                 * @param error code is defined in SpeechRecognizer
                 */
                @Override
                public void onError(int error) {
                    System.err.println("Error listening for speech: " + error);
                }

                @Override
                public void onBeginningOfSpeech() {
                    System.out.println("Speech starting");
                    Log.i("LISTEN", "Speech starting");
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onEndOfSpeech() {
                    // TODO Auto-generated method stub
                    System.out.println("End Speech");
                    Log.i("LISTEN", "End Speech");
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // TODO Auto-generated method stub
                }
            };
        }
        return recognitionListener;
    }

    private Intent getRecognitionIntent(){
        if (recognitionIntent == null){
            recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 500);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "ceo.ntphat.speech2text");
            //recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
            //recognitionIntent.putExtra(recognitionIntent.EX)
        }
        return  recognitionIntent;
    }

    private void startVoiceRecognition(){
        Intent recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "ceo.ntphat.speech2text");

        getSpeechRecognizer().setRecognitionListener(getRecognitionListener());
        getSpeechRecognizer().startListening(recognitionIntent);
    }

    @Override
    public int onStartCommand(Intent intent2, int flags, int startId) {

        final SpeechRecognizer recognizer = SpeechRecognizer
                .createSpeechRecognizer(this.getApplicationContext());
        mSpeechRecognizerStartListeningTime = System.currentTimeMillis();
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> voiceResults = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults == null) {
                    System.out.println("No voice results");
                }
                else {
                    Intent done = new Intent();
                    done.setAction("get_text_action");
                    done.putExtra("text", voiceResults.get(0));
                    sendBroadcast(done);
                }
                recognizer.startListening(getRecognitionIntent());
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
//                ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                String word = (String) data.get(data.size() - 1);
//                Intent done = new Intent();
//                done.setAction("get_text_action");
//                done.putExtra("text", word);
//                sendBroadcast(done);
//
//                Log.i("TEST", "partial_results: " + word);
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("Ready for speech");
                Log.i("LISTEN", "Ready for speech");
            }

            /**
             *  ERROR_NETWORK_TIMEOUT = 1;
             *  ERROR_NETWORK = 2;
             *  ERROR_AUDIO = 3;
             *  ERROR_SERVER = 4; // connect to internet
             *  ERROR_CLIENT = 5;
             *  ERROR_SPEECH_TIMEOUT = 6;
             *  ERROR_NO_MATCH = 7;
             *  ERROR_RECOGNIZER_BUSY = 8;
             *  ERROR_INSUFFICIENT_PERMISSIONS = 9;
             *
             * @param error code is defined in SpeechRecognizer
             */
            @Override
            public void onError(int error) {
                long duration = System.currentTimeMillis() - mSpeechRecognizerStartListeningTime;
                if (duration < 500){
                    return;
                }
                System.err.println("Error listening for speech: " + error);

                if (error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
                    return;
                }
                else if (error == 2){
                    recognizer.startListening(getRecognitionIntent());
                }
                else if (error == 7){
                    recognizer.startListening(getRecognitionIntent());
                }
                else if (error == 6){

                }
            }

            @Override
            public void onBeginningOfSpeech() {
                System.out.println("Speech starting");
                Log.i("LISTEN", "Speech starting");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
                System.out.println("End Speech");
                Log.i("LISTEN", "End Speech");
                //recognizer.startListening(getRecognitionIntent());
                //recognizer.setRecognitionListener(this);
                //recognizer.startListening(getRecognitionIntent());
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub
            }
        };

        recognizer.setRecognitionListener(listener);
        recognizer.startListening(getRecognitionIntent());

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        System.out.println("End Service");
        super.onDestroy();
    }
}
