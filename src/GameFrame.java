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


//최상단 툴바, 좌측 게임 패널, 우측 몬스터 패널 + 스코어 패널로 구성
public class GameFrame extends JFrame {
	private ScorePanel scorePanel;
	private MonsterPanel monsterPanel;
	private GamePanel gamePanel;
	private GameFrame gameFrame;
	
	private JDialog FontDialog;
	private JDialog LevelDialog;
	
	private JButton btnStart, btnLevel, btnSize, btnBgm, btnEasy, btnNormal, btnHard;
	
	
	public GameFrame(String name) {
		gameFrame = this;
		setTitle("Monster Hunter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null); //창 위치 정중앙
		
		scorePanel = new ScorePanel();
		monsterPanel = new MonsterPanel(name);
		gamePanel = new GamePanel(monsterPanel, scorePanel);
		
		makeSplitPane();
		makeToolBar();
		setResizable(false);
		setVisible(true);
	}
	
	//게임패널과 몬스터패널, 스코어패널을 나눔
	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(600);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel);
		
		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setTopComponent(monsterPanel);
		pPane.setBottomComponent(scorePanel);
		hPane.setRightComponent(pPane);
	}
	
	//툴바
	private void makeToolBar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);

		tBar.setFloatable(false);

		
		//게임 시작 버튼
		btnStart = new JButton("START");
		btnStart.setBackground(Color.red);
		tBar.add(btnStart);
		btnStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JButton thisBtn = (JButton)e.getSource();
				if(thisBtn.getText().equals("START")) {
					gamePanel.startGame();
					thisBtn.setText("STOP");
				}
				else {
					gamePanel.gameStop();
					thisBtn.setText("START");
				}

			}
		});
		// 게임 오버시 다시 시작하도록 연결
		gamePanel.btnStartLink(btnStart); 
		
		//게임 난이도 버튼
		btnLevel = new JButton("EASY");
		btnLevel.setForeground(Color.WHITE);
		btnLevel.setBackground(Color.BLACK);
		tBar.add(btnLevel);
		btnLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLevel = (JButton)e.getSource();
				//게임 난이도 Dialog
				LevelDialog ld = new LevelDialog(gameFrame, "LEVEL");
				ld.setVisible(true);
			}
		} );
		
		//게임 글자 크기
		btnSize = new JButton("글자 크기");
		btnSize.setForeground(Color.WHITE);
		btnSize.setBackground(Color.BLACK);
		tBar.add(btnSize);
		btnSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSize = (JButton)e.getSource();
				//글자 사이즈 Dialog
				FontDialog fd = new FontDialog(gameFrame, "FONT SIZE");
				fd.setVisible(true);
			}
		});
		
		//배경 음악 버튼
		btnBgm = new JButton("MUSIC OFF");
		btnBgm.setForeground(Color.WHITE);
		btnBgm.setBackground(Color.BLACK);
		tBar.add(btnBgm);
		btnBgm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBgm = (JButton)e.getSource();
				if(btnBgm.getText().equals("MUSIC OFF")) {
					gamePanel.stopAudio("bgm"); //음악 끄기
					btnBgm.setText("MUSIC ON");
					btnBgm.setForeground(Color.GREEN);
				}
				else {
					gamePanel.playAudio("bgm"); //음악 켜기
					btnBgm.setText("MUSIC OFF"); 
					btnBgm.setForeground(Color.WHITE);
				}
			}

	//단어 가져오기
	private class WordAction implements ActionListener {

		public void actionPerformed(ActionEvent e) { // 파일 다이얼로그 사용, 파일 필터 적용
			JFileChooser chooser = new JFileChooser("word");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt 파일", "txt");
			chooser.setFileFilter(filter);
			int ret = chooser.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				String pathName = chooser.getSelectedFile().getPath(); 
				String fileName = chooser.getSelectedFile().getName();
				//gamePanel.textWord.changeFile(pathName);
				JButton thisBtn = (JButton)e.getSource();
				thisBtn.setText(fileName.split("\\.")[0]); //.txt 제거 후 버튼 이름 변경
			}
		}
	} 
	
	//글자 크기 조절 Dialog
	private class FontDialog extends JDialog {
		private FontDialog(JFrame jf, String title) {
			super(jf, title);
			setBounds(750, 300, 300, 150);
			setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
			
			JButton btnApply = new JButton("APPLY");
			add(btnApply);
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			
			//슬라이더 이용하여 글자 크기를 조절
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 25, gamePanel.textSize);
			slider.setPaintLabels(true);
			slider.setPaintTicks(true);
			slider.setPaintTrack(true);
			slider.setMinorTickSpacing(1);
			slider.setMajorTickSpacing(5);
			add(slider);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int pt = slider.getValue();
					gamePanel.setWordSize(pt);
					btnSize.setText("SIZE : "+ pt +"pt");
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
			
			btnEasy = new JButton("EASY");
			btnEasy.setBackground(Color.BLACK);
			btnEasy.setForeground(Color.WHITE);
			add(btnEasy);
			btnEasy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.changeLevel(500, 3000);  //easy 
					btnLevel.setText("EASY");
					btnLevel.setForeground(Color.WHITE);
					setVisible(false);
				}
				
			});
			
			btnNormal = new JButton("NORMAL");
			btnNormal.setBackground(Color.BLACK);
			btnNormal.setForeground(Color.ORANGE);
			add(btnNormal);
			btnNormal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.changeLevel(350, 2000);  //normal
					btnLevel.setText("NORMAL");
					btnLevel.setForeground(Color.ORANGE);
					setVisible(false);
				}
				
			});
			
			btnHard = new JButton("HARD");
			btnHard.setBackground(Color.BLACK);
			btnHard.setForeground(Color.RED);
			add(btnHard);
			btnHard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gamePanel.changeLevel(200, 1000);	//hard
					btnLevel.setText("HARD");
					btnLevel.setForeground(Color.RED);
					setVisible(false);
				}
			});		
		}
	}
}

