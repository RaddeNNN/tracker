package com.example.secondapp3;

import static android.content.ContentValues.TAG;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Toast;

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
    int LoadSoundID;
    boolean StartPlaying=false;
    String FilePath, FileName;
    final String LOG_TAG = "myLogs"; // название лога для просмотра действий при дебаге
    final int MAX_STREAMS = 10; //максимальное колво одновременно воспроизвидимых звуков
    private Button X;
    private static final int  PICKFILE_RESULT_CODE=1;
    private static final int beats = 8; //количество ударов - разнообразие партии
    private static final int instruments = 3; //количество инструментов для массива далее
    private EditText[] NumericValues=new EditText[2];
    private ImageButton imageFirst, stopButton, imageButtonleft, imageButtonright;
    private MediaPlayer soundExp, soundKick, soundSnare, soundMetronom;//звуки
    private CountDownTimer timer, timerDelay;//высчитываеся удары в минуту и стучит на каждый тик
    private SeekBar bar; //выбрать удары в минуту
    private int counterBeat;//показывает как такт стучит машина
    private SoundPool mSoundPool; //Обьект класса SoundPool для работы с парой и более звуков
    int soundIdHat, soundIdKick,soundIdClaps,soundIdMetronom,soundIdShot,soundIdExplosion,soundIdSnare; //ID для последующего обращения к звукам при загрузке в SoundPool

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this); //SoundPool не будет воспроизводить звуки пока все они не будут готовы к воспроизведению ( сделано во избежание ошибок)



        imageFirst = findViewById(R.id.imageFirst);
        stopButton = findViewById(R.id.stopButton);
        bar = findViewById(R.id.seekBar);
        X=findViewById(R.id.button);
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        final EditText[][] examples=new EditText[2][6];
        examples[0][0]=(EditText) findViewById(R.id.sound1_1);
        examples[0][1]=(EditText) findViewById(R.id.sound1_2);
        examples[0][2]=(EditText) findViewById(R.id.sound1_3);
        examples[0][3]=(EditText) findViewById(R.id.sound1_4);
        examples[0][4]=(EditText) findViewById(R.id.sound1_5);
        examples[0][5]=(EditText) findViewById(R.id.sound1_6);
        examples[1][0]=(EditText) findViewById(R.id.sound2_1);
        examples[1][1]=(EditText) findViewById(R.id.sound2_2);
        examples[1][2]=(EditText) findViewById(R.id.sound2_3);
        examples[1][3]=(EditText) findViewById(R.id.sound2_4);
        examples[1][4]=(EditText) findViewById(R.id.sound2_5);
        examples[1][5]=(EditText) findViewById(R.id.sound2_6);
        final String message[]=new String[12];
        for(int i=0; i<11;i++){
            message[i]="0";
        }

        //ниже создан регион с обработкой listenerov каждого из EditText

        //region ListenersAdded
        examples[0][0].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[0] = examples[0][0].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[0][1].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[1] = examples[0][1].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[0][2].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[2] = examples[0][2].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[0][3].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[3] = examples[0][3].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[0][4].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[4] = examples[0][4].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[0][5].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[5] = examples[0][5].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][0].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[6] = examples[1][0].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][1].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[7] = examples[1][1].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][2].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[8] = examples[1][2].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][3].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[9] = examples[1][3].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][4].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[10] = examples[1][4].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        examples[1][5].addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message[11] = examples[1][5].getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        //endregion

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message[11]==null){

                }
                else if(message[11].equals("1")) {
                        byte[] b = getData("kick");
                        LoadSound(b);
                    }
                else if(message[11].equals("2")){
                    byte[] b = getData("clap");
                    LoadSound(b);
                }
                else if(message[11].equals("3")){
                    byte[] b = getData("snare");
                    LoadSound(b);
                }
                else if(message[11].equals("4")){
                    byte[] b = getData("flute");
                    LoadSound(b);
                }
                else if(message[11].equals("5")){
                    byte[] b = getData("hat");
                    LoadSound(b);
                }
//                for(int i=0; i<12;i++){
//                    if(message[i].equals("1")) {
//                        byte[] b = getData("kick");
//                        LoadSound(b);
//                    }
//                    else if(message[i].equals("2")){
//                        byte[] b = getData("clap");
//                        LoadSound(b);
//                    }
//                    else if(message[i].equals("3")){
//                        byte[] b = getData("snare");
//                        LoadSound(b);
//                    }
//                    else if(message[i].equals("4")){
//                        byte[] b = getData("flute");
//                        LoadSound(b);
//                    }
//                    else if(message[i].equals("5")){
//                        byte[] b = getData("hat");
//                        LoadSound(b);
//                    }
//                    else{
//
//                    }
//                }
                mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId,
                                               int status) {
                        Log.i("OnLoadCompleteListener","Sound "+sampleId+" loaded.");
                        boolean loaded = true;
                        mSoundPool.play(LoadSoundID,1,1,1,-1,1);

                    }
                });


            }
        });



    }
    private void metronomSetter(){//задает метроном в зависимости от ударов в минуту и количества инстуремнтов (костыльно пока что)
        //if(flagsForPlay[0][counterBeat % beats]) playSound(soundIdExplosion, effects[5]);
        //if(flagsForPlay[1][counterBeat % beats]) playSound(soundIdKick, effects[1]);
        //if(flagsForPlay[2][counterBeat % beats]) playSound(soundIdSnare, effects[6]);
        //if(counterBeat < Integer.MAX_VALUE) counterBeat++;
        //else counterBeat = 0;
        //mSoundPool.play(soundIdMetronom,1,1,0,0,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    FilePath = data.getData().getPath();
                    FileName = data.getData().getLastPathSegment();
                    Toast.makeText(MainActivity.this,FilePath,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
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
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    } //функция которая показывает, что звук загружен в программу и готов к использованию
    public void LoadSound(byte[] b){
        File filetest;
        try{
            filetest=new File("/storage/emulated/0/Download/" + "output.bin");
            if (filetest.exists()) {
                filetest.delete();
            }
            try {
                filetest.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileOutputStream out2 = new FileOutputStream(filetest);
            try {
                out2.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out2.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                out2.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LoadSoundID=mSoundPool.load(filetest.getPath(),1);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public byte[] getData(String input){
        //File myDir = new File(getCacheDir(), "folder");
        //myDir.mkdir();
        File file = new File(getCacheDir() + "/" +input + ".wav"); //плюсуется название файла
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "FileNotFound", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
            Toast.makeText(MainActivity.this, "Cant read the file", Toast.LENGTH_SHORT).show();
        }
        return b;
    }
}