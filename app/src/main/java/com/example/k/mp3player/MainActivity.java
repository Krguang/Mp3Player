package com.example.k.mp3player;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button musicStart;
    private Button musicNext;
    private int picSwitchCount=0;
    private MediaPlayer mediaPlayer;
    private File[] musics;
    //private AsyncPlayer asyncPlayer = new AsyncPlayer(null);
    private int songIndex = 0;
    private ArrayList<String> songArrayList; //播放声音列表
    private String musicPath;
    private File music;
    private boolean firstStart=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new CompletionListener());
        musicPath="mnt/extsd";
        songArrayList = new ArrayList<String>();
        music = new File(musicPath);

        Log.d("music", "onCreate: "+music.isDirectory());
        musics = music.listFiles();
        if (musics!=null){
            for (File item : musics) {
                if (item.toString().endsWith(".mp3")){
                    songArrayList.add(item.toString());
                    Log.d("歌曲的位置", "→" +item);
                }
            }
        }

        musicStart=(Button)findViewById(R.id.music_start_id);
        musicNext=(Button)findViewById(R.id.musdic_next_id);
        musicStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musics!=null){
                    picSwitchCount++;
                    if (picSwitchCount%2==1){
                        musicStart.setBackgroundResource(R.drawable.music_pause);
                        if (firstStart){
                            firstStart=false;
                            songplay();
                        }
                        mediaPlayer.start();//播放

                    }else {
                        musicStart.setBackgroundResource(R.drawable.music_start);
                        mediaPlayer.pause();
                    }
                }
            }
        });

        musicNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picSwitchCount%2==1){
                    nextsong();
                }
            }
        });

        musicNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    view.setBackgroundResource(R.drawable.music_next_down);
                }else if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    view.setBackgroundResource(R.drawable.music_next_up);
                }
                return false;
            }
        });
    }

    private final class CompletionListener implements MediaPlayer.OnCompletionListener {

        public void onCompletion(MediaPlayer mp) {
            nextsong();
        }

    }
    private void nextsong() {

        if (songIndex < songArrayList.size() - 1) {
            songIndex = songIndex + 1;
            songplay();
        }
        else {
            songIndex = 0;
            songplay();
        }
    }
    private void songplay() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songArrayList.get(songIndex));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
}
