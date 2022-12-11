import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class GameFrame extends JFrame {
	private ScorePanel scorePanel;
	private MonsterPanel monsterPanel;
	private GamePanel gamePanel;
	private GameFrame gameFrame;
	
	private JToolBar toolbar;
	private JButton btnStart, btnLevel, btnSize, btnBgm, btnEasy, btnNormal, btnHard, btnApply;
	
	public GameFrame(String name) {
		gameFrame = this;
		setTitle("Monster Hunter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null); //창 위치 정중앙

		monsterPanel = new MonsterPanel();
		scorePanel = new ScorePanel(name);
		gamePanel = new GamePanel(monsterPanel, scorePanel);
		
		createToolBar();
		
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(600);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel); //게임 패널
		
		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setTopComponent(monsterPanel); //monster 패널
		pPane.setBottomComponent(scorePanel); //점수 패널
		hPane.setRightComponent(pPane);
		
		setResizable(false);
		setVisible(true);
	}
	
	//툴바
	private void createToolBar() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		
		//게임 시작 버튼
		btnStart = new JButton("START");
		btnStart.setBackground(Color.green);
		toolbar.add(btnStart);
		btnStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(btnStart.getText().equals("START")) {
					gamePanel.startGame(); //게임 실행
					btnStart.setText("STOP");
					btnStart.setBackground(Color.red);
				}
				else {
					gamePanel.gameStop(); //게임 중지
					btnStart.setText("START");
					btnStart.setBackground(Color.green);
				}
			}
		});
		// 게임 오버시 다시 시작하도록 연결
		gamePanel.btnStartLink(btnStart); 
		
		//게임 난이도 버튼
		btnLevel = new JButton("EASY");
		btnLevel.setForeground(Color.WHITE);
		btnLevel.setBackground(Color.BLACK);
		toolbar.add(btnLevel);
		btnLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//게임 난이도 Dialog
				LevelDialog levelD = new LevelDialog(gameFrame, "LEVEL");
				levelD.setVisible(true);
			}
		} );
		
		//게임 글자 크기
		btnSize = new JButton("글자 크기");
		btnSize.setForeground(Color.WHITE);
		btnSize.setBackground(Color.BLACK);
		toolbar.add(btnSize);
		btnSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//글자 사이즈 Dialog
				FontDialog fontD = new FontDialog(gameFrame, "FONT SIZE");
				fontD.setVisible(true);
			}
		});
		
		//배경 음악 버튼
		btnBgm = new JButton("MUSIC OFF");
		btnBgm.setForeground(Color.red);
		btnBgm.setBackground(Color.BLACK);
		toolbar.add(btnBgm);
		btnBgm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnBgm.getText().equals("MUSIC OFF")) {
					gamePanel.sound.stopSound("bgm"); //음악 끄기
					btnBgm.setText("MUSIC ON");
					btnBgm.setForeground(Color.GREEN);
				}
				else {
					gamePanel.sound.playSound("bgm"); //음악 켜기
					btnBgm.setText("MUSIC OFF"); 
					btnBgm.setForeground(Color.red);
				}
			}
		} );
	}
	
	//글자 크기 조절 Dialog
	private class FontDialog extends JDialog {
		private FontDialog(JFrame jf, String title) {
			super(jf, title);
			setBounds(750, 300, 300, 150);
			setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
			
			btnApply = new JButton("APPLY"); //적용 버튼
			add(btnApply);
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			
			//슬라이더 이용하여 글자 크기를 조절
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 25, gamePanel.textSize);
			slider.setPaintLabels(true);slider.setPaintTicks(true);slider.setPaintTrack(true);
			slider.setMinorTickSpacing(1);slider.setMajorTickSpacing(5);
			add(slider);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int ptsize = slider.getValue();
					gamePanel.changeFontSize(ptsize); //크기 적용
					btnSize.setText("SIZE : "+ ptsize +"pt");
				}
			});
		}
	}
	
	//난이도 설정 Dialog
	private class LevelDialog extends JDialog { 
		private LevelDialog(JFrame jf, String title) {
			super(jf, title, true); 
			setBounds(750, 300, 300, 100);
			setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
			//easy
			btnEasy = new JButton("EASY");
			btnEasy.setBackground(Color.BLACK);
			btnEasy.setForeground(Color.green);
			add(btnEasy);
			btnEasy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.switchLevel(500, 3000);  //easy는 500, 3000
					btnLevel.setText("EASY");
					btnLevel.setForeground(Color.WHITE);
					setVisible(false);
				}
				
			});
			//normal
			btnNormal = new JButton("NORMAL");
			btnNormal.setBackground(Color.BLACK);
			btnNormal.setForeground(Color.ORANGE);
			add(btnNormal);
			btnNormal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.switchLevel(350, 2000);  //normal은 350, 2000
					btnLevel.setText("NORMAL");
					btnLevel.setForeground(Color.ORANGE);
					setVisible(false);
				}
				
			});
			//hard
			btnHard = new JButton("HARD");
			btnHard.setBackground(Color.BLACK);
			btnHard.setForeground(Color.RED);
			add(btnHard);
			btnHard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.switchLevel(200, 1000);	//hard는 200, 1000
					btnLevel.setText("HARD");
					btnLevel.setForeground(Color.RED);
					setVisible(false);
				}
			});		
		}
	}
}

