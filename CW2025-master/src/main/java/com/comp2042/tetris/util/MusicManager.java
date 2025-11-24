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
        System.out.println("=== Loading Background Music ===");

        try {

            URL musicUrl = getClass().getClassLoader().getResource("TETRIS_PHONK.wav");

            if (musicUrl == null) {
                System.err.println("TETRIS_PHONK.wav not found in classpath!");
                System.err.println("Make sure the file is in src/main/resources/ directory");
                return;
            }

            System.out.println("Found music file: " + musicUrl);

            // Load the WAV file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

            System.out.println("Music loaded successfully! Duration: " +
                    (audioClip.getMicrosecondLength() / 1000000) + " seconds");

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playMusic() {
        if (audioClip != null && !isPlaying) {
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
            isPlaying = true;
            System.out.println("Music started");
        } else if (audioClip == null) {
            System.err.println("Cannot play music");
        }
    }

    public void pauseMusic() {
        if (audioClip != null && isPlaying) {
            audioClip.stop();
            isPlaying = false;
            System.out.println("Music paused");
        }
    }

    public void stopMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            isPlaying = false;
            System.out.println("Music stopped");
        }
    }

    public void resetMusic() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0); // Reset to beginning
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
            isPlaying = true;
            System.out.println("Music restarted");
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