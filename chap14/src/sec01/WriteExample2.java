package sec01;

import java.io.*;

public class WriteExample2 {

	public static void main(String[] args)  throws Exception{
		// 문자기반의 출력 스트림 예제
		Writer writer = new FileWriter("./test1.text1");
//		Writer writer = new FileWriter("./test1.text2");
//		Writer writer = new FileWriter("./test1.text3");
//		Writer writer = new FileWriter("./test1.text4");
		
		char a = 'A';
		char b = 'B';
		char c = 'C';
//		
		writer.write(a);
		writer.write(b);
		writer.write(c);
		
//		char[] array = { 'A', 'B', 'C'};
//		writer.write(array);
		
//		char[] array = {'A', 'B', 'C', 'D', 'E'};
//		
//		writer.write(array, 1, 3);
		
//		String str = "ABC";
//		writer.write(str);
		
		writer.flush();
		writer.close();
		
		System.out.println("프로그램 종료");
		
	}

}
