//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.secondapp3;

import android.content.Intent;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//to do:
//better design for numScore (обозначение, какая дорожка сейчас на экране) - денис
//add listeners for numLine (восемь обработчиков для строк, текст в которых будет меняться) - витя
//add more lines for output music - витя
//add export button to save the file (.wav / .mod) to download - денис
//add save function to save the nums in edittexts to download - денис
public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener {
    final int SAMPLESSCORE = 2;
    final int NUMEFFECTS = 3;
    int dynamicScore;
    int dynamicLines;//use for loading output stuff
    int LoadSoundID;
    String FilePath;
    String FileName;
    byte[][][] outputMusic;
    int[][][] numsMusic;
    private SoundPool mSoundPool;
    private boolean IsPlaying = false;
    EditText[][] examples = new EditText[8][4];

    //need to be made better
    TextView numScore;

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.mSoundPool = new SoundPool(10, 3, 0);
        this.mSoundPool.setOnLoadCompleteListener(this);

        numScore = findViewById(R.id.numScore);

        examples[0][0] = (EditText) this.findViewById(R.id.sound1_1);
        examples[0][1] = (EditText) this.findViewById(R.id.sound1_2);
        examples[0][2] = (EditText) this.findViewById(R.id.sound1_3);
        examples[0][3] = (EditText) this.findViewById(R.id.sound1_4);
        examples[1][0] = (EditText) this.findViewById(R.id.sound2_1);
        examples[1][1] = (EditText) this.findViewById(R.id.sound2_2);
        examples[1][2] = (EditText) this.findViewById(R.id.sound2_3);
        examples[1][3] = (EditText) this.findViewById(R.id.sound2_4);
        examples[2][0] = (EditText) this.findViewById(R.id.sound3_1);
        examples[2][1] = (EditText) this.findViewById(R.id.sound3_2);
        examples[2][2] = (EditText) this.findViewById(R.id.sound3_3);
        examples[2][3] = (EditText) this.findViewById(R.id.sound3_4);
        examples[3][0] = (EditText) this.findViewById(R.id.sound4_1);
        examples[3][1] = (EditText) this.findViewById(R.id.sound4_2);
        examples[3][2] = (EditText) this.findViewById(R.id.sound4_3);
        examples[3][3] = (EditText) this.findViewById(R.id.sound4_4);
        examples[4][0] = (EditText) this.findViewById(R.id.sound5_1);
        examples[4][1] = (EditText) this.findViewById(R.id.sound5_2);
        examples[4][2] = (EditText) this.findViewById(R.id.sound5_3);
        examples[4][3] = (EditText) this.findViewById(R.id.sound5_4);
        examples[5][0] = (EditText) this.findViewById(R.id.sound6_1);
        examples[5][1] = (EditText) this.findViewById(R.id.sound6_2);
        examples[5][2] = (EditText) this.findViewById(R.id.sound6_3);
        examples[5][3] = (EditText) this.findViewById(R.id.sound6_4);
        examples[6][0] = (EditText) this.findViewById(R.id.sound7_1);
        examples[6][1] = (EditText) this.findViewById(R.id.sound7_2);
        examples[6][2] = (EditText) this.findViewById(R.id.sound7_3);
        examples[6][3] = (EditText) this.findViewById(R.id.sound7_4);
        examples[7][0] = (EditText) this.findViewById(R.id.sound8_1);
        examples[7][1] = (EditText) this.findViewById(R.id.sound8_2);
        examples[7][2] = (EditText) this.findViewById(R.id.sound8_3);
        examples[7][3] = (EditText) this.findViewById(R.id.sound8_4);

        int line, column;
        outputMusic = new byte[SAMPLESSCORE][8][];
        numsMusic = new int[SAMPLESSCORE][8][NUMEFFECTS + 1];
        dynamicScore = 0;
        //cycle for samples
        for(line = 0; line < examples.length; line++){
                int LINE = line;
                examples[line][0].addTextChangedListener(new LightTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        //cleaning
                        if(s.toString().length() == 1 && Integer.parseInt(s.toString()) > 5) examples[LINE][0].setText("5");
                        else if(s.toString().length() == 2){
                            if(s.toString().charAt(0) == '0'){
                                if(Integer.parseInt(s.toString().substring(1,2)) > 5) examples[LINE][0].setText("05");
                            }
                            else if(Integer.parseInt(s.toString().substring(0,2)) > 5) examples[LINE][0].setText("5");
                        }
                        else if(s.toString().length() > 2) examples[LINE][0].setText(s.toString().substring(1,3));
                    }
                });
        }
        //cycle for effects
        for(line = 0; line < examples.length; line++){
            for(column = 1; column < examples[line].length; column++){
                int COLUMN = column;
                int LINE = line;
                examples[line][column].addTextChangedListener(new LightTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        //cleaning
                        if(s.toString().length() > 2) examples[LINE][COLUMN].setText(s.toString().substring(1,3));
                    }
                });
            }
        }

        final EditText BPM = (EditText) this.findViewById(R.id.BPM);
        Button playButton = (Button) this.findViewById(R.id.playButton);
        Button stopButton = (Button) this.findViewById(R.id.button2);
        Button toLeft = (Button) this.findViewById(R.id.toLeft);
        Button toRight = (Button) this.findViewById(R.id.toRight);

        numScore.setText(String.valueOf(dynamicScore + 1));

        toLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadToOutput(outputMusic, numsMusic);
                if(dynamicScore > 0) dynamicScore--;
                numScore.setText(String.valueOf(dynamicScore + 1));
                LoadFromNumsMusic(numsMusic);
                //fix loading to output
            }
        });
        toRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadToOutput(outputMusic, numsMusic);
                if(dynamicScore < SAMPLESSCORE - 1) dynamicScore++;
                numScore.setText(String.valueOf(dynamicScore + 1));
                LoadFromNumsMusic(numsMusic);
                //fix loading to output
            }
        });
        stopButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                IsPlaying = false;
                MainActivity.this.mSoundPool.stop(MainActivity.this.LoadSoundID);
            }
        });
        Button InfoButton = findViewById(R.id.InfoButton);
        InfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "1 = kick, 2 = clap", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "3 = snare, 4 = flute", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "5 = hat", Toast.LENGTH_SHORT).show();
            }
        });
        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (IsPlaying) {
                    IsPlaying = false;
                    MainActivity.this.mSoundPool.stop(MainActivity.this.LoadSoundID);
                }
                IsPlaying = true;
                int bpm = Integer.parseInt(BPM.getText().toString());
                if (bpm < 50 || bpm > 300) {
                    Toast.makeText(MainActivity.this, "Invalid BPM. Now it's 120.", Toast.LENGTH_LONG).show();
                    bpm = 120;
                    BPM.setText("120");
                }
                LoadToOutput(outputMusic, numsMusic);
                LoadFromNumsMusic(numsMusic);

                byte[] output = WavFile.SaveSamples(outputMusic, bpm);
                if (output[0] != -1) {
                    MainActivity.this.LoadSound(output);
                    MainActivity.this.mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Log.i("OnLoadCompleteListener", "Sound " + sampleId + " loaded.");
                            boolean loaded = true;
                            MainActivity.this.mSoundPool.play(MainActivity.this.LoadSoundID, 1.0F, 1.0F, 1, -1, 1.0F);
                        }
                    });
                }

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    this.FilePath = data.getData().getPath();
                    this.FileName = data.getData().getLastPathSegment();
                    Toast.makeText(this, this.FilePath, Toast.LENGTH_LONG).show();
                }
            default:
        }
    }

    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d("myLogs", "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }

    public void LoadSound(byte[] b) {
        try {
            File filetest = new File("/storage/emulated/0/Download/output.wav");
            if (filetest.exists()) {
                filetest.delete();
            }

            try {
                filetest.createNewFile();
            } catch (IOException var9) {
                throw new RuntimeException(var9);
            }

            FileOutputStream out2 = new FileOutputStream(filetest);

            try {
                out2.write(b);
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            try {
                out2.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            try {
                out2.flush();
            } catch (IOException var6) {
                throw new RuntimeException(var6);
            }

            try {
                out2.close();
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }

            this.LoadSoundID = this.mSoundPool.load(filetest.getPath(), 1);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public byte[] getData(String input) {
        File file = new File(this.getCacheDir() + "/" + input + ".wav");
        byte[] b = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
        } catch (FileNotFoundException var5) {
            System.out.println("File Not Found.");
            var5.printStackTrace();
            Toast.makeText(this, "FileNotFound", Toast.LENGTH_LONG).show();
        } catch (IOException var6) {
            System.out.println("Error Reading The File.");
            var6.printStackTrace();
            Toast.makeText(this, "Cant read the file", Toast.LENGTH_LONG).show();
        }

        return b;
    }

    public byte[] getSample(Editable s) {
        int k = GetCoef(s);
        if (k == 1) {
            return MainActivity.this.getData("kick");
        } else if (k == 2) {
            return MainActivity.this.getData("clap");
        } else if (k == 3) {
            return MainActivity.this.getData("snare");
        } else if (k == 4) {
            return MainActivity.this.getData("flute");
        } else if (k == 5) {
            return MainActivity.this.getData("hat");
        } else {
            return new byte[]{-1};
        }
    }

    public int GetCoef(Editable s){
        if(s.toString().length() == 0) return 0;
        else if(s.toString().length() == 1) return Integer.parseInt(s.toString());
        else if(s.toString().length() == 2) {
            if(s.toString().charAt(0) == '0') return Integer.parseInt(s.toString().substring(1,2));
            else return Integer.parseInt(s.toString());
        }
        else return -1;
    }

    public boolean CheckNum(String snum){
        if (!snum.equals("00") && !snum.equals("0") && !snum.equals("")) return true;
        else return false;
    }

    public void LoadToOutputMusic(byte[][][] outputM){
        int line;
        for(line = outputM[dynamicScore].length / 8 - 1; line < outputM[dynamicScore].length / 8 - 1 + 8; line++){
            //applying data
            outputM[dynamicScore][line] = getSample(examples[line][0].getText());
            //applying effect
            for(int effect = 0; effect < NUMEFFECTS; effect++) {
                if (CheckNum(examples[line][effect + 1].getText().toString()))
                    WavFile.ApplyEffect(
                            outputM[dynamicScore][line],
                            Integer.parseInt(examples[line][effect + 1].getText().toString()),
                            Effect.getInstance(effect));
            }
        }
    }
    public void LoadToNumsMusic(int[][][] numsM){
        int line, index;
        for(line = 0; line < numsM[0].length; line++){//dynamicLines here
            for(index = 0; index < NUMEFFECTS + 1; index++){
                numsM[dynamicScore][line][index] = GetCoef(examples[line][index].getText());
            }
        }
    }
    public void LoadToOutput(byte[][][] outputM, int[][][] numsM){
        LoadToOutputMusic(outputM);
        LoadToNumsMusic(numsM);
    }
    public void LoadFromNumsMusic(int[][][] numsM){
        int line, index;
        String num;
        for(line = 0; line < numsM[0].length; line++){//dynamicLines here
            for(index = 0; index < NUMEFFECTS + 1; index++){
                num = String.valueOf(numsM[dynamicScore][line][index]);
                if(num.length() < 2) num = "0" + num;
                examples[line][index].setText(num);
            }
        }
    }
}
abstract class LightTextWatcher implements TextWatcher {
    @Override public final void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public final void onTextChanged(CharSequence s, int start, int before, int count) {}
}