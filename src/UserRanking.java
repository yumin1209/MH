import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

// �ܾ ���Ͽ��� �����ؼ� ���Ϳ� �Է�
public class UserRanking {
	
	protected class User {
		String name;
		int score;
		
		public User(String name, int score) {
			this.name = name;
			this.score = score;
		}
	}
	
	private Vector<User> userVector = new Vector<>();
	String filePath = null;
	
	public UserRanking() { 
		filePath = "ranking/ranking.txt";
		readRanking();
	}
	
	// ��ŷ �б�
	private void readRanking() {
		userVector.clear(); // ���͸� �ѹ� ����

		//���Ϳ� ���� ������ ����
		try {
			Scanner fScanner = new Scanner(new FileInputStream(filePath), "utf-8");
			while(fScanner.hasNext()) {
				String str = fScanner.nextLine();
				String[] strArray = str.split(" ");
				String name = strArray[0];
				int score = Integer.parseInt(strArray[1]);
				User user = new User(name, score);
				userVector.add(user);
			}
			fScanner.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//���� ��ŷ ����
	public void saveRanking(String name, int score) {
		User user = new User(name, score);
		userVector.add(user);
		
		//���� ���͸� ���� �������� ����
		Collections.sort(userVector, new Comparator<User>() {
			public int compare(User u1, User u2) {
				if(u1.score < u2.score) 
					return 1;
				else if(u1.score==u2.score)
					return 0;
				else
					return -1;
			}
		});
		
		try {
			FileWriter fout = new FileWriter(filePath);
			for(int i=0;i<userVector.size();i++) {
				fout.write(userVector.get(i).name+" "+userVector.get(i).score+"\n");
			}
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ��ŷ���� ���� ���
	public User getUser(int ranking) {
		return userVector.get(ranking);
	}
}
