package com.example.secondapp3;

public enum Effect {
    VOLUME, DECIMATOR, OVERDRIVE;
    static Effect getInstance(int effect){
        if (effect == 0) return VOLUME;
        if (effect == 1) return DECIMATOR;
        if (effect == 2) return  OVERDRIVE;
        else return null;
    }
}
