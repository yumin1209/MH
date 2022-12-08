import java.awt.Color;
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
	private int energy = 100;
	
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	JLabel energyLabel = new JLabel("ENERGY ("+energy+"/100)");
	
	public ScorePanel() {
		this.setBackground(Color.BLACK);
		setLayout(null);
		
		JLabel textLabel = new JLabel("SCORE");
		textLabel.setSize(50, 20);
		textLabel.setLocation(10, 10);
		textLabel.setForeground(Color.green);
		add(textLabel);
		
		scoreLabel.setSize(100, 20);
		scoreLabel.setLocation(70, 10);
		scoreLabel.setForeground(Color.green);
		add(scoreLabel);
		
		energyLabel.setSize(120, 20);
		energyLabel.setLocation(10, 60);
		energyLabel.setForeground(Color.green);
		add(energyLabel);
	}
	

	protected void changeScore(int score,int energy) {
		this.score = score;
		this.energy = energy;
		if(energy<0)
			energy=0;
		scoreLabel.setText(Integer.toString(score));
		energyLabel.setText("ENERGY ("+energy+"/100)");
		repaint();
	}
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
		g.setColor(Color.red);
		g.drawRect(10, 35, 150, 20);
		g.setColor(Color.green);
		int width = (int)(149*((double)energy/100));
		g.fillRect(11, 36, width, 19);
	}
}
