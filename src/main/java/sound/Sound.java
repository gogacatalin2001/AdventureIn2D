package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.util.Objects;

public class Sound {
    private final String filePath;
    private Clip clip;
    private boolean playing = false;

    public Sound(String filePath) {
        this.filePath = filePath;
        setAudioClip();
    }

    private void setAudioClip() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(getClass().getResource(filePath)));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Cannot set sound file with location: " + filePath);
            System.err.println(e.getMessage());
        }
    }

    // Method to play sound with debounce
    public void playWithDebounce() {
        if (!playing) {
            playSound();
        }
    }

    public void loop() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && playing) {
            clip.stop();
            clip.setFramePosition(0); // Reset to start position
            playing = false;
        }
    }

    // Plays the sound
    private void playSound() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Reset to start
            clip.start();
            playing = true;

            // Listener to reset the playing flag after the sound completes
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playing = false;
                    clip.setFramePosition(0); // Reset to start position
                }
            });
        }
    }
}
