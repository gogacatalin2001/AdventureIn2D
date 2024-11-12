package sound;

import lombok.EqualsAndHashCode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.util.Objects;

@EqualsAndHashCode
public class Sound {
    private final String filePath;
    private final String name;
    private Clip clip;
    private boolean playing = false;

    public Sound(String name, String filePath) {
        this.filePath = filePath;
        this.name = name;
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

    public void loop() {
        if (clip != null && !playing) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && playing) {
            clip.stop();
            clip.setFramePosition(0);
            playing = false;
        }
    }

    public void play() {
        if (clip != null && !playing) {
            clip.stop();
            clip.setFramePosition(0);
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
