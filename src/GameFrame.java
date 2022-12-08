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

// 占쏙옙占쏙옙 占쏙옙체 占쏙옙占쏙옙占쏙옙
// 占쌍삼옙占� 占쏙옙占쏙옙, 占쏙옙占쏙옙 占쏙옙占쏙옙 占싻놂옙, 占쏙옙占쏙옙 占싣뱄옙타 占싻놂옙 + 占쏙옙占쌘억옙 占싻널뤄옙 占쏙옙占쏙옙
// 占쏙옙占쏙옙 占싻놂옙占쏙옙 占싣뱄옙타 占싻널곤옙 占쏙옙占쌘억옙 占싻놂옙占쏙옙 占쏙옙占쏙옙占쏙옙
public class GameFrame extends JFrame {
	private ScorePanel scorePanel;
	private AvatarPanel avatarPanel;
	private GamePanel gamePanel;
	private GameFrame gameFrame;
	
	public GameFrame(String name) {
		gameFrame = this;
		setTitle("Endless Typing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocation(600,200);
		
		scorePanel = new ScorePanel();
		avatarPanel = new AvatarPanel(name);
		gamePanel = new GamePanel(avatarPanel, scorePanel);
		
		makeSplitPane();
		makeToolBar();
		setResizable(false);
		setVisible(true);
	}
	
	//占쏙옙占쏙옙 占쌓띰옙占쏙옙 占싻널곤옙 占싣뱄옙타占싻놂옙, 占쏙옙占쌘억옙占싻놂옙占쏙옙 占쏙옙占쏙옙
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
		pPane.setTopComponent(avatarPanel);
		pPane.setBottomComponent(scorePanel);
		hPane.setRightComponent(pPane);
	}
	
	// 占쏙옙占� 占쏙옙占쌕울옙占쏙옙 7占쏙옙占쏙옙 占쏙옙튼占쏙옙 占쏙옙占쏙옙 : 占쏙옙占쏙옙 占쏙옙占쏙옙/占쏙옙占쏙옙 , 占쏙옙占싱듸옙 占쏙옙占쏙옙, 占쌤억옙 占쏙옙占쏙옙 占쏙옙占쏙옙 
	// 占쏙옙占쏙옙 占쏙옙占� 占쏙옙占쏙옙, 占싣뱄옙타 占쏙옙占쏙옙, 占쌔쏙옙트 크占쏙옙 占쏙옙占쏙옙, 占쏙옙占쏙옙占쏙옙占� 占싼깍옙/占쏙옙占쏙옙
	private void makeToolBar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);
		tBar.setFloatable(false); // 占쏙옙占쏙옙 占쏙옙占쏙옙
		
		JButton startBtn = new JButton("START");
		startBtn.setBackground(Color.red);
		tBar.add(startBtn);
		startBtn.addActionListener(new StartAction());
		// 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 STOP->START 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 占싻널곤옙 占쏙옙占쏙옙 占쏙옙튼占쏙옙 占쏙옙占쏙옙
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
	
	//占쏙옙占쏙옙 占쏙옙占쏙옙
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
	
	// 占쏙옙占싱듸옙 占쏙옙占쏙옙, 占쌤억옙 占쏙옙占싱븝옙占쏙옙 占쏙옙占쏙옙 占쌈듸옙占쏙옙 占쏙옙占쏙옙 占쌈듸옙占쏙옙 占쏙옙占쏙옙
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
	
	// 占쌤억옙 占쏙옙占쏙옙 占쏙옙占쏙옙, 占쏙옙占쏙옙 占쏙옙占싱억옙慣占� 占쏙옙占�
	private class WordAction implements ActionListener {

		public void actionPerformed(ActionEvent e) { // 占쏙옙占쏙옙 占쏙옙占싱억옙慣占� 占쏙옙占�, 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
			JFileChooser chooser = new JFileChooser("word");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt 파일", "txt");
			chooser.setFileFilter(filter);
			int ret = chooser.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				String pathName = chooser.getSelectedFile().getPath(); // 占쏙옙占쏙옙 占쏙옙罐占�
				String fileName = chooser.getSelectedFile().getName();
				gamePanel.textSource.changeFile(pathName);
				JButton thisBtn = (JButton)e.getSource();
				thisBtn.setText(fileName.split("\\.")[0]); // .txt 占쏙옙占쏙옙 占쏙옙 占쏙옙튼 占싱몌옙 占쏙옙占쏙옙
			}
		}
		
	} 
	
	
	
	
	
	// 占쏙옙占쏙옙 크占쏙옙 占쏙옙占쏙옙, 占쏙옙占쏙옙占싱댐옙占쏙옙 占쏙옙占쏙옙
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
				
				//占쏙옙占쏙옙 크占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占싱댐옙 占쏙옙占�
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

