import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

//몬스터의 남은 체력과 점수를 보여주는 패널
public class ScorePanel extends JPanel {
	ImageIcon bgIcon = new ImageIcon("image/background/user.png");
	Image bgImage = bgIcon.getImage();
	
	private int score = 0;
	private int life = 100;
	protected String name = null;
	
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel lifeLabel = new JLabel("LIFE ("+life+"/100)");
	private JLabel textLabel;
	private JLabel nameLabel;
	
	public ScorePanel(String name) {
		this.setBackground(Color.BLACK);
		this.name = name;
		setLayout(null);
		
		nameLabel = new JLabel(name);
		nameLabel.setOpaque(true);
		nameLabel.setBackground(Color.BLACK);
		nameLabel.setForeground(Color.GREEN);
		nameLabel.setFont(new Font("GOTHIC",Font.BOLD,15));
		nameLabel.setBounds(25, 270, 120, 20);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		add(nameLabel);
		
		textLabel = new JLabel("SCORE");
		textLabel.setBounds(10, 10, 50, 20);
		textLabel.setForeground(Color.green);
		add(textLabel);
		
		scoreLabel.setBounds(70, 10, 100, 20);
		scoreLabel.setForeground(Color.green);
		add(scoreLabel);
		
		lifeLabel.setBounds(10, 60, 120, 20);
		lifeLabel.setForeground(Color.green);
		add(lifeLabel);
	}
	
	
	protected void changeScore(int score,int life) {
		this.score = score;
		this.life = life;
		if(life<0)
			life=0;
		scoreLabel.setText(Integer.toString(score));
		lifeLabel.setText("LIFE ("+life+"/100)");
		repaint();
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
		g.setColor(Color.red);
		g.drawRect(10, 35, 150, 20);
		g.setColor(Color.green);
		if(life<=50)
			g.setColor(Color.yellow);
		if(life<=30)
			g.setColor(Color.red);
		int width = (int)(149*((double)life/100));
		g.fillRect(11, 36, width, 19);
	}
}
