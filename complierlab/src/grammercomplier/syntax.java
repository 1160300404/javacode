package grammercomplier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class syntax {
	private ArrayList<String> token;//��������
	private ArrayList<String> kind;//������
	private ArrayList<String> linecount;//������
	private HashMap<String, HashMap<String,ArrayList<String>>> selecttable;//������select��
	private ArrayList<String> statestack;//����ջ
	private ArrayList<Integer> tap;//��ʾ������
	private generateTable gTable;//���ɳ�ʼfirst,follow,select
	private JTable jTable;//��Ŵ�����Ϣ
	private ArrayList<String> ouput;//��������Ϣ
	private String[] directcompare={ "integer", "real",  "int","char",
	        ";", "else", "then", "or", "and", "not", "relop",  ",","(",")","[","]","{","}",
	        "true", "false", "call",  "do", "if", "while","proc","record","+", "-", "*", "=", "<", ">", "<=",  
	         "==", "!=", ">=" ,"++","--","for"};
		public syntax(ArrayList<String> token,ArrayList<String> kind,ArrayList<String> linecount,JTable jTable){
			this.token=new ArrayList<>();
			this.kind=new ArrayList<>();
			this.selecttable=new HashMap<>();
			this.statestack=new ArrayList<>();
			this.tap=new ArrayList<>();
			this.gTable=new generateTable();
			this.jTable=new JTable();
			this.linecount=new ArrayList<>();
			this.ouput=new ArrayList<>();
			this.token=token;
			this.kind=kind;
			this.jTable=jTable;
			this.linecount=linecount;
			getselecttable();
		}
		//����Ԥ�������
		private void getselecttable(){
			//��ǰ����
			for(int i=0;i<gTable.nonterminals.size();i++){
				HashMap<String,ArrayList<String>> hashMap=new HashMap<>();
				if(!selecttable.containsKey(gTable.nonterminals.get(i)))
					selecttable.put(gTable.nonterminals.get(i), hashMap);
			}
			//��ȡÿһ������ʽ���µ�Ԥ��������ӵ���Ӧ���ս����hashmap��
			for(int i=0;i<gTable.productions.size();i++){
				Production production=gTable.productions.get(i);
				HashMap<String, ArrayList<String>> hashMap=selecttable.get(production.getleft());
				for(int j=0;j<production.select.size();j++){
					ArrayList<String > arrayList=new ArrayList<>();
					for(int k=0;k<production.getright().length;k++){
						arrayList.add(production.getright()[k]);
					}
					hashMap.put(production.select.get(j), arrayList);
				}
				//System.out.println(production.getleft()+selecttable.get(production.getleft()).keySet());
				selecttable.put(production.getleft(), hashMap);
			}
		}
		//���ݵõ���token���кͶ�Ӧ�����ͽ��з���
		public void parse(){
			//System.out.println(selecttable.get("P"));
			String suojin="	";
			statestack.add("Program");
			tap.add(0);
			String choice=null;
			int i=0;
			while(i<token.size()){
				//���ջ���ǿյ�
				if(statestack.isEmpty()){
					DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
					defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"���������ַ�"});
					jTable.invalidate();
					break;
				}
				//������ջ�������Ǹ��ս��
				if(gTable.terminals.contains(statestack.get(statestack.size()-1))||statestack.get(statestack.size()-1).equals("empty")){
					if(tokenKind(token.get(i),kind.get(i),statestack.get(statestack.size()-1))||statestack.get(statestack.size()-1).equals("empty")){
						if(statestack.get(statestack.size()-1).equals(token.get(i))){
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"("+linecount.get(i)+")");
							i++;
						}else if(statestack.get(statestack.size()-1).equals("empty")){
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"("+linecount.get(i)+")");
						}else{
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"  :"+token.get(i)+"("+linecount.get(i)+")");
							i++;
						}
						statestack.remove(statestack.size()-1);
						tap.remove(tap.size()-1);
					}
					else{
						DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
						defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"�ս����ƥ��"});
						statestack.remove(statestack.size()-1);//�������ս��
						jTable.invalidate();
					}
				}
				//ջ�����Ų����ս��
				else{
				HashMap<String, ArrayList<String>> hashMap=selecttable.get(statestack.get(statestack.size()-1));
				
				for (String str : hashMap.keySet()) {
					if(tokenKind(token.get(i),kind.get(i),str)){
						choice=str;
					}
				}
				ArrayList<String> states=hashMap.get(choice);//�ҵ���Ӧ�Ĳ���ʽ�Ҳ�
				if(states==null){
					DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
					defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"δ�ҵ�����ʽ"});//ֱ�Ӳ��ֻܿ�ģʽ
					i++;
					jTable.invalidate();
				}else{ 
				ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"("+linecount.get(i)+")");
				statestack.remove(statestack.size()-1);
				int aa=tap.get(tap.size()-1)+1;
				tap.remove(tap.size()-1);
				for(int j=0;j<states.size();j++){
						statestack.add(states.get(states.size()-1-j));
						tap.add(aa);
					}
				}
			}
				if(i<token.size())
				System.out.println(statestack+"  "+token.get(i));
		}
			if(!statestack.isEmpty()){
				if(!gTable.CanEmpty(statestack.get(statestack.size()-1))){
				DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
				defaultTableModel.addRow(new Object[]{statestack.get(statestack.size()-1),linecount.get(i-1),"ȱ�������ַ�"});//ֱ�Ӳ��ֻܿ�ģʽ
				jTable.invalidate();
				}
			}
			File file,file1,file2,file3;
			FileWriter fileWriter;
			BufferedWriter bufferedWriter;
			try {
				file=new File("tree.txt");
				fileWriter=new FileWriter(file);
				bufferedWriter=new BufferedWriter(fileWriter);
				for(int j=0;j<ouput.size();j++){
					bufferedWriter.write(ouput.get(j)+"\r\n");
						System.out.println(ouput.get(j));
				}
				bufferedWriter.close();
				
				file1=new File("first.txt");
				fileWriter=new FileWriter(file1);
				bufferedWriter=new BufferedWriter(fileWriter);
				for (String string : gTable.firsts.keySet()) {
					bufferedWriter.write( string+" : "+gTable.firsts.get(string)+"\r\n");
				}
				bufferedWriter.close();
				
				file2=new File("follow.txt");
				fileWriter=new FileWriter(file2);
				bufferedWriter=new BufferedWriter(fileWriter);
				for (String string : gTable.follows.keySet()) {
					bufferedWriter.write( string+" : "+gTable.follows.get(string)+"\r\n");
				}
				bufferedWriter.close();
				
				StringBuilder stringBuilder=new StringBuilder();
				file3=new File("Select.txt");
				fileWriter=new FileWriter(file3);
				bufferedWriter=new BufferedWriter(fileWriter);
				for(int j=0;j<gTable.terminals.size();j++){
					stringBuilder.append("\t\t"+gTable.terminals.get(j));
				}
				bufferedWriter.write(stringBuilder.toString()+"\r\n");
				for (int j=0;j<gTable.nonterminals.size();j++) {
					StringBuilder stringBuilder1=new StringBuilder();
					stringBuilder1.append(gTable.nonterminals.get(j)+"    ");
					for(int k=0;k<gTable.terminals.size();k++){
						if (selecttable.get(gTable.nonterminals.get(j)).get(gTable.terminals.get(k))!=null) {
							stringBuilder1.append(gTable.nonterminals.get(j)+"->"+selecttable.get(gTable.nonterminals.get(j)).get(gTable.terminals.get(k))+"\t");
						}else{
						stringBuilder1.append("null   \t");
						}
					}
					bufferedWriter.write(stringBuilder1.toString()+"\r\n");
				}
				bufferedWriter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		//�ж��Ƿ���ֱ�ӱȽ��ַ���
		private boolean candirectcompare(String str){
			for(int i=0;i<directcompare.length;i++){
				if(directcompare[i].equals(str)){
					return true;
				}
			}
			return false;
		}
		//�ж�token��Ӧ�����������
		public boolean tokenKind(String token,String kind,String terimal){
			if(candirectcompare(terimal))
			return token.equals(terimal);
			else{
				if(terimal.equals("id")){
					if(kind.equals("IDN")&&!candirectcompare(token)){
						return true;
					}
				}else if(terimal.equals("num")){
					if(kind.equals("CONSTINT")){
						return true;
					}
				}else if(terimal.equals("digit")){
					if(kind.equals("CONSTINT")||kind.equals("CONSTFLOAT")){
						return true;
					}
				}else if(terimal.equals("ch")){
					if(kind.equals("CHAR")){
						return true;
					}
				}
			}
			return false;
		}
		//�ַ�������
		public String mulString(int count,String str){
			StringBuilder stringBuilder=new StringBuilder();
			for(int i=0;i<count;i++){
				stringBuilder.append(str);
			}
			return stringBuilder.toString();
		}
}
