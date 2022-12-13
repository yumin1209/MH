import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

//단어를 파일에서 추출해서 벡터에 입력
public class Rank {
	private Vector<UserScore> userVector = new Vector<>();
	
	public class UserScore {
		String name;
		int score;
		
		public UserScore(String name, int score) {
			this.name = name;
			this.score = score;
		}
	}

	public Rank() { 
		userVector.clear(); 
		
		try {
			Scanner fsc = new Scanner(new FileInputStream("ranking/ranking.txt"), "utf-8");
			while(fsc.hasNext()) {
				String str = fsc.nextLine();
				String[] strArray = str.split(" ");
				String name = strArray[0];
				int score = Integer.parseInt(strArray[1]);
				UserScore user = new UserScore(name, score);
				userVector.add(user); //파일에서 데이터 읽어와서 벡터에 넣음
			}
			fsc.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//유저 랭킹 저장
	public void rankSave(String name, int score) {
		UserScore user = new UserScore(name, score);
		userVector.add(user);
		
		//점수 비교해서 높은 순으로 정렬
		Collections.sort(userVector, new Comparator<UserScore>() {
			public int compare(UserScore u1, UserScore u2) {
				if(u1.score < u2.score) 
					return 1;
				else if(u1.score==u2.score)
					return 0;
				else
					return -1;
			}
		});
		
		//파일에 벡터 데이터 쓰기
		try {
			FileWriter fw = new FileWriter("ranking/ranking.txt");
			for(int i=0;i<userVector.size();i++) {
				fw.write(userVector.get(i).name+" "+userVector.get(i).score+"\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 랭킹으로 유저 얻기
	public UserScore getUser(int ranking) {
		return userVector.get(ranking);
	}
}