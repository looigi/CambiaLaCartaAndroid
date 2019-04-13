package com.looigi.cambiolacarta;

import android.media.AudioManager;
import android.media.SoundPool;

public class SuonAudio {
	
	public void SuonaAudio(int QualeSuono, SoundPool soundPool) {
		if (SharedObjects.getInstance().getSuonaAudio()) {
			float curVolume = SharedObjects.getInstance().getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = SharedObjects.getInstance().getAudioManager().getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float leftVolume = curVolume/maxVolume;
			float rightVolume = curVolume/maxVolume;
			int priority = 1;
			int no_loop = 0;
			float normal_playback_rate = 1f;
			soundPool.play(QualeSuono, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
		}
	}

}
