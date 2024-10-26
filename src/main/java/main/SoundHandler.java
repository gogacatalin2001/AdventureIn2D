package main;

import lombok.Getter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundHandler {
    public static final String THEME_SONG = "blue_boy_adventure";
    public static final String COIN_SOUND = "coin";
    public static final String POWER_UP_SOUND = "power_up";
    public static final String UNLOCK_SOUND = "unlock";
    public static final String FANFARE_SOUND = "fanfare";
    public static final String HIT_MONSTER_SOUND = "hit_monster";
    public static final String RECEIVE_DAMAGE_SOUND = "receive_damage";
    public static final String WEAPON_SWING_SOUND = "weapon_swing";
    private final Map<String, URL> sounds = new HashMap<>();
    private Clip clip;

    public SoundHandler() {
        sounds.put(THEME_SONG, getClass().getResource("/sounds/BlueBoyAdventure.wav"));
        sounds.put(COIN_SOUND, getClass().getResource("/sounds/coin.wav"));
        sounds.put(POWER_UP_SOUND, getClass().getResource("/sounds/powerup.wav"));
        sounds.put(UNLOCK_SOUND, getClass().getResource("/sounds/unlock.wav"));
        sounds.put(FANFARE_SOUND, getClass().getResource("/sounds/fanfare.wav"));
        sounds.put(HIT_MONSTER_SOUND, getClass().getResource("/sounds/hit_monster.wav"));
        sounds.put(RECEIVE_DAMAGE_SOUND, getClass().getResource("/sounds/receive_damage.wav"));
        sounds.put(WEAPON_SWING_SOUND, getClass().getResource("/sounds/swing_weapon.wav"));
    }

    public void setFile(final String sound) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sounds.get(sound));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

}
