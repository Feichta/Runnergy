package com.ffeichta.runnergy.gui.listener;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Fabian on 09.02.2016.
 */
public class InitListener implements TextToSpeech.OnInitListener {

    private TextToSpeech tts = null;


    public InitListener(TextToSpeech tts) {
        this.tts = tts;
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.UK);
        }
    }
}
