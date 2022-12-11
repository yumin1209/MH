import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// 오디오를 송출하는 클래스
public class Sound {
	private Clip bgm;
	private Clip correct;
	private Clip incorrect;
	private Clip button;
	private Clip gameover;
	
	public Sound() {
		//배경음
		try {
			bgm = AudioSystem.getClip();
			File bgmFile = new File("sound/bgm.wav");
			AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(bgmFile);
			bgm.open(audioStream1);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//정답
		try {
			correct = AudioSystem.getClip();
			File correctFile = new File("sound/correct.wav");
			AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(correctFile);
			correct.open(audioStream2);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//오답
		try {
			incorrect = AudioSystem.getClip();
			File incorrectFile = new File("sound/wrong.wav");
			AudioInputStream audioStream3 = AudioSystem.getAudioInputStream(incorrectFile);
			incorrect.open(audioStream3);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//버튼
		try {
			button = AudioSystem.getClip();
			File buttonFile = new File("sound/button.wav");
			AudioInputStream audioStream4 = AudioSystem.getAudioInputStream(buttonFile);
			button.open(audioStream4);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//게임종료
		try {
			gameover = AudioSystem.getClip();
			File gameoverFile = new File("sound/gameover.wav");
			AudioInputStream audioStream5 = AudioSystem.getAudioInputStream(gameoverFile);
			gameover.open(audioStream5);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//음향 시작
	public void playSound(String name) {
		if(name.equals("bgm")) {
			bgm.setFramePosition(0); 
			bgm.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if(name.equals("correct")) {
			correct.setFramePosition(0); 
			correct.start();
		}
		else if(name.equals("wrong")) {
			incorrect.setFramePosition(0); 
			incorrect.start();
		}
		else if(name.equals("button")) {
			button.setFramePosition(0); 
			button.start();
		}
		else if(name.equals("gameover")) {
			gameover.setFramePosition(0); 
			gameover.start();
		}
	}
	
	//음향 중지
	public void stopSound(String name) {
		bgm.stop(); 
	}
}

