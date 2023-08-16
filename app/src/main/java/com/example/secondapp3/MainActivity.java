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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener {
    final int SAMPLESSCORE = 1;
    final int NUMEFFECTS = 3;
    int dynamicScore;
    int LoadSoundID;
    String FilePath;
    String FileName;
    byte[][][] outputMusic;
    private SoundPool mSoundPool;
    private boolean IsPlaying = false;
    EditText[][] examples = new EditText[8][5];
    EditText BPM;

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.mSoundPool = new SoundPool(10, 3, 0);
        this.mSoundPool.setOnLoadCompleteListener(this);

        examples[0][0] = (EditText) this.findViewById(R.id.sound1_1);
        examples[0][1] = (EditText) this.findViewById(R.id.sound1_2);
        examples[0][2] = (EditText) this.findViewById(R.id.sound1_3);
        examples[0][3] = (EditText) this.findViewById(R.id.sound1_4);
        examples[0][4]=  (EditText) this.findViewById(R.id.sound1_5);
        examples[1][0] = (EditText) this.findViewById(R.id.sound2_1);
        examples[1][1] = (EditText) this.findViewById(R.id.sound2_2);
        examples[1][2] = (EditText) this.findViewById(R.id.sound2_3);
        examples[1][3] = (EditText) this.findViewById(R.id.sound2_4);
        examples[1][4]=  (EditText) this.findViewById(R.id.sound2_5);
        examples[2][0] = (EditText) this.findViewById(R.id.sound3_1);
        examples[2][1] = (EditText) this.findViewById(R.id.sound3_2);
        examples[2][2] = (EditText) this.findViewById(R.id.sound3_3);
        examples[2][3] = (EditText) this.findViewById(R.id.sound3_4);
        examples[2][4]=  (EditText) this.findViewById(R.id.sound3_5);
        examples[3][0] = (EditText) this.findViewById(R.id.sound4_1);
        examples[3][1] = (EditText) this.findViewById(R.id.sound4_2);
        examples[3][2] = (EditText) this.findViewById(R.id.sound4_3);
        examples[3][3] = (EditText) this.findViewById(R.id.sound4_4);
        examples[3][4]=  (EditText) this.findViewById(R.id.sound4_5);
        examples[4][0] = (EditText) this.findViewById(R.id.sound5_1);
        examples[4][1] = (EditText) this.findViewById(R.id.sound5_2);
        examples[4][2] = (EditText) this.findViewById(R.id.sound5_3);
        examples[4][3] = (EditText) this.findViewById(R.id.sound5_4);
        examples[4][4]=  (EditText) this.findViewById(R.id.sound5_5);
        examples[5][0] = (EditText) this.findViewById(R.id.sound6_1);
        examples[5][1] = (EditText) this.findViewById(R.id.sound6_2);
        examples[5][2] = (EditText) this.findViewById(R.id.sound6_3);
        examples[5][3] = (EditText) this.findViewById(R.id.sound6_4);
        examples[5][4]=  (EditText) this.findViewById(R.id.sound6_5);
        examples[6][0] = (EditText) this.findViewById(R.id.sound7_1);
        examples[6][1] = (EditText) this.findViewById(R.id.sound7_2);
        examples[6][2] = (EditText) this.findViewById(R.id.sound7_3);
        examples[6][3] = (EditText) this.findViewById(R.id.sound7_4);
        examples[6][4]=  (EditText) this.findViewById(R.id.sound7_5);
        examples[7][0] = (EditText) this.findViewById(R.id.sound8_1);
        examples[7][1] = (EditText) this.findViewById(R.id.sound8_2);
        examples[7][2] = (EditText) this.findViewById(R.id.sound8_3);
        examples[7][3] = (EditText) this.findViewById(R.id.sound8_4);
        examples[7][4]=  (EditText) this.findViewById(R.id.sound8_5);

        int line, column;
        outputMusic = new byte[SAMPLESSCORE][8][];
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

        BPM = (EditText) this.findViewById(R.id.BPM);
        Button playButton = (Button) this.findViewById(R.id.playButton);
        Button stopButton = (Button) this.findViewById(R.id.button2);
        Button toLeft = (Button) this.findViewById(R.id.toLeft);
        Button toRight = (Button) this.findViewById(R.id.toRight);
        toLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dynamicScore < SAMPLESSCORE - 1) dynamicScore++;
                //to do: loading to byte[][][] array
                LoadToOutputMusic(outputMusic);
                //to do: loading coefs arrangement upper in cycle
                //to do: turning over the score
            }
        });
        toRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dynamicScore > 0) dynamicScore--;
                //to do: loading to byte[][][] array
                LoadToOutputMusic(outputMusic);
                //to do: loading coefs arrangement upper in cycle
                //to do: turning over the score
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
        Button LoadButton=findViewById(R.id.LoadButton);
        Button SaveButton=findViewById(R.id.SaveButton);
        LoadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoadData();
                    Toast.makeText(MainActivity.this,"Data loaded successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        SaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SaveData(examples);
                    Toast.makeText(MainActivity.this,"Saved successfully", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Error. Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!IsPlaying) {
                    IsPlaying = true;
                    int bpm = Integer.parseInt(BPM.getText().toString());
                    if (bpm < 50 || bpm > 300) {
                        Toast.makeText(MainActivity.this, "Invalid BPM. Now it's 80.", Toast.LENGTH_LONG).show();
                        bpm = 80;
                        BPM.setText("80");
                    }
                    LoadToOutputMusic(outputMusic);

//                    byte[][] input = new byte[examples.length][];
//                    String num; int number;
//                    boolean isSet;
//                    for (int i = 0; i < examples.length; ++i) {
//                        isSet = false;
//                        if (examples[i][0].getText().toString().equals("1")) {
//                            input[i] = MainActivity.this.getData("kick");
//                            isSet = true;
//
//                        } else if (examples[i][0].getText().toString().equals("2")) {
//                            input[i] = MainActivity.this.getData("clap");
//                            isSet = true;
//                        } else if (examples[i][0].getText().toString().equals("3")) {
//                            input[i] = MainActivity.this.getData("snare");
//                            num = examples[i][1].getText().toString();
//                            isSet = true;
//                        } else if (examples[i][0].getText().toString().equals("4")) {
//                            input[i] = MainActivity.this.getData("flute");
//                            isSet = true;
//                        } else if (examples[i][0].getText().toString().equals("5")) {
//                            input[i] = MainActivity.this.getData("hat");
//                            isSet = true;
//                        } else {
//                            input[i] = new byte[]{-1};
//                            isSet = false;
//                        }
//                        if (isSet){
//                            num = examples[i][1].getText().toString();
//                            if (num.equals("00") || num.equals("0") || num.equals("")) {
//                                examples[i][1].setText("50");
//                                WavFile.Volume(input[i], (double) 50);
//                            }
//                        }
//                        if (input[i][0] != -1) {
//                            num = examples[i][1].getText().toString();
//                            if (!num.equals("00") && !num.equals("0") && !num.equals("")) {
//                                number = Integer.parseInt(num);
//                                if(number > 99) {
//                                    number = 99;
//                                    examples[i][1].setText("99");
//                                }
//                                WavFile.Volume(input[i], (double) Integer.parseInt(examples[i][1].getText().toString()));
//                            }
//                            num = examples[i][2].getText().toString();
//                            if (!num.equals("00") && !num.equals("0") && !num.equals("")) {
//                                number = Integer.parseInt(num);
//                                if(number > 99) {
//                                    number = 99;
//                                    examples[i][1].setText("99");
//                                }
//                                WavFile.DownSampler(input[i], Integer.parseInt(examples[i][2].getText().toString()));
//                            }
//                            num = examples[i][3].getText().toString();
//                            if (!num.equals("00") && !num.equals("0") && !num.equals("")) {
//                                number = Integer.parseInt(num);
//                                if(number > 99) {
//                                    number = 99;
//                                    examples[i][1].setText("99");
//                                }
//                                WavFile.OverDrive(input[i], (double) Integer.parseInt(examples[i][3].getText().toString()));
//                            }
//                        }
//                    }
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
    public void EraseData(EditText[][] Data){
        for(int i=0; i<8; i++){
            for(int j=0; j<4; j++){
                Data[i][j].setText("00");
            }
        }
        BPM.setText("120");
    }
    public void SaveData(EditText[][] Data) throws FileNotFoundException {
        byte[] bytes=new byte[40];
        String storage="";
        for(int i=0;i<8;i++){
            for (int j=0;j<5;j++){
                storage+=Data[i][j].getText().toString();
                storage+=" ";
            }
        }
        storage+=BPM.getText().toString();
        bytes=storage.getBytes(StandardCharsets.UTF_8);
        File fileout = new File("/storage/emulated/0/Download/saved.txt");
        if (fileout.exists()) {
            fileout.delete();
        }

        try {
            fileout.createNewFile();
        } catch (IOException var9) {
            throw new RuntimeException(var9);
        }
        FileOutputStream out2 = new FileOutputStream(fileout);

        try {
            out2.write(bytes);
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
    }
    public void LoadData() throws IOException {
        File filein = new File("/storage/emulated/0/Download/saved.txt");
        BufferedReader reader = new BufferedReader(new FileReader(filein));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
// delete the last new line separator
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String content = stringBuilder.toString();
        String[] array = null;
        int j,i;
        array = content.split(" ");
        for(i=0;i<examples.length;i++){
            for (j=0;j<examples[i].length;j++){
                examples[i][j].setText(array[i*5+j]);
            }
        }
        BPM.setText(array[examples.length*5]);
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
}
abstract class LightTextWatcher implements TextWatcher {
    @Override public final void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public final void onTextChanged(CharSequence s, int start, int before, int count) {}
}