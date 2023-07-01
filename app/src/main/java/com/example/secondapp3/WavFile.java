package com.example.secondapp3;

public class WavFile {
//to do: change coefs in effects
    //delay
    //better saving function - the sounds shoudnt be cut
    private static void Volume(byte[] sample, double k) {
        k = k / 50.0D;

        for (int i = 44; i < sample.length; ++i) {
            int temp = sample[i] & 0xFF;
            temp = (int) ((double) (temp - 128) * k);
            if (temp > 127) {
                temp = 127;
            } else if (temp < -128) {
                temp = -128;
            }

            sample[i] = (byte) (temp + 128);
        }

    }

    private static void OverDrive(byte[] sample, double k) {
        k += 1;
        int temp;

        for (int i = 44; i < sample.length; ++i) {
            temp = sample[i] & 0xFF;
            temp = (int) ((double) (temp - 128) * k);
            if (temp > 127) {
                temp = 127;
            } else if (temp < -128) {
                temp = -128;
            }

            temp = (byte) ((int) ((double) (temp * 3) / k));
            sample[i] = (byte) (temp + 128);
        }

    }

    private static void DownSampler(byte[] sample, double k) {

        for (int i = 44; i < sample.length; ++i) {
            if ((i - 44) % ((int)k) != 0) {
                sample[i] = -128;
            }
        }

    }

    static void ApplyEffect(byte[] sample, double k, Effect effect) {
        if (effect == Effect.VOLUME) {
            Volume(sample, k);
        }
        else if (effect == Effect.DECIMATOR) {
            DownSampler(sample, k);
        }
        else if (effect == Effect.OVERDRIVE) {
            OverDrive(sample, k);
        }
    }

    static byte MixSamples(byte sample1, byte sample2) {
        byte output;
        int intSample1, intSample2;
        intSample1 = (sample1 & 0xFF) - 128;
        intSample2 = (sample2 & 0xFF) - 128;
        float mixed = ((float)intSample1 + (float)intSample2) / 128.0F;

        mixed *= 0.6F;
        if (mixed > 1.0F)
        {
            mixed = 1.0F;
        }
        else if (mixed < -1.0F)
        {
            mixed = -1.0F;
        }
        output = (byte)((((int)(mixed * 128.0F)) & 0xFF) + 128);

        return output;
    }

    static byte[] MixSamples(byte[][]samples) {
        byte[] output = new byte[]{-1};
        int startScore = 0;
        for(int sample = 0; sample < samples.length; sample++){
            if(samples[sample][0] != -1){
                output = samples[sample].clone();
                startScore = sample;
                break;
            }
        }
        if (output[0] == -1) {
            return new byte[]{-1};
        }
        else{
            int i, score;
            for(i = 44; i < output.length; i++){
                for(score = startScore + 1; score < samples.length; score++){
                    if (samples[score] != null && samples[score][0] != -1) {
                        output[i] = MixSamples(output[i], samples[score][i]);
                    }
                }
            }
            return output;
        }
    }

    static byte[] SaveSamples(byte[][][] samples, int bpm) {
        byte[][] tempOutput = new byte[samples.length][];
        for(int score = 0; score < samples.length; score++){
            tempOutput[score] = SaveSamples(samples[score], bpm);
        }
        return MixSamples(tempOutput);
    }

    static byte[] SaveSamples(byte[][] samples, int bpm) {
        int i;
        int time4sample = 44100 * 30 / bpm;
        byte[] output = new byte[44 + time4sample * samples.length];
        output[0] = -1;


        for(int j = 0; j < samples.length; ++j) {
            if (samples[j] != null && samples[j][0] != -1) {
                for(i = 0; i < 44; i++){
                    output[i] = samples[j][i];
                }
                break;
            }
        }

        if (output[0] == -1) {
            return new byte[]{-1};
        } else {
            output[4] = (byte) ((output.length - 44 + 36) % 256);
            output[5] = (byte) ((output.length - 44 + 36) / 256 % 256);
            output[6] = (byte) ((output.length - 44 + 36) / 65536 % 256);
            output[7] = (byte) ((output.length - 44 + 36) / 16777216 % 256);
            output[40] = (byte) ((output.length - 44) % 256);
            output[41] = (byte) ((output.length - 44) / 256 % 256);
            output[42] = (byte) ((output.length - 44) / 65536 % 256);
            output[43] = (byte) ((output.length - 44) / 16777216 % 256);

            for (i = 0; i < output.length - 44; ++i) {
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
