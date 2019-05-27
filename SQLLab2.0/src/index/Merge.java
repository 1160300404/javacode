package index;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Merge {
   
	public static void main(String[] args) throws IOException {
        //maopao();
		//ʹ��4·�鲢
        long start,end;
        start=System.currentTimeMillis();
		for(int i=0;i<4;i++) {
			String name1="sort"+(i*4+0)+".txt";
			String name2="sort"+(i*4+1)+".txt";
			String name3="sort"+(i*4+2)+".txt";
			String name4="sort"+(i*4+3)+".txt";
			String name5="result"+i+".txt";
			fourmerge(name1,name2,name3,name4,name5,2500,62500);
		}
		end=System.currentTimeMillis();
		System.out.println("��ʱ��"+(end-start));
		String name1="result0.txt";
		String name2="result1.txt";
		String name3="result2.txt";
		String name4="result3.txt";
		String name5="result.txt";
		start=System.currentTimeMillis();
		fourmerge(name1,name2,name3,name4,name5,2500,250000);
		end=System.currentTimeMillis();
		System.out.println("��ʱ��"+(end-start));
	}
	 public static void maopao() throws IOException {
			long start,end;
			//��ʼð������
			File f = new File("data1.txt");
			FileInputStream fps = new FileInputStream(f);
			int[] key = new int[62500]; 
			String[] value = new String[62500];
			
			for(int j=0;j<16;j++) {//�ֳ���16��
				start = System.currentTimeMillis();
				File f2 = new File("sort"+j+".txt");
				FileOutputStream fop = new FileOutputStream(f2);
				for(int i=0;i<62500;i++) {//д����Ӧ����ȥ
					byte[] as = new byte[4];
					byte[] bs = new byte[12];
					fps.read(as);
					fps.read(bs);
					int a = byte4ToInt(as);
					String b = new String(bs);
					key[i] = a;
					value[i] = b;
				}
				//sort ����
				for(int i=0;i<key.length-1;i++){
					for(int m=0;m<key.length-1-i;m++){
						if(key[m]>key[m+1]){
							int temp=key[m];
							key[m]=key[m+1];
							key[m+1]=temp;
							String tmp = value[m];
							value[m] = value[m+1];
							value[m+1] = tmp;
						}
					}
				} 
				for (int i = 0; i < key.length; i++) {
					fop.write(intToByte4(key[i]));
					fop.write(value[i].getBytes());
				}
				fop.close();
				end = System.currentTimeMillis();
				System.out.println("1������������ʱ�䣺 "+(end-start)+"ms");
			}
			fps.close();
	    }
	public static void fourmerge(String name1,String name2,String name3,String name4,String name5,int mergecount,int allcount) throws IOException {
		int counts[]= {mergecount,mergecount,mergecount,mergecount};//�ĸ����鶼�Ƚ��˶�����
		int countss[]= {0,0,0,0};//�ĸ��ļ������˶���
		int count=0;//�ܹ������ݶ��˶���
		int[][] keys = new int[4][mergecount];
		String[][] values = new String[4][mergecount];
		int choice=0;//���ѡ�����������
		int choicekey;
		String choicevalue;
		File f1 = new File(name1);
		FileInputStream fps1 = new FileInputStream(f1);
		File f2 = new File(name2);
		FileInputStream fps2 = new FileInputStream(f2);
		File f3 = new File(name3);
		FileInputStream fps3 = new FileInputStream(f3);
		File f4 = new File(name4);
		FileInputStream fps4 = new FileInputStream(f4);
		File f5 = new File(name5);//���������
		FileOutputStream fops = new FileOutputStream(f5);
		FileInputStream[] fpss= {fps1,fps2,fps3,fps4};
		while(count!=4*allcount) {
			for(int j=0;j<4;j++) {
				if(counts[j]==mergecount) {
					counts[j]=0;
					for(int k=0;k<mergecount;k++) {
						byte[] as = new byte[4];
						byte[] bs = new byte[12];
						fpss[j].read(as);
						fpss[j].read(bs);
						int aa = byte4ToInt(as);
						String bb = new String(bs);
						keys[j][k]=aa;
						values[j][k]=bb;
					}
					countss[j]+=mergecount;
				}
			}
			for(int j=0;j<4;j++)//Ѱ����С��ֵ
			if(countss[j]!=allcount+mergecount)
					choice = j;
			choicekey = keys[choice][counts[choice]];
			choicevalue = values[choice][counts[choice]];
			for(int k=0;k<4;k++) {
			if(countss[k]!=allcount+mergecount) {
				if(keys[k][counts[k]]<choice) {
					choice = k;
					choicekey = keys[choice][counts[choice]];
					choicevalue = values[choice][counts[choice]];
				   }
				}
			}
			    counts[choice]++;
				fops.write(intToByte4(choicekey));
				fops.write(choicevalue.getBytes());
				count++;
			}
			fops.close();
			fps1.close();
			fps2.close();
			fps3.close();
		}
	public static byte[] intToByte4(int i) {  
        byte[] result = new byte[4];  
        result[3] = (byte) (i & 0xFF);  
        result[2] = (byte) (i >> 8 & 0xFF);  
        result[1] = (byte) (i >> 16 & 0xFF);  
        result[0] = (byte) (i >> 24 & 0xFF);  
        return result;  
    }

	public static int byte4ToInt(byte[] bytes) {  
        int b0 = bytes[0] & 0xFF;  
        int b1 = bytes[1] & 0xFF;  
        int b2 = bytes[2] & 0xFF;  
        int b3 = bytes[3] & 0xFF;  
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;  
    }

}
