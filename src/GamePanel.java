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
	private JTextField inputText = new JTextField(30); //입력칸 크기
	public GameGroundPanel gameGroundPanel;
	private MonsterPanel monsterPanel;
	private ScorePanel scorePanel;
	
	private int rainSpeed = 500; //떨어지는 속도
	private int makeWordSpeed = 3000; //단어 생성 딜레이
	
	public int textSize = 17; //글자 크기
	private int score; //점수
	private int life; //생명
	
	public Word textWord = new Word(); //단어장
	private Vector<WordLabel> wordLabelV = new Vector<>(); //단어
	private Rank ranking = new Rank(); //사용자 랭킹
	public Sound sound = new Sound(); //음향
	
	private JButton btnStart = null;
	protected void btnStartLink(JButton btnStart) {
		this.btnStart = btnStart;
	}
	
	protected boolean bonusScore = false;  // 보너스 점수
	
	private ImageIcon bgIcon = new ImageIcon("image/background/background.jpg");
	private Image bgImage = bgIcon.getImage();
		
	private class WordLabel extends JLabel {
		private WordLabel wordLabel = null;
		private WordThread wordthread = null;
		private int x; 
		private int y;
		protected boolean bonusWord = false;
		
		public WordLabel(String word) {
			super(word);
			x = (int)(Math.random()*(gameGroundPanel.getWidth()-(textSize*5+40)));
			y = 0;
			setForeground(Color.GREEN);
			setFont(new Font("GOTHIC",Font.PLAIN,textSize));
			setSize(300,50);
			
			
			int r = (int)(Math.random()*5); //추가 점수 획득할 수 있는 레이블 생성
			if(r==0) {
				bonusWord = true;
				setForeground(Color.RED);
			}
			
			//단어 스레드 시작
			wordLabel = this;
			wordthread = new WordThread(); 
			wordthread.start();
		}
		
		//단어 스레드
		private class WordThread extends Thread {
		
			public void run() {
				while(true) {
					y += 5; //5씩 떨어짐
					setLocation(x,y);
					try {
						sleep(rainSpeed); //떨어지는 속도
					} catch (InterruptedException e) {
						return;
					}
					//바닥에 닿으면 멈춤
					if (y >= gameGroundPanel.getHeight() - 20)
						break;
				}
				
				life -= 5; //바닥에 닿으면 5씩 생명이 깎임
				scorePanel.settingScore(score, life); //점수 변경 적용
				
				if(life < 50) 
					monsterPanel.changeEmotion("danger"); //괴물의 표정이 바뀜

				if(life <= 0) 
					gameOver(); //게임 종료
				
				//단어label 지움
				wordLabelV.remove(wordLabel);
				gameGroundPanel.remove(wordLabel);
				gameGroundPanel.repaint();
			}
		}
	}
	
	public GamePanel(MonsterPanel monsterPanel, ScorePanel scorePanel) {
		this.scorePanel = scorePanel;
		this.monsterPanel = monsterPanel;
		
		gameGroundPanel = new GameGroundPanel();
		setLayout(new BorderLayout());
		
		add(gameGroundPanel, BorderLayout.CENTER); //게임 화면
		add(new InputPanel(), BorderLayout.SOUTH); //입력칸 화면
		
		//음향 시작
		sound.playSound("bgm");
	}
	
	//게임 난이도 변경 적용
	public void switchLevel(int rainSpeed, int makeWordSpeed) {
		this.rainSpeed = rainSpeed;
		this.makeWordSpeed = makeWordSpeed;
		gameGroundPanel.gameThreadEnd();
		gameGroundPanel.repaint();
		gameGroundPanel.gameThreadStart();
		score =0;
		life= 100;
		scorePanel.settingScore(score, life);
	}
	
	//글자 크기 변경 적용
	public void changeFontSize(int ptsize) {
		this.textSize = ptsize;
	}
	
	//gameGroundPanel 시작
	public void gameStart() {
		inputText.requestFocus();
		gameGroundPanel.repaint();
		gameGroundPanel.gameThreadStart();
		
		monsterPanel.changeEmotion("normal");
		life = 100;

		score = 0;
		life = 100;
		scorePanel.settingScore(score, life); //점수 적용
		
		//버튼 음향
		sound.playSound("button");
	}
	
	//gameGroundPanel 중지
	public void gameStop() {
		gameGroundPanel.gameThreadEnd();
		gameGroundPanel.repaint();
		
		//버튼 음향
		sound.playSound("button");
	}
	
	//게임 종료
	public void gameOver() {
		btnStart.setText("START");
		gameGroundPanel.gameThreadEnd();
		gameGroundPanel.repaint();
		
		monsterPanel.changeEmotion("gameover");

		//랭킹 저장
		ranking.rankSave(scorePanel.name, score);
		
		//게임 종료창과 함께 랭킹 보여줌
		RankDialog gameoverdialog = new RankDialog((JFrame)getTopLevelAncestor(), "GAME OVER");
		gameoverdialog.getContentPane().setBackground(Color.black);
		gameoverdialog.setVisible(true);
		
		//게임종료 음향
		sound.playSound("gameover");
	}
	
	//게임 종료 Dialog
	private class RankDialog extends JDialog {
		private RankDialog(JFrame jf, String title) {
			super(jf, title, false);
			setBounds(580, 250, 340, 390);
			setLocationRelativeTo(null);
			setLayout(null);
			
			JLabel golabel = new JLabel("GAME OVER");
			golabel.setBounds(40, 20, 300, 40);
			golabel.setFont(new Font("GOTHIC",Font.BOLD,40));
			golabel.setForeground(Color.WHITE);
			add(golabel);
			
			JLabel sclabel = new JLabel(scorePanel.name + " : " + score);
			sclabel.setBounds(70, 60, 200, 40);
			sclabel.setFont(new Font("GOTHIC",Font.BOLD,30));
			sclabel.setForeground(Color.WHITE);
			add(sclabel);
			
			//확인 버튼
			JButton btnCheck = new JButton("확인");
			btnCheck.setBounds(130, 320, 60, 20);
			add(btnCheck);
			
			btnCheck.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			
			//랭킹 화면에 출력
			for(int i=0; i<10; i++) {
				String userName = ranking.getUser(i).name;
				int score = ranking.getUser(i).score;
				if(i!=9)
					sclabel = new JLabel("(" + (i+1) + ")   " + userName + " " + score);
				else
					sclabel = new JLabel("(" + (i+1) + ") " + userName + " " + score);
				sclabel.setForeground(Color.WHITE);
				sclabel.setFont(new Font("GOTHIC",Font.BOLD,20));
				sclabel.setBounds(80, 100+20*i, 200, 20);
				add(sclabel);
			}
			
		}
	}
	
	//단어 떨어지는 게임화면
	class GameGroundPanel extends JPanel {
		
		public GameGroundPanel() {
			setLayout(null);
		}
		
		makeWordThread makewordthread; //게임 스레드
		
		//스레드 시작
		public void gameThreadStart() {
			makewordthread = new makeWordThread();
			makewordthread.start();
		}
		
		//스레드 종료
		public void gameThreadEnd() {
			for(int i=0; i<wordLabelV.size(); i++) 
				wordLabelV.get(i).wordthread.interrupt();
			
			wordLabelV.clear();
			gameGroundPanel.removeAll();
			makewordthread.interrupt();
		}
		
		//스레드 생성
		private class makeWordThread extends Thread {
			public void run() {
				while(true) {
					WordLabel wordLabel = new WordLabel(textWord.getWord());
					wordLabelV.add(wordLabel);
					add(wordLabel);
					try {
						sleep(makeWordSpeed); //생성 속도
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		} 
		
		//배경 이미지
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
			if(btnStart.getText().equals("START")) {
				g.setColor(Color.green);
				g.drawString("게임을 플레이하려면 START 버튼을 누르세요", 180, 30);
			}
		}
	}
	
	//정답 맞추는 입력칸
	class InputPanel extends JPanel {
		public InputPanel() {
			add(inputText);
			setLayout(new FlowLayout());
			setBackground(Color.BLACK);
			
			inputText.addActionListener(new ActionListener() {
				//입력한 단어가 벡터에 있는지 확인하고 삭제
				public boolean checkInput(String text) {
					for(int i=0;i<wordLabelV.size();i++) {
						//일치하면 화면에서 단어 삭제
						if(text.equals(wordLabelV.get(i).getText())) { 
							gameGroundPanel.remove(wordLabelV.get(i));
							gameGroundPanel.repaint();
							
							if(wordLabelV.get(i).bonusWord == true) { //빨간색 단어를 맞추면 50점 추가 득점, 체력 10 회복
								score += 40;
								sound.playSound("bonus");
								if(life<=90)
									life += 10;
								if(life == 95)
									life += 5;
								
							}
							
							wordLabelV.get(i).wordthread.interrupt();
							wordLabelV.remove(i);
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
							scorePanel.settingScore(score, life);
							if(life >= 50) 
								monsterPanel.changeEmotion("normal"); 
							monsterPanel.changeEmotion("correct");

							//정답 음향
							sound.playSound("correct");
						}
					
					
						else { //틀리면 생명 감소
							life -= 5;
							scorePanel.settingScore(score, life);
							if(life <= 0) {
								gameOver();
								return;
							}
							if(life < 50)
								monsterPanel.changeEmotion("danger"); // 체력이 낮으면 위험 표정
							monsterPanel.changeEmotion("wrong"); // 실패 시 표정

							
							//오답 음향
							sound.playSound("wrong");
						}
						input.setText(""); //입력창 초기화
					}
				}
			});
		}
		
	}
	
}
