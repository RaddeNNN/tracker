package com.example.secondapp3;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioFiles {
    public AudioFormat audioFormat;
    public InputStream audioFile;
    public static void main(String[] args) {
        String file = "C:\\Users\\Dmitriy\\Downloads\\Roland-GR-1-Clarinet-C5.wav";
        AudioFiles afiles = new AudioFiles();
        byte[] data1 = afiles.readAudioFileData(file);
        byte[] data2 = afiles.readWAVAudioFileData(file);
        System.out.format("data len: %d\n", data1.length);
        System.out.format("data len: %d\n", data2.length);
        System.out.format("diff len: %d\n", data2.length - data1.length);
        afiles.toWav(data1);
        afiles.toWav(data2);
    }

    public byte[] readAudioFileData(final String filePath) {
        byte[] data = null;
        try {
            final ByteArrayOutputStream baout = new ByteArrayOutputStream();
            final File file = new File(filePath);
            final AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(file);

            byte[] buffer = new byte[4096];
            int c;
            while ((c = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                baout.write(buffer, 0, c);
            }
            audioInputStream.close();
            baout.close();
            data = baout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public byte[] readWAVAudioFileData(final String filePath) {
        byte[] data = null;
        try {
            final ByteArrayOutputStream baout = new ByteArrayOutputStream();
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, baout);
            audioInputStream.close();
            baout.close();
            data = baout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public void toWav(byte[] totalByteArray) {
        try{
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLineTemp = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
            sourceDataLineTemp.open();
            sourceDataLineTemp.start();
            ByteArrayInputStream bais = new ByteArrayInputStream(totalByteArray);
            long length = (long)(totalByteArray.length / audioFormat.getFrameSize());
            AudioInputStream audioInputStreamTemp = new AudioInputStream(bais, audioFormat, length);
            File fileOut = new File("transmitted.wav");
            AudioFileFormat.Type fileType = AudioSystem.getAudioFileFormat(audioFile).getType();
            if (AudioSystem.isFileTypeSupported(fileType,
                    audioInputStreamTemp)) {
                System.out.println("Trying to write");
                AudioSystem.write(audioInputStreamTemp, fileType, fileOut);
                System.out.println("Written successfully");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        BufferedOutputStream bos = null;
        try {
            //create an object of FileOutputStream
            FileOutputStream fos = new FileOutputStream(new File("BufferedStream.wav"));

            //create an object of BufferedOutputStream
            bos = new BufferedOutputStream(fos);


            /*
             * To write byte array to file use,
             * public void write(byte[] b) method of BufferedOutputStream
             * class.
             */
            System.out.println("Writing byte array to file");

            bos.write(totalByteArray);

            System.out.println("File written");
        } catch (
                FileNotFoundException fnfe) {
            System.out.println("Specified file not found" + fnfe);
        } catch (
                IOException ioe) {
            System.out.println("Error while writing file" + ioe);
        } finally {
            if (bos != null) {
                try {

                    //flush the BufferedOutputStream
                    bos.flush();

                    //close the BufferedOutputStream
                    bos.close();

                } catch (Exception e) {
                }
            }
        }
    }
}
