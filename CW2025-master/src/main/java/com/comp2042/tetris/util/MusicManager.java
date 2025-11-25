package com.comp2042.tetris.util;

import javax.sound.sampled.*;
import java.net.URL;

public class MusicManager {
    private static MusicManager instance;
    private Clip audioClip;
    private boolean isPlaying = false;

    private MusicManager() {
        // Private constructor for singleton pattern
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void loadMusic() {

        try {

            URL musicUrl = getClass().getClassLoader().getResource("TETRIS_PHONK.wav");

            if (musicUrl == null) {
                return;
            }

            // Load the WAV file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {

        }
    }

    public void playMusic() {
        if (audioClip != null && !isPlaying) {
            audioClip.start();
            isPlaying = true;
        }
    }

    public void pauseMusic() {
        if (audioClip != null && isPlaying) {
            audioClip.stop();
            isPlaying = false;
        }
    }

    public void stopMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            isPlaying = false;
        }
    }

    public void resetMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0); // Reset to beginning
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
            isPlaying = true;
        }
    }

    public void setVolume(double volume) {
        if (audioClip != null && audioClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert linear volume to decibels
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(min, Math.min(max, dB)));
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}