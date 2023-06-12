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
    boolean StartPlaying = false;
    String FilePath, FileName;
    final String LOG_TAG = "myLogs"; // название лога для просмотра действий при дебаге
    final int MAX_STREAMS = 10; //максимальное колво одновременно воспроизвидимых звуков
    private Button X;
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int beats = 8; //количество ударов - разнообразие партии
    private static final int instruments = 3; //количество инструментов для массива далее
    private EditText[] NumericValues = new EditText[2];
    private ImageButton imageFirst, stopButton, imageButtonleft, imageButtonright;
    private MediaPlayer soundExp, soundKick, soundSnare, soundMetronom;//звуки
    private CountDownTimer timer, timerDelay;//высчитываеся удары в минуту и стучит на каждый тик
    private SeekBar bar; //выбрать удары в минуту
    private int counterBeat;//показывает как такт стучит машина
    private SoundPool mSoundPool; //Обьект класса SoundPool для работы с парой и более звуков
    int soundIdHat, soundIdKick, soundIdClaps, soundIdMetronom, soundIdShot, soundIdExplosion, soundIdSnare; //ID для последующего обращения к звукам при загрузке в SoundPool

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this); //SoundPool не будет воспроизводить звуки пока все они не будут готовы к воспроизведению ( сделано во избежание ошибок)

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
        final EditText[][] examples = new EditText[2][6];
        examples[0][0] = (EditText) findViewById(R.id.sound1_1);
        examples[0][1] = (EditText) findViewById(R.id.sound1_2);
        examples[0][2] = (EditText) findViewById(R.id.sound1_2);
        examples[0][3] = (EditText) findViewById(R.id.sound1_2);
        examples[0][4] = (EditText) findViewById(R.id.sound1_2);
        examples[0][5] = (EditText) findViewById(R.id.sound1_2);
        examples[1][0] = (EditText) findViewById(R.id.sound1_2);
        examples[1][1] = (EditText) findViewById(R.id.sound1_2);
        examples[1][2] = (EditText) findViewById(R.id.sound2_3);
        examples[1][3] = (EditText) findViewById(R.id.sound2_4);
        examples[1][4] = (EditText) findViewById(R.id.sound1_2);
        examples[1][5] = (EditText) findViewById(R.id.sound2_6);
        final String message[] = new String[12];
        for (int i = 0; i < 11; i++) {
            message[i] = "0";
        }

        //ниже создан регион с обработкой listenerov каждого из EditText

//        for(int i = 0 ; i < 11; i++){
//            int finalI = i;
//            examples[i / 6][i % 6].addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    message[finalI] = examples[finalI / 6][finalI % 6].getText().toString();
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });

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
        Button playButton=findViewById(R.id.playButton);
        //endregion

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[][] input = new byte[message.length / 6][];
                for (int i = 0; i < message.length; i += 6) {//старт индекс и прибавление зависит от номера едиттекста, куда запиываюстя номера семплов
                    if (message[i].equals("1")) {
                        input[i / 6] = getData("kick");
                    } else if (message[i].equals("2")) {
                        input[i / 6] = getData("clap");
                    } else if (message[i].equals("3")) {
                        input[i / 6] = getData("snare");
                    } else if (message[i].equals("4")) {
                        input[i / 6] = getData("flute");
                    } else if (message[i].equals("5")) {
                        input[i / 6] = getData("hat");
                    } else{
                            input[i / 6] = new byte[1];
                    }
                }
