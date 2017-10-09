package com.job.permisomicrofonoapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button bGrabar, bDetener, bReproducir;
    private static final String LOG_TAG = "Main";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio.3gp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bGrabar = (Button) findViewById(R.id.button1);
        bDetener = (Button) findViewById(R.id.button2);
        bReproducir = (Button) findViewById(R.id.button3);
        bDetener.setEnabled(false);
        bReproducir.setEnabled(false);

    }

    private static final int PERMISSIONS_REQUEST = 100;

    public void grabar(View view) {

        // Check permission (Api 22 check in Manifest, Api 23 check by requestPermissions)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Dont have permission => request one or many permissions (String[])
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
        } else {
            // Have permission
            comenzarGrabacion();
        }
    }

    // On request permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (permissions[0].equals(Manifest.permission.RECORD_AUDIO)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        comenzarGrabacion();
                    } else {
                        Toast.makeText(this, "Permiso Rechazado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void comenzarGrabacion() {
        bGrabar.setEnabled(false);
        bDetener.setEnabled(true);
        bReproducir.setEnabled(false);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOutputFile(fichero);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fallo en grabación");
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

        }
        mediaRecorder.start();
    }

    public void detenerGrabacion(View view){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder  = null;

        bDetener.setEnabled(false);
        bGrabar.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();

    }

    public void reproducir(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fichero);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fallo en reproducción");
        }

    }
}