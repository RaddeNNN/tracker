//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.secondapp3;

import android.content.Intent;
import android.icu.text.IDNA;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener {
    int LoadSoundID;
    String FilePath;
    String FileName;
    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 10;
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int beats = 8;
    private static final int instruments = 3;
    private EditText[] NumericValues = new EditText[2];
    private SoundPool mSoundPool;

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.mSoundPool = new SoundPool(10, 3, 0);
        this.mSoundPool.setOnLoadCompleteListener(this);
        final EditText[][] examples = new EditText[8][4];
        examples[0][0] = (EditText)this.findViewById(R.id.sound1_1);
        examples[0][1] = (EditText)this.findViewById(R.id.sound1_2);
        examples[0][2] = (EditText)this.findViewById(R.id.sound1_3);
        examples[0][3] = (EditText)this.findViewById(R.id.sound1_4);
        examples[1][0] = (EditText)this.findViewById(R.id.sound2_1);
        examples[1][1] = (EditText)this.findViewById(R.id.sound2_2);
        examples[1][2] = (EditText)this.findViewById(R.id.sound2_3);
        examples[1][3] = (EditText)this.findViewById(R.id.sound2_4);
        examples[2][0] = (EditText)this.findViewById(R.id.sound3_1);
        examples[2][1] = (EditText)this.findViewById(R.id.sound3_2);
        examples[2][2] = (EditText)this.findViewById(R.id.sound3_3);
        examples[2][3] = (EditText)this.findViewById(R.id.sound3_4);
        examples[3][0] = (EditText)this.findViewById(R.id.sound4_1);
        examples[3][1] = (EditText)this.findViewById(R.id.sound4_2);
        examples[3][2] = (EditText)this.findViewById(R.id.sound4_3);
        examples[3][3] = (EditText)this.findViewById(R.id.sound4_4);
        examples[4][0] = (EditText)this.findViewById(R.id.sound5_1);
        examples[4][1] = (EditText)this.findViewById(R.id.sound5_2);
        examples[4][2] = (EditText)this.findViewById(R.id.sound5_3);
        examples[4][3] = (EditText)this.findViewById(R.id.sound5_4);
        examples[5][0] = (EditText)this.findViewById(R.id.sound6_1);
        examples[5][1] = (EditText)this.findViewById(R.id.sound6_2);
        examples[5][2] = (EditText)this.findViewById(R.id.sound6_3);
        examples[5][3] = (EditText)this.findViewById(R.id.sound6_4);
        examples[6][0] = (EditText)this.findViewById(R.id.sound7_1);
        examples[6][1] = (EditText)this.findViewById(R.id.sound7_2);
        examples[6][2] = (EditText)this.findViewById(R.id.sound7_3);
        examples[6][3] = (EditText)this.findViewById(R.id.sound7_4);
        examples[7][0] = (EditText)this.findViewById(R.id.sound8_1);
        examples[7][1] = (EditText)this.findViewById(R.id.sound8_2);
        examples[7][2] = (EditText)this.findViewById(R.id.sound8_3);
        examples[7][3] = (EditText)this.findViewById(R.id.sound8_4);
        final EditText BPM = (EditText)this.findViewById(R.id.BPM);
        Button playButton = (Button)this.findViewById(R.id.playButton);
        Button stopButton = (Button)this.findViewById(R.id.button2);
        stopButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.mSoundPool.stop(MainActivity.this.LoadSoundID);
            }
        });
        Button InfoButton=findViewById(R.id.InfoButton);
        InfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "1=kick, 2=clap",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "3=snare, 4=flute",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "5=hat",Toast.LENGTH_SHORT).show();
            }
        });
        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int bpm = Integer.parseInt(BPM.getText().toString());
                if (bpm < 50 || bpm > 300) {
                    Toast.makeText(MainActivity.this, "Invalid BPM. Now it's 120.", Toast.LENGTH_LONG).show();
                    bpm = 120;
                    BPM.setText("120");
                }

                byte[][] input = new byte[examples.length][];

                for(int i = 0; i < examples.length; ++i) {
                    if (examples[i][0].getText().toString().equals("1")) {
                        input[i] = MainActivity.this.getData("kick");
                    } else if (examples[i][0].getText().toString().equals("2")) {
                        input[i] = MainActivity.this.getData("clap");
                    } else if (examples[i][0].getText().toString().equals("3")) {
                        input[i] = MainActivity.this.getData("snare");
                    } else if (examples[i][0].getText().toString().equals("4")) {
                        input[i] = MainActivity.this.getData("flute");
                    } else if (examples[i][0].getText().toString().equals("5")) {
                        input[i] = MainActivity.this.getData("hat");
                    } else {
                        input[i] = new byte[]{-1};
                    }

                    if (input[i][0] != -1) {
                        if (!examples[i][1].getText().toString().equals("00") && !examples[i][1].getText().toString().equals("0") && !examples[i][3].getText().toString().equals("")) {
                            MainActivity.Volume(input[i], (double)Integer.parseInt(examples[i][1].getText().toString()));
                        }

                        if (!examples[i][2].getText().toString().equals("00") && !examples[i][2].getText().toString().equals("0") && !examples[i][2].getText().toString().equals("")) {
                            MainActivity.DownSampler(input[i], Integer.parseInt(examples[i][2].getText().toString()));
                        }

                        if (!examples[i][3].getText().toString().equals("00") && !examples[i][3].getText().toString().equals("0") && !examples[i][3].getText().toString().equals("")) {
                            MainActivity.OverDrive(input[i], (double)Integer.parseInt(examples[i][3].getText().toString()));
                        }
                    }
                }

                byte[] output = MainActivity.SaveSamples(input, bpm);
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
        switch(requestCode) {
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
            File filetest = new File("/storage/emulated/0/Download/output.bin");
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
        byte[] b = new byte[(int)file.length()];

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

    static void Volume(byte[] sample, double k) {
        k = (k + 1.0D) / 25.0D;

        for(int i = 44; i < sample.length; ++i) {
            int temp = sample[i] & 255;
            temp = (int)((double)(temp - 128) * k);
            if (temp > 127) {
                temp = 127;
            } else if (temp < -128) {
                temp = -128;
            }

            sample[i] = (byte)(temp + 128);
        }

    }

    static void OverDrive(byte[] sample, double k) {
        k += 1;
        int temp;

        for(int i = 44; i < sample.length; ++i) {
            temp = sample[i] & 255;
            temp = (int)((double)(temp - 128) * k);
            if (temp > 127) {
                temp = 127;
            } else if (temp < -128) {
                temp = -128;
            }

            temp = (byte)((int)((double)(temp * 3) / k));
            sample[i] = (byte)(temp + 128);
        }

    }

    static void DownSampler(byte[] sample, int k) {
        for(int i = 44; i < sample.length; ++i) {
            if ((i - 44) % k != 0) {
                sample[i] = -128;
            }
        }

    }

    static byte MixSamples(byte sample1, byte sample2) {
        byte output;
        if (sample1 == 0) {
            output = sample2;
        } else if (sample2 == 0) {
            output = sample1;
        } else {
            float mixed = (float)sample1 / 128.0F + (float)sample2 / 128.0F;
            mixed *= 0.8F;
            if (mixed > 1.0F) {
                mixed = 1.0F;
            }

            if (mixed < -1.0F) {
                mixed = -1.0F;
            }

            output = (byte)((int)(mixed * 128.0F));
        }

        return output;
    }

    static byte[] SaveSamples(byte[][] samples, int bpm) {
        int time4sample = 44100 * 60 / bpm;
        byte[] output = new byte[44 + time4sample * samples.length];
        output[0] = -1;

        int i;
        label38:
        for(int j = 0; j < samples.length; ++j) {
            if (samples[j][0] != -1) {
                i = 0;

                while(true) {
                    if (i >= 44) {
                        break label38;
                    }

                    output[i] = samples[j][i];
                    ++i;
                }
            }
        }

        if (output[0] == -1) {
            return new byte[]{-1};
        } else {
            output[4] = (byte)((output.length - 44 + 36) % 256);
            output[5] = (byte)((output.length - 44 + 36) / 256 % 256);
            output[6] = (byte)((output.length - 44 + 36) / 65536 % 256);
            output[7] = (byte)((output.length - 44 + 36) / 16777216 % 256);
            output[40] = (byte)((output.length - 44) % 256);
            output[41] = (byte)((output.length - 44) / 256 % 256);
            output[42] = (byte)((output.length - 44) / 65536 % 256);
            output[43] = (byte)((output.length - 44) / 16777216 % 256);

            for(i = 0; i < output.length - 44; ++i) {
                if (i % time4sample + 44 >= samples[i / time4sample].length - 1) {
                    output[i + 44] = -128;
                } else {
                    output[i + 44] = samples[i / time4sample][i % time4sample + 44];
                }
            }

            return output;
        }
    }
}
