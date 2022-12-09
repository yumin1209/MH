import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private ImageIcon icon = new ImageIcon("image/background/main.jpg");
	private Image image = icon.getImage();
	private JTextField textField;
	private JButton btnStart;
	
	public MainFrame() {
		setTitle("Monster Hunter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setContentPane(new MainPanel());
		
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null); //창 위치 정중앙
		
		repaint();
		setVisible(true);
	}
	
	private class MainPanel extends JPanel {
		public MainPanel() {
			setLayout(null);
		
			JLabel label = new JLabel("ID");
			label.setBounds(280,360, 60, 40);
			label.setFont(new Font("GOTHIC",Font.BOLD,20));
			label.setForeground(Color.WHITE);
			add(label);
			
			// 사용자 ID 입력받음
			textField = new JTextField("");
			textField.setBounds(310,360,170,40);
			textField.setHorizontalAlignment(JTextField.CENTER);
			textField.setForeground(Color.WHITE);
			textField.setBackground(Color.black);
			add(textField);
			
			//시작 버튼
			btnStart = new JButton("START");
			btnStart.setBounds(350,420,80,20);
			btnStart.setForeground(Color.WHITE);
			btnStart.setBackground(Color.black);
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = textField.getText(); 
					//게임 시작하면 GameFrame 시작
					System.out.println("---시작 버튼 누름");
					new GameFrame(name);
					dispose(); //창 사라짐
				}
			});
			add(btnStart);
		
		}
		
		//배경 이미지
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
