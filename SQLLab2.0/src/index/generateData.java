package index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//里面的数据项
public class generateData {
		public void start(){
			File file=new File("./datas.txt");
			FileWriter fileWriter;
			HashMap<Integer,Integer> arrayList=new HashMap<>();
			try {
				fileWriter = new FileWriter(file);
				BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
				int count=0;
				long current=0;
				while(count!=1000000){
					Random random=new Random(current);
					current++;
					int a=Math.abs(random.nextInt());
					int b=arrayList.keySet().size();
					arrayList.put(a, a);
					if(b==arrayList.keySet().size()){
						continue;
					}else{
						StringBuilder stringBuilder=new StringBuilder();
						for(int i=0;i<12;i++){
							stringBuilder.append((char)(random.nextInt(93)+33));
						}
						bufferedWriter.write(a+"\t"+stringBuilder.toString()+"\r\n");
						count++;
						System.out.println(count+"\t");
						System.out.println(a+"\t"+stringBuilder.toString());
					}
					
				}
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		public static void main(String[] args) {
			generateData generateData=new generateData();
			generateData.start();
		    
		}
}