//                byte[][] input2 = new byte[8][];
//                input2[0] = getData("kick");
//                input2[1] = getData("clap");
//                input2[2] = getData("flute");
//                input2[3] = getData("hat");
//                input2[4] = getData("clap");
//                input2[5] = getData("flute");
//                input2[6] = getData("kick");
//                input2[7] = getData("kick");
                byte[] output = SaveSamples(input, 120);
                if (output[0] != -1){
                    LoadSound(output);
                    mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId,
                                                   int status) {
                            Log.i("OnLoadCompleteListener", "Sound " + sampleId + " loaded.");
                            boolean loaded = true;
                            mSoundPool.play(LoadSoundID, 1, 1, 1, -1, 1);

                        }
                    });

                }
            }
        });


    }

    private void metronomSetter() {//задает метроном в зависимости от ударов в минуту и количества инстуремнтов (костыльно пока что)
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
                    Toast.makeText(MainActivity.this, FilePath, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void playSound(int SoundId, int effect) {
        if (effect == 0) {
            mSoundPool.play(SoundId, 1, 1, 0, 0, 1);
        } else if (effect == 1) {
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

    public void LoadSound(byte[] b) {
        File filetest;
        try {
            filetest = new File("/storage/emulated/0/Download/" + "output.bin");
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

            LoadSoundID = mSoundPool.load(filetest.getPath(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getData(String input) {
        //File myDir = new File(getCacheDir(), "folder");
        //myDir.mkdir();
        File file = new File(getCacheDir() + "/" + input + ".wav"); //плюсуется название файла
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "FileNotFound", Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
            Toast.makeText(MainActivity.this, "Cant read the file", Toast.LENGTH_SHORT).show();
        }
        return b;
    }

    static byte[] OverDrive(byte[] sample, double k) {
        int i, temp;
        int centre;//насколько смещается центр, относительно которого преобразуется звук
        for (i = 44; i < sample.length; i++) {
            temp = (int) ((sample[i]) * k);//может быть больше или меньше крайних чисел в типе данных
            if (temp > 127) {
                temp = 127;
            } else if (temp < -128) {
                temp = -128;
            }
            temp = (byte) ((temp) / k);//compressor
            sample[i] = (byte) temp;
        }
        return sample;
    }

    static byte MixSamples(byte sample1, byte sample2)
    {
        float mixed;
        byte output;
        if(sample1 == 0)
        {
            output = sample2;
        }
        else if(sample2 == 0)
        {
            output = sample1;
        }
        else
        {
            mixed = (sample1) / 128.0f + (sample2) / 128.0f;
            // reduce the volume a bit:
            mixed *= 0.8f;
            // hard clipping
            if (mixed > 1.0f) mixed = 1.0f;
            if (mixed < -1.0f) mixed = -1.0f;
            output = (byte)(mixed * 128);
        }

        return output;
    }
    static byte[] SaveSamples(byte[][] samples, int bpm) {
        int time4sample = 44100 * 30 / bpm;//кол-во байт на один шаг
        byte[] output = new byte[44 + time4sample * samples.length];
        int i, j;

        output[0] = -1;
        for (j = 0; j < samples.length; j++) {
            if (samples[j][0] != 0) {
                for (i = 0; i < 44; i++) {
                    output[i] = samples[j][i];
                }
                break;
            }
        }
        if (output[0] == -1) {
            return new byte[]{-1};
        }

        for (i = 44; i < output.length; i++) {
            output[i] = (byte)0;
        }
        output[4] = (byte) ((output.length - 44 + 36) % 256);
        output[5] = (byte) (((output.length - 44 + 36) / 256) % 256);
        output[6] = (byte) (((output.length - 44 + 36) / (256 * 256)) % 256);
        output[7] = (byte) (((output.length - 44 + 36) / (256 * 256 * 256)) % 256);
        output[40] = (byte) ((output.length - 44) % 256);
        output[41] = (byte) (((output.length - 44) / 256) % 256);
        output[42] = (byte) (((output.length - 44) / (256 * 256)) % 256);
        output[43] = (byte) (((output.length - 44) / (256 * 256 * 256)) % 256);
        for (j = 0; j < samples.length; j++) {
            for (i = 44; i < samples[j].length; i++) {
                if (i + time4sample * j > output.length - 1 || i - 44 > time4sample || samples[j][0] == -1) {
                    break;
                }
                output[i + time4sample * j] = MixSamples(output[i + time4sample * j], samples[j][i]);

            }
        }
        return output;
    }
}