import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MonsterPanel extends JPanel {
	Vector<Image> imageV = new Vector<>(); //이미지들을 담는 벡터
	Image currentImg = null;
	EmotionThread emotionThread = null;

	public MonsterPanel() {
		File monsterF = new File("image/monster");
		File[] monsterFiles = monsterF.listFiles();
		
		imageV.clear();
		
		//파일에서 이미지 가져와서 벡터에 넣음
		for(int i=0; i<monsterFiles.length; i++) 
			imageV.add(new ImageIcon(monsterFiles[i].getPath()).getImage());
				
		currentImg = imageV.get(1);
		emotionThread = new EmotionThread();
		emotionThread.start();
	}

	//표정을 변경
	public void changeEmotion(String text) {
		if(text.equals("correct")) {  //0
			emotionThread.correct = true; 
		}else if(text.equals("normal")) { //1
			emotionThread.normal = true; 
		}else if(text.equals("wrong")) { //2
			emotionThread.wrong = true; 
		}
	}
	
	//표정 스레드
	private class EmotionThread extends Thread {
		public boolean correct = false;
		public boolean normal = false;
		public boolean wrong = false;
		
		public void run() {
			while(true) {
				if(imageV.size()==3) { //평소표정
					currentImg = imageV.get(1);
					repaint();
				}
				if(correct==true) { //정답일때
					currentImg = imageV.get(0);
					repaint();
					
					try {
						sleep(500); //0.5초
					} catch (InterruptedException e) {
						return;
					}
					
					correct=false;
				}
				else if(wrong==true) { //틀렸을때
					currentImg = imageV.get(2);
					repaint();
					
					try {
						sleep(500); //0.5초
					} catch (InterruptedException e) {
						return;
					}
					
					wrong=false;
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentImg, 0, 0, getWidth(), getHeight(), null);
	}
		
	
}
