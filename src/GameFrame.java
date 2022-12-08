import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


//게임 전체 프레임
//최상단 툴바, 좌측 게임 패널, 우측 몬스터 패널 + 스코어 패널로 구성
//게임 패널은 몬스터 패널과 스코어 패널을 관리함
public class GameFrame extends JFrame {
	private ScorePanel scorePanel;
	private MonsterPanel monsterPanel;
	private GamePanel gamePanel;
	private GameFrame gameFrame;
	
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
	
	// 상단 툴바 : 게임시작/종료, 난이도 변경, 텍스트 크기 변경, 배경음악 켜기/끄기
	private void makeToolBar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);

		tBar.setFloatable(false);

		
		JButton startBtn = new JButton("START");
		startBtn.setBackground(Color.red);
		tBar.add(startBtn);
		startBtn.addActionListener(new StartAction());
		gamePanel.linkStartBtn(startBtn); 
		
		JButton levelBtn = new JButton("EASY");
		levelBtn.setForeground(Color.GREEN);
		levelBtn.setBackground(Color.BLACK);
		tBar.add(levelBtn);
		levelBtn.addActionListener(new LevelAction());
		
		
		JButton sizeBtn = new JButton("글자 크기");
		sizeBtn.setForeground(Color.GREEN);
		sizeBtn.setBackground(Color.BLACK);
		tBar.add(sizeBtn);
		sizeBtn.addActionListener(new SizeAction());
		
		JButton bgmBtn = new JButton("배경음악 끄기");
		bgmBtn.setForeground(Color.GREEN);
		bgmBtn.setBackground(Color.BLACK);
		tBar.add(bgmBtn);
		bgmBtn.addActionListener(new BgmAction());
	}
	
	//게임 시작
	private class StartAction implements ActionListener {
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
	} 
	
	// 난이도 선택, 단어 레이블의 낙하 속도와 생성 속도 조절
	private class LevelAction implements ActionListener {
		JButton levelBtn;
		public void actionPerformed(ActionEvent e) {
			levelBtn = (JButton)e.getSource();
			LevelDialog dialog = new LevelDialog(gameFrame, "난이도 설정");
			dialog.setVisible(true);
		}
		
		private class LevelDialog extends JDialog { 
			private LevelDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(300,100);
				setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				
				JButton easyBtn = new JButton("EASY");
				easyBtn.setBackground(Color.BLACK);
				easyBtn.setForeground(Color.GREEN);
				JButton normalBtn = new JButton("NORMAL");
				normalBtn.setBackground(Color.BLACK);
				normalBtn.setForeground(Color.ORANGE);
				JButton hardBtn = new JButton("HARD");
				hardBtn.setBackground(Color.BLACK);
				hardBtn.setForeground(Color.RED);
				add(easyBtn);
				add(normalBtn);
				add(hardBtn);
				
				LevelDialogAction levelDialogAction = new LevelDialogAction();
				
				easyBtn.addActionListener(levelDialogAction);
				normalBtn.addActionListener(levelDialogAction);
				hardBtn.addActionListener(levelDialogAction);
			}
			
			private class LevelDialogAction implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					JButton thisBtn = (JButton)e.getSource();
					switch(thisBtn.getText()) {
					case "EASY" : 
						gamePanel.changeLevel(500, 3000); 
						levelBtn.setText("EASY");
						levelBtn.setForeground(Color.GREEN);
						break;
					case "NORMAL" : 
						gamePanel.changeLevel(350, 2000); 
						levelBtn.setText("NORMAL");
						levelBtn.setForeground(Color.ORANGE);
						break;
					case "HARD" : 
						gamePanel.changeLevel(200, 1000);
						levelBtn.setText("HARD");
						levelBtn.setForeground(Color.RED);
						break;
					}
					setVisible(false);
				}
			}
		}
	} 
	
  
	private class WordAction implements ActionListener {

		public void actionPerformed(ActionEvent e) { // 파일 다이얼로그 사용, 파일 필터 적용
			JFileChooser chooser = new JFileChooser("word");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt 파일", "txt");
			chooser.setFileFilter(filter);
			int ret = chooser.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				String pathName = chooser.getSelectedFile().getPath(); 
				String fileName = chooser.getSelectedFile().getName();
				gamePanel.textSource.changeFile(pathName);
				JButton thisBtn = (JButton)e.getSource();
				thisBtn.setText(fileName.split("\\.")[0]); //.txt 제거 후 버튼 이름 변경
			}
		}
		
	} 
	
	
	
	
	
	// 글자 크기 선택, 슬라이더로 조절
	private class SizeAction implements ActionListener {
		JButton thisBtn;
		public void actionPerformed(ActionEvent e) {
			thisBtn = (JButton)e.getSource();
			SizeDialog dialog = new SizeDialog(gameFrame, "글자 크기 설정");
			dialog.setVisible(true);
		}
		
		private class SizeDialog extends JDialog { 
			private SizeDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(250,150);
				setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				
				//글자 크기 조절을 위해 슬라이더 사용
				JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 25, gamePanel.wordSize);
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
						thisBtn.setText("글자크기: "+pt+"pt");
					}
				});
				JButton okBtn = new JButton("확인");
				add(okBtn);
				okBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
			}
			
		}
	} 
	private class BgmAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton thisBtn = (JButton)e.getSource();
			if(thisBtn.getText().equals("배경음악 끄기")) {
				gamePanel.audioSource.stopAudio("bgm");
				thisBtn.setText("배경음악 켜기");
			}
			else {
				gamePanel.audioSource.playAudio("bgm");
				thisBtn.setText("배경음악 끄기");
			}
		}
	} 
}

