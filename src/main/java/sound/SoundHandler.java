package sound;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

public class SoundHandler {
    public static final String NO_SOUND = "no_sound";
    public static final String THEME_SONG = "blue_boy_adventure";
    public static final String COIN_SOUND = "coin";
    public static final String POWER_UP_SOUND = "power_up";
    public static final String UNLOCK_SOUND = "unlock";
    public static final String FANFARE_SOUND = "fanfare";
    public static final String HIT_MONSTER_SOUND = "hit_monster";
    public static final String RECEIVE_DAMAGE_SOUND = "receive_damage";
    public static final String WEAPON_SWING_SOUND = "weapon_swing";

    private final Map<String, Sound> sounds = new HashMap<>();

    public SoundHandler() {
        sounds.put(THEME_SONG, new Sound("/sounds/BlueBoyAdventure.wav"));
        sounds.put(COIN_SOUND, new Sound("/sounds/Coin.wav"));
        sounds.put(POWER_UP_SOUND, new Sound("/sounds/PowerUp.wav"));
        sounds.put(UNLOCK_SOUND, new Sound("/sounds/Unlock.wav"));
        sounds.put(FANFARE_SOUND, new Sound("/sounds/Fanfare.wav"));
        sounds.put(HIT_MONSTER_SOUND, new Sound("/sounds/hit_monster.wav"));
        sounds.put(RECEIVE_DAMAGE_SOUND, new Sound("/sounds/receive_damage.wav"));
        sounds.put(WEAPON_SWING_SOUND, new Sound("/sounds/swing_weapon.wav"));
    }

    public void playMusic(final String sound) {
//        Sound music = sounds.get(sound);
//        music.clip.start();
//        music.clip.loop(Clip.LOOP_CONTINUOUSLY);
        sounds.get(sound).loop();
    }

    public void playSoundEffect(final String sound) {
        if (!sound.equals(NO_SOUND)) {
            sounds.get(sound).playWithDebounce();
//            Sound soundEffect = sounds.get(sound);
//            if (soundEffect.playing) {
//                soundEffect.playCounter++;
//                if (soundEffect.playCounter > 25) {
//                    soundEffect.playCounter = 0;
//                    soundEffect.playing = false;
//                }
//            } else {
//                soundEffect.playing = true;
//                soundEffect.clip.start();
//            }
        }
    }

}
