package com.example.secondapp3;

import static android.content.ContentValues.TAG;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.SoundPool.OnLoadCompleteListener;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener {

    final String LOG_TAG = "myLogs"; // название лога для просмотра действий при дебаге
    final int MAX_STREAMS = 10; //максимальное колво одновременно воспроизвидимых звуков
    private Button X;
    private CheckBox effectApplier;
    private static final int beats = 8; //количество ударов - разнообразие партии
    private static final int instruments = 3; //количество инструментов для массива далее
    private TextView text, text4tool; //выводит на экран "инстурмент"
    private ImageButton imageFirst, stopButton, imageButtonleft, imageButtonright;
    private MediaPlayer soundExp, soundKick, soundSnare, soundMetronom;//звуки
    private CountDownTimer timer, timerDelay;//высчитываеся удары в минуту и стучит на каждый тик
    private SeekBar bar; //выбрать удары в минуту
    private int counterBeat;//показывает как такт стучит машина
    private int counterTool; //показывает какой инструмент выбран - всего 3 инструмента
    private CheckBox[] checksForStep; //массив для галочек для всех тактов
    private CheckBox firstcheck, secondcheck, thirdcheck, fourthcheck;//галочки для всех тактов
    private CheckBox fifthcheck, sixthcheck, seventhcheck, eighthcheck;
    private boolean[][] flagsForPlay; //массив флагов чтобы воспроизводить звуки
    private int[] effects=new int[7];
    private SoundPool mSoundPool; //Обьект класса SoundPool для работы с парой и более звуков
    int soundIdHat, soundIdKick,soundIdClaps,soundIdMetronom,soundIdShot,soundIdExplosion,soundIdSnare; //ID для последующего обращения к звукам при загрузке в SoundPool

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this); //SoundPool не будет воспроизводить звуки пока все они не будут готовы к воспроизведению ( сделано во избежание ошибок)

        //////
        soundIdShot = mSoundPool.load(this, R.raw.shot, 1);
        Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);
        soundIdExplosion = mSoundPool.load(this,R.raw.explosion, 1);
        Log.d(LOG_TAG, "soundIdExplosion = " + soundIdExplosion);
        soundIdMetronom = mSoundPool.load(this,R.raw.metronom, 1);
        Log.d(LOG_TAG, "soundIdMetronom = " + soundIdMetronom);
        soundIdSnare = mSoundPool.load(this, R.raw.snare, 1);
        Log.d(LOG_TAG, "soundIdSnare = " + soundIdSnare);
        soundIdKick = mSoundPool.load(this,R.raw.kick, 1);
        Log.d(LOG_TAG, "soundIdKick = " + soundIdKick);
        soundIdClaps = mSoundPool.load(this,R.raw.clap, 1);
        Log.d(LOG_TAG, "soundIdClaps = " + soundIdClaps);
        soundIdHat = mSoundPool.load(this,R.raw.hat, 1);
        Log.d(LOG_TAG, "soundIdHat = " + soundIdHat);
        ////// добавление всех звуков из папки raw в обьект класса SoundPool для последующего воспроизведения

        soundExp = MediaPlayer.create(this, R.raw.explosion);
        soundKick = MediaPlayer.create(this, R.raw.kick);
        soundSnare = MediaPlayer.create(this, R.raw.snare);
        soundMetronom = MediaPlayer.create(this, R.raw.metronom);


        imageFirst = findViewById(R.id.imageFirst);
        stopButton = findViewById(R.id.stopButton);
        bar = findViewById(R.id.seekBar);
        text = findViewById(R.id.textView);
        text4tool = findViewById(R.id.text4tool);
        X=findViewById(R.id.button);
        effectApplier=findViewById(R.id.checkBox9);
        firstcheck = findViewById(R.id.checkBox);
        secondcheck = findViewById(R.id.checkBox2);
        thirdcheck = findViewById(R.id.checkBox3);
        fourthcheck = findViewById(R.id.checkBox4);
        fifthcheck = findViewById(R.id.checkBox5);
        sixthcheck = findViewById(R.id.checkBox6);
        seventhcheck = findViewById(R.id.checkBox7);
        eighthcheck = findViewById(R.id.checkBox8);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<7; i++){
                    effects[i]=0;
                }
            }
        }, 0);
        firstcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,0);
            }
        });
        secondcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,1);
            }
        });
        thirdcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,2);
            }
        });
        fourthcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,3);
            }
        });
        fifthcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,4);
            }
        });
        sixthcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,5);
            }
        });
        seventhcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,6);
            }
        });
        eighthcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(v,7);
            }
        });
        effectApplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(counterTool%instruments){
                    case 0:
                        effects[5]=1;
                    case 1:
                        effects[1]=1;
                    case 2:
                        effects[6]=1;
                };
            }
        });

        checksForStep = new CheckBox[]{firstcheck, secondcheck, thirdcheck, fourthcheck,
                                        fifthcheck, sixthcheck, seventhcheck, eighthcheck};

        imageButtonleft = findViewById(R.id.imageButtonleft);
        imageButtonright = findViewById(R.id.imageButtonright);

        flagsForPlay = new boolean[instruments][beats]; //кол-во строк - кол-во инструментов; кол-во столбцов - кол-во шагов
        text4tool.setText(String.valueOf(counterTool%instruments));


        counterBeat = 0;
        timer = new CountDownTimer(10000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                metronomSetter();
            }
            @Override
            public void onFinish() {
                timer.start();
            }
        };

        imageFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                counterBeat = 0;
                timer.start();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });

        imageButtonleft.setOnClickListener(new View.OnClickListener() {//перелестывание в списке инструментов влево
            @Override
            public void onClick(View v) {
                if(counterTool > 0) counterTool--;
                else counterTool = Integer.MAX_VALUE - 2;
                changeChecked();

                changeText();

            }
        });
        imageButtonright.setOnClickListener(new View.OnClickListener() {//перелестывание в списке инструментов вправо
            @Override
            public void onClick(View v) {
                if(counterTool < Integer.MAX_VALUE - 2) counterTool++;
                else counterTool = 0;
                changeChecked();

                changeText();

            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timer.cancel();
                long tick = Math.round(60000.0 / progress);
                timer = new CountDownTimer(tick * 1000, tick) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        metronomSetter();
                    }
                    @Override
                    public void onFinish() {
                        timer.start();
                    }
                };
                text.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


    }
    private void changeChecked(){ //срабатывает при перелистывании страницы
        for(int i = 0; i < 8; i++) {
            checksForStep[i].setChecked(flagsForPlay[counterTool % instruments][i]);
        }
//        firstcheck.setChecked(flagsForPlay[counterTool%instruments][0]);
//        secondcheck.setChecked(flagsForPlay[counterTool%instruments][1]);
//        thirdcheck.setChecked(flagsForPlay[counterTool%instruments][2]);
//        fourthcheck.setChecked(flagsForPlay[counterTool%instruments][3]);
//        fifthcheck.setChecked(flagsForPlay[counterTool%instruments][4]);
//        sixthcheck.setChecked(flagsForPlay[counterTool%instruments][5]);
//        seventhcheck.setChecked(flagsForPlay[counterTool%instruments][6]);
//        eighthcheck.setChecked(flagsForPlay[counterTool%instruments][7]);
    }
    private void checkChanged(View v, int i){//срабатывает когда пользователь взаимодействует с чекбоксом
        timer.cancel();
        if(((CheckBox)v).isChecked()){
            flagsForPlay[counterTool%3][i] = true;
        }
        else {
            flagsForPlay[counterTool%3][i] = false;
        }
    }
    private void metronomSetter(){//задает метроном в зависимости от ударов в минуту и количества инстуремнтов (костыльно пока что)
        if(flagsForPlay[0][counterBeat % beats]) playSound(soundIdExplosion, effects[5]);
        if(flagsForPlay[1][counterBeat % beats]) playSound(soundIdKick, effects[1]);
        if(flagsForPlay[2][counterBeat % beats]) playSound(soundIdSnare, effects[6]);
        if(counterBeat < Integer.MAX_VALUE) counterBeat++;
        else counterBeat = 0;
        mSoundPool.play(soundIdMetronom,1,1,0,0,1);
    }
    private void changeText(){
        String texto;
        switch(counterTool%instruments){
            case 0:
                texto = "взрыв";
                break;
            case 1:
                texto = "кик";
                break;
            case 2:
                texto = "барабан";
                break;
            default:
                texto = "gg";
                break;
        };
        text4tool.setText(texto);
    }//
    private void mergePlaySounds(){
        mSoundPool.play(soundIdShot, 1, 1, 0, 0, 1);
        mSoundPool.play(soundIdExplosion, 1, 1, 0, 0, 1);
        mSoundPool.play(soundIdMetronom,1,1,0,-1,1);
        mSoundPool.play(soundIdClaps,1,1,0,-1,1);
        mSoundPool.play(soundIdKick,1,1,0,0,1);
        mSoundPool.play(soundIdSnare,1,1,0,-1,1);
    } //функция для параллельного воспроизведения нескольких звуков (сейчас это набор шаблонов)
    private void playSound(int SoundId, int effect){
        if(effect==0){
            mSoundPool.play(SoundId, 1, 1, 0, 0, 1);
        }
        else if(effect==1) {
            mSoundPool.play(SoundId, 1, 1, 0, 0, 1);
            Timer timerDelay = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    mSoundPool.play(SoundId, 1, 1, 0, 0, 1);
                }
            };
            timerDelay.schedule(task, 20);
        }
    }
    private void writeToFile(byte[] array)
    {
        File file = new File("C:\\Users\\Dmitriy\\Downloads\\test.wav");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(array);
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    } //функция которая показывает, что звук загружен в программу и готов к использованию
    public void OnClick(View v){

    }
}