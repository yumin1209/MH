import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


//게임 패널은 몬스터 패널과 스코어 패널을 관리함

public class GamePanel extends JPanel {
	private JTextField inputText = new JTextField(30);
	private MonsterPanel monsterPanel;
	private ScorePanel scorePanel;
	protected GameGroundPanel gameGroundPanel;
	private int score; // 점수
	private int life; // 생명
	protected int textSize = 15; // 글자 크기
	private int fallDelay = 500; // 낙하 딜레이score
	private int createDelay = 3000; // 생성 딜레이
	
	private Clip clip; //배경 음악
	private Clip btnClip; 
	private Clip correctClip;
	private Clip wrongClip;
	private Clip gameoverClip;
	
	
	// 게임 오버시 다시 시작하도록 start
	private JButton btnStart = null;
	protected void btnStartLink(JButton btnStart) {
		this.btnStart = btnStart;
	}
	
	private UserRanking userRanking = new UserRanking(); // 유저 랭킹 벡터 생성 
	
	protected Word textWord = new Word(); // 단어장 벡터 생성
	private Vector<WordLabel> wordLabelVector = new Vector<>(); // 단어 벡터
	
	private class WordLabel extends JLabel {
		protected int x; 
		protected int y;
		private WordLabel thisLabel = null;
		private WordThread th = null;
		protected boolean blindLabel = false;
		
		public WordLabel(String word) {
			super(word);
			x = (int)(Math.random()*(gameGroundPanel.getWidth()-(textSize*5+40)));
			y = 0;
			setForeground(Color.WHITE);
			setFont(new Font("GOTHIC",Font.PLAIN,textSize));
			setSize(300,50);
			
			thisLabel = this;
			th = new WordThread(); //단어 스레드 시작
			th.start();
		}
		
		//단어 스레드
		private class WordThread extends Thread {
			public void run() {
				while(true) {
					y+=5;
					setLocation(x,y);
					try {
						sleep(fallDelay);
					}catch (InterruptedException e){
						return;
					}
					if(y>=gameGroundPanel.getHeight()-20) //바닥에 닿으면 멈춤
						break;
				}
				life-=10; //10씩 생명이 깎임
				scorePanel.changeScore(score, life);
				if(life<50) 
					monsterPanel.changeExpression("danger"); //괴물의 표정이 바뀜
				if(life<=0) gameOver(); //게임 종료
				wordLabelVector.remove(thisLabel);
				gameGroundPanel.remove(thisLabel);
				gameGroundPanel.repaint();
			}
		}
	}
	
	
	public GamePanel(MonsterPanel monsterPanel, ScorePanel scorePanel) {
		this.scorePanel = scorePanel;
		this.monsterPanel = monsterPanel;
		score = 0;
		life = 100;
		setLayout(new BorderLayout());
		gameGroundPanel = new GameGroundPanel();
		add(gameGroundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);  
		
		//음향 시작
		loadAudio();
		playAudio("bgm");
	}
	
	//게임 난이도 변경 적용
	protected void changeLevel(int fallDelay, int createDelay) {
		this.fallDelay = fallDelay;
		this.createDelay = createDelay;
	}
	
	//글자 크기 변경 적용
	protected void setWordSize(int pt) {
		this.textSize = pt;
	}
	
	
	//게임 시작
	public void startGame() {
		gameGroundPanel.repaint();
		gameGroundPanel.start();
		inputText.requestFocus();
		monsterPanel.changeExpression("reset");
		monsterPanel.changeExpression("normal");
		score = 0;
		life = 100;
		scorePanel.changeScore(score, life);
		//playAudio("button"); //효과음
	}
	
	//게임 종료
	public void gameOver() {
		monsterPanel.changeExpression("gameover");
		gameGroundPanel.end();
		gameGroundPanel.repaint();
		btnStart.setText("START");
		
		//랭킹 저장
		userRanking.saveRanking(monsterPanel.name, score);
		
		//playAudio("gameover"); //효과음
		
		//게임 종료창과 함께 랭킹 보여줌
		GameOverDialog dialog = new GameOverDialog((JFrame)getTopLevelAncestor(), "GAME OVER");
		dialog.getContentPane().setBackground(Color.black);
		dialog.setVisible(true);
	}
	
	//주석 수정 해야행
	//게임 오버시 점수 및 랭킹 출력 다이얼로그
	private class GameOverDialog extends JDialog {
		private GameOverDialog(JFrame frame, String title) {
			super(frame, title, false);
			setBounds(770,270, 340,390);
			setLayout(null);
			
			JLabel label = new JLabel("GAME OVER");
			label.setForeground(Color.WHITE);
			label.setFont(new Font("GOTHIC",Font.BOLD,40));
			setBounds(40,20,300,40);
			add(label);
			
			label = new JLabel(monsterPanel.name+" : "+score);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("GOTHIC",Font.BOLD,30));
			setBounds(70,60,200,40);
			add(label);
			
			for(int i=0;i<10;i++) {
				String username = userRanking.getUser(i).name;
				int score = userRanking.getUser(i).score;
				if(i!=9)
					label = new JLabel(i+1+".   "+username+" "+score);
				else
					label = new JLabel(i+1+". "+username+" "+score);
				label.setForeground(Color.WHITE);
				label.setFont(new Font("GOTHIC",Font.BOLD,20));
				label.setSize(200,20);
				label.setLocation(80,100+20*i);
				add(label);
			}
			
