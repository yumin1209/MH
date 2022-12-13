import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
	private ImageIcon bgIcon = new ImageIcon("image/background/user.png");
	private Image bgImage = bgIcon.getImage();
	
	private int score = 0;
	private int life = 100;
	public String name = null;
	
	private JLabel scoreL;
	private JLabel lifeL;
	private JLabel textL;
	private JLabel nameL;
	
	public ScorePanel(String name) {
		this.setBackground(Color.BLACK);
		this.name = name;
		setLayout(null);
		
		textL = new JLabel("SCORE  :");
		textL.setBounds(30, 10, 80, 30);
		textL.setForeground(Color.YELLOW);
		textL.setFont(new Font("Dialog", Font.PLAIN, 15));
		add(textL);
		
		//점수
		scoreL = new JLabel(Integer.toString(score));
		scoreL.setBounds(105, 10, 110, 30);
		scoreL.setForeground(Color.YELLOW);
		scoreL.setFont(new Font("Dialog", Font.PLAIN, 17));
		add(scoreL);
		
		//이름
		nameL = new JLabel(name);
		nameL.setOpaque(true);
		nameL.setBackground(Color.BLACK);
		nameL.setForeground(Color.YELLOW);
		nameL.setFont(new Font("Dialog", Font.BOLD, 22));
		nameL.setBounds(25, 210, 120, 40);
		nameL.setHorizontalAlignment(JLabel.CENTER);
		add(nameL);
				
		//생명
		lifeL = new JLabel("LIFE ("+life+"/100)");
		lifeL.setBounds(10, 70, 120, 20);
		lifeL.setForeground(Color.YELLOW);
		add(lifeL);
		
	}
	
	public void settingScore(int score, int life) {
		this.score = score;
		this.life = life;
		
		scoreL.setText(Integer.toString(score));
		lifeL.setText("LIFE ("+life+"/100)");
		repaint();
	}
	
	//생명칸 색 설정값
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
		g.setColor(Color.WHITE);
		g.drawRect(10, 45, 150, 20);
		g.setColor(Color.YELLOW);
		if(life<=50)
			g.setColor(Color.BLUE); //50보다 작아지면 파란색
		if(life<=30)
			g.setColor(Color.red); //30보다 작아지면 빨간색
		g.fillRect(11, 46, (int)(149*((double)life/100)), 19);
	}
}
