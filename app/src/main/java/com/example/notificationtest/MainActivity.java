package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notificationtest.Services.OnClearFromRecentService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Playable{

    ImageButton play;
    TextView title;

    NotificationManager notificationManager;

    List<Track> tracks;
    int position = 0;
    boolean isPlaying = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        title = findViewById(R.id.title);

        populateTracks();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRAKS_TAKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CreateNotification.createNotification(MainActivity.this, tracks.get(1), R.drawable.ic_pause, 1, tracks.size()-1);
                if(isPlaying){
                    onTrackPause();
                }else{
                    onTrackPlay();
                }
            }
        });
    }

    private void populateTracks(){
        tracks = new ArrayList<>();

        tracks.add(new Track("track1","artist 1", R.drawable.image1));
        tracks.add(new Track("track2","artist 2", R.drawable.image2));
    }
    private void createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "NAME", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PLAY:
                    if(isPlaying){
                        onTrackPause();
                    }else{
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_STOP:
                    onTrackNext();
                    break;
            }
        }
    };

    @Override
    public void onTrackPrevious() {

        position--;
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        title.setText("TRACKSSKSKTITLEK");

    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        play.setImageResource(R.drawable.ic_pause);
        title.setText("PLAYYY");
        isPlaying = true;
    }

    @Override
    public void onTrackPause() {
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_play, position, tracks.size()-1);
        play.setImageResource(R.drawable.ic_play);
        title.setText("PAUZEE");
        isPlaying = false;
    }

    @Override
    public void onTrackNext() {
        position++;
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        title.setText("NEXCTTRAKCK");

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }
}