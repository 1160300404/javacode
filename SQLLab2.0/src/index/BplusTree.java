package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
public class BplusTree {
	 BplusNode root;
	 int order;
	 int height = 0;
	public BplusNode getRoot() {
		return root;
	}
	public void setRoot(BplusNode root) {
		this.root = root;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public String get(Integer key) {
		return root.get(key);
	}
	public String remove(Integer key) {
		return root.remove(key, this);
	}
	public void insertOrUpdate(Integer key, String  value) {
		root.insertOrUpdate(key, value, this);
 
	}
	public BplusTree(int order) {
		if (order < 3) {
			System.out.print("order must be greater than 2");
			System.exit(0);
		}
		this.order = order;
		root = new BplusNode (true, true);
	}
 
	// ²âÊÔ
	public static void main(String[] args) {
 
		int size = 1000000;
		int order = 100;
		BplusTree bplusTree=new BplusTree(order);
		File file=new File("./datas.txt");
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader bufferedReader=new BufferedReader(fileReader);
		    String string=bufferedReader.readLine();
			int count=0;
			while(string!=null){
				String[] strings=string.split("\t");
				bplusTree.insertOrUpdate(Integer.valueOf(strings[0]), strings[1]);
				string=bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String string = bplusTree.get(1232132134);
		System.out.println(string);
		long time1=System.currentTimeMillis();
		bplusTree.insertOrUpdate(1232132134, "qweqweqe");
		long time2=System.currentTimeMillis();
		bplusTree.get(1232132134);
		string=bplusTree.get(1232132134);
		time2=System.currentTimeMillis();
		System.out.println(string);
		bplusTree.remove(1232132134);
		string = bplusTree.get(1232132134);
		System.out.println(string);
		

	}

}
