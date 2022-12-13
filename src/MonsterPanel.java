import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// 이 패널에서는 아바타 이미지를 출력함
// 디렉토리 별로 아바타의 표정 5개에 해당하는 사진을 저장
// File 객체로 읽어서 벡터에 저장 후 상황에 맞게 출력함
public class MonsterPanel extends JPanel {
	Vector<Image> imageVector = new Vector<>(); //몬스터 이미지들을 담는 벡터
	String filePath = null;
	File imageDir = null;
	File[] imageFiles = null;
	Image currentImage = null;
	ExpressionThread expressionThread = null;
	
	public MonsterPanel() {
		
		setMonster("monster");
		currentImage = imageVector.get(1);
		expressionThread = new ExpressionThread();
		expressionThread.start();
	}
	
	protected void setMonster(String text) {
		if(text.equals("monster")) {
			filePath = "image/monster";
		}
		
		imageDir = new File(filePath);
		imageFiles = imageDir.listFiles();
		
		imageVector.clear(); // 벡터를 한번 비움
		
		// 디렉토리에서 이미지를 모두 가져와서 이미지 벡터 설정
		for(int i=0;i<imageFiles.length;i++) 
			imageVector.add(new ImageIcon(imageFiles[i].getPath()).getImage());
	}
	
	// 표정을 변경, 벡터에서 파일명 알파벳 순서에 의해 정해진 표정의 인덱스는 다음과 같음
	// 0:correct, 1:normal, 2:wrong
	public void changeExpression(String text) {
		switch(text) {
		case "correct" : expressionThread.correct = true; break;
		case "normal" : expressionThread.normal = true; break;
		case "wrong" : expressionThread.wrong = true; break;
		}
	}
	
	//맞추거나 틀린 표정의 경우 0.5초만 띄움
	private class ExpressionThread extends Thread {
		protected boolean correct = false;
		protected boolean normal = false;
		protected boolean wrong = false;
		
		public void run() {
			while(true) {
				if(correct==true) {
					currentImage = imageVector.get(0);
					repaint();
					try {sleep(500);} catch (InterruptedException e) {return;}
					correct=false;
				}
				else if(wrong==true) {
					currentImage = imageVector.get(2);
					repaint();
					try {sleep(500);} catch (InterruptedException e) {return;}
					wrong=false;
				}
				
				if(imageVector.size()==3) { // 이미지 로딩이 됐을때
					currentImage = imageVector.get(1);
					repaint();
				}
				
				
				try {sleep(50);} catch (InterruptedException e) {return;}
			}
		}
	}
	
	// 아바타 그리기
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);
	}
		
	
}