			JButton btnCheck = new JButton("확인");
			btnCheck.setBounds(130,320,60,20);
			add(btnCheck);
			
			btnCheck.addActionListener(new GameOverDialogAction());
		}
		
		//다이얼로그에서 확인 버튼을 누르면 체크 박스 선택된 값 게임 패널로 전달
		private class GameOverDialogAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		}
		
	}
	
	//게임 중지
	public void gameStop() {
		gameGroundPanel.end();
		gameGroundPanel.repaint();
		//playAudio("button");
	}
	
	
	
	//단어가 떨어지는 패널
	class GameGroundPanel extends JPanel {
		ImageIcon bgIcon = new ImageIcon("image/background/background.jpg");
		Image bgImage = bgIcon.getImage();
		
		GameThread gameThread; //게임 스레드
		
		public GameGroundPanel() {
			setLayout(null);
		}
		
		//스레드 시작
		public void start() {
			gameThread = new GameThread();
			gameThread.start();
		}
		
		//스레드 종료
		public void end() {
			for(int i=0;i<wordLabelVector.size();i++) 
				wordLabelVector.get(i).th.interrupt();
			wordLabelVector.clear();
			gameGroundPanel.removeAll();
			gameThread.interrupt();
			
		}
		
		//스레드 생성
		private class GameThread extends Thread {
			public void run() {
				while(true) {
					WordLabel wordLabel = new WordLabel(textWord.getWord());
					wordLabelVector.add(wordLabel);
					add(wordLabel);
					try {sleep(createDelay);} catch (InterruptedException e) {return;}
				}
			}
		} 
		
		
		//배경 이미지
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
		}
	}
	
	
	//정답 맞추는 입력칸
	class InputPanel extends JPanel {
		
		public InputPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.BLACK);
			add(inputText);
			
			inputText.addActionListener(new InputActionListener());
		}
		
		private class InputActionListener implements ActionListener {
			//입력한 단어가 벡터에 있는지 확인하고 삭제
			public boolean checkInput(String text) {
				for(int i=0;i<wordLabelVector.size();i++) {
					if(text.equals(wordLabelVector.get(i).getText())) {
						gameGroundPanel.remove(wordLabelVector.get(i));
						gameGroundPanel.repaint();
						
						wordLabelVector.get(i).th.interrupt();
						wordLabelVector.remove(i);
						return true;
					}
				}
				return false;
			}
			public void actionPerformed(ActionEvent e) {
				if(btnStart.getText().equals("STOP")) { 
					JTextField input = (JTextField)e.getSource();
					if(checkInput(input.getText())) { //정답 -> 점수, 생명 증가
						score += 10;
						if(life!=100) 
							life+=5;
						scorePanel.changeScore(score, life);
						if(life>=50) 
							monsterPanel.changeExpression("normal"); 
						monsterPanel.changeExpression("correct");
						
						//playAudio("correct"); //효과음
						//correctClip.start();
						//correctClip.setFramePosition(0);
					}
					else { //틀리면 생명 감소
						life-=10;
						scorePanel.changeScore(score, life);
						if(life<=0) {
							gameOver();
							return;
						}
						if(life<50) 
							monsterPanel.changeExpression("danger"); 
						monsterPanel.changeExpression("wrong"); 
						
						//playAudio("wrong");  //효과음
					}
					input.setText(""); //입력칸 초기화
				}
			}
		}
	}
	//음향
	private void loadAudio() {
		try {
			clip = AudioSystem.getClip();
			File audioFile1 = new File("audio/game4.wav");
			AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(audioFile1);
			clip.open(audioStream1);
			
			btnClip = AudioSystem.getClip();
			File audioFile2 = new File("audio/btn.wav");
			AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(audioFile2);
			btnClip.open(audioStream2);
			
			correctClip = AudioSystem.getClip();
			File audioFile3 = new File("audio/correct.wav");
			AudioInputStream audioStream3 = AudioSystem.getAudioInputStream(audioFile2);
			btnClip.open(audioStream3);
			
			wrongClip = AudioSystem.getClip();
			File audioFile4 = new File("audio/incorrect.wav");
			AudioInputStream audioStream4 = AudioSystem.getAudioInputStream(audioFile2);
			btnClip.open(audioStream4);
			
			gameoverClip = AudioSystem.getClip();
			File audioFile5 = new File("audio/gameover.wav");
			AudioInputStream audioStream5 = AudioSystem.getAudioInputStream(audioFile2);
			btnClip.open(audioStream5);
			
		} catch (Exception e) {
			return;
		}
	}
	
	//음향 재생
	public void playAudio(String name) {
		switch(name) {
		case "bgm": clip.loop(Clip.LOOP_CONTINUOUSLY); break;
		case "correct": correctClip.setFramePosition(0); correctClip.start(); break;
		case "wrong": wrongClip.setFramePosition(0); wrongClip.start(); break;
		case "button": btnClip.setFramePosition(0); btnClip.start(); break;
		case "gameover": gameoverClip.setFramePosition(0); gameoverClip.start(); break;
		}
	}
		
	//음향 중지
	public void stopAudio(String name) {
		switch(name) {
		case "bgm": clip.stop(); break;
		}
	}
	
	
}
