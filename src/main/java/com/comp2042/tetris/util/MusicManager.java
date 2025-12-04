package com.comp2042.tetris.util;

import javax.sound.sampled.*;
import java.net.URL;

// Singleton class to manage background music
// Manages loading, playing, pausing, stopping, resetting, and volume control (BGM only)
public class MusicManager {
    private static MusicManager instance;
    private Clip audioClip;
    private boolean isPlaying = false;

    private MusicManager() {
        // Private constructor for singleton pattern
    }

    /**
     * Get the singleton instance of MusicManager.
     * Creates a new instance if one doesn't exist.
     *
     * @return the singleton MusicManager instance
     */
    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    // Load background music from resources
    public void loadMusic() {

        try {

            URL musicUrl = getClass().getClassLoader().getResource("TETRIS_PHONK.wav");

            if (musicUrl == null) {
                // Resource not found, exit silently
                return;
            }

            // Load the WAV file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            // Audio loading failed, exit silently
        }
    }

    // Starts playing music. Only starts if music is loaded and not already playing
    public void playMusic() {
        if (audioClip != null && !isPlaying) {
            audioClip.start();
            isPlaying = true;
        }
    }

    // Pause music. Keeps the current position for resuming later
    public void pauseMusic() {
        if (audioClip != null && isPlaying) {
            audioClip.stop();
            isPlaying = false;
        }
    }

    // Stops music
    public void stopMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            isPlaying = false;
        }
    }

    // Reset music to beginning and restart playback
    public void resetMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0); // Reset to beginning
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
            isPlaying = true;
        }
    }

    // set volume (0.0 to 1.0)
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

    /**
     * Check if music is currently playing.
     *
     * @return true if music is playing, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }
}