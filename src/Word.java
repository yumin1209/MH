import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

// 단어를 파일에서 추출해서 벡터에 입력
public class Word {
	private Vector<String> v = new Vector<String>();
	
	public Word() { 
		v.clear(); // 벡터를 한번 지움
		Scanner sc;
		try {
			sc = new Scanner(new FileInputStream("word/word.txt"), "utf-8");
			while(sc.hasNext()) {
				String word = sc.nextLine();
				v.add(word.trim());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//랜덤으로 단어 출제
	public String getWord() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}

