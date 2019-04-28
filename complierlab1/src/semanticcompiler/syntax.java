package semanticcompiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.Node;

public class syntax {
	private int count=1;//地址指令号
	private int tcount=0;//临时变量t的下标
	private int parmcount=0;//参数个数
	private String procname=null;//存放过程名字
	private boolean iserror=false;
	private String lastE=null;//存放上一个E的addr
	private int offset=0;//偏移量 
	private String t=null;//将类型和宽度信息从语分析树中的B结点传递到对应于产生式C→ε的结点
	private int w=0;//同上
	private String shuzhu=new String();
	private ArrayList<String> Gstack=new ArrayList<>();
	private ArrayList<String> Fstack=new ArrayList<>();
	private ArrayList<String> numstack=new ArrayList<>();
	private HashMap<String, Object> attribute;//存放综合属性
	private HashMap<Integer,String> threeadd;//存放三地址码
	private HashMap<Integer,String> four;//存放四元式
	private HashMap<String, Integer> idparmcount;//每个过程的参数个数
	private ArrayList<String> parmlist;//调用时候的参数
	private ArrayList<Symbol> symboltable;//符号表
	private ArrayList<String> token;//输入序列
	private ArrayList<String> kind;//词类型
	private ArrayList<String> linecount;//词行数
	private HashMap<String, HashMap<String,ArrayList<String>>> selecttable;//真正的select表
	private ArrayList<Object> statestack;//符号栈
	private ArrayList<Integer> tap;//表示缩进的
	private generateTable gTable;//生成初始first,follow,select
	private JTable jTable;//存放语法错误信息
	private JTable jTable2;//存放符号表
	private JTable jTable4;//存放语义错误信息
	private JTable jTable5;//存放三地址指令和四元式
	private ArrayList<String> ouput;//存放输出信息
	private String[] directcompare={ "integer", "real",  "int","char","return",
	        ";", "else", "then", "or", "and", "not", ",","(",")","[","]","{","}",
	        "true", "false", "call",  "do", "if", "while","proc","record","+", "-", "*", "=", "<", ">", "<=",  
	         "==", "!=", ">=" ,"++","--","for"};
	private String[] opStrings={"=", "<", ">", "<=",  "==", "!="};
		public syntax(ArrayList<String> token,ArrayList<String> kind,ArrayList<String> linecount,JTable jTable,JTable jTable2,JTable jTable4,JTable jTable5){
			this.token=new ArrayList<>();
			this.kind=new ArrayList<>();
			this.selecttable=new HashMap<>();
			this.statestack=new ArrayList<>();
			this.tap=new ArrayList<>();
			this.gTable=new generateTable();
			this.jTable=new JTable();
			this.jTable2=new JTable();
			this.jTable4=new JTable();
			this.jTable5=new JTable();
			this.linecount=new ArrayList<>();
			this.ouput=new ArrayList<>();
			this.attribute=new HashMap<>();
			this.threeadd=new HashMap<>();
			this.symboltable=new ArrayList<>();
			this.idparmcount=new HashMap<>();
			this.parmlist=new ArrayList<>();
			this.four=new HashMap<>();
			this.token=token;
			this.kind=kind;
			this.jTable=jTable;
			this.jTable2=jTable2;
			this.jTable4=jTable4;
			this.jTable5=jTable5;
			this.linecount=linecount;
			getselecttable();
		}
		//创建预测分析表
		private void getselecttable(){
			//提前创建
			for(int i=0;i<gTable.nonterminals.size();i++){
				HashMap<String,ArrayList<String>> hashMap=new HashMap<>();
				if(!selecttable.containsKey(gTable.nonterminals.get(i)))
					selecttable.put(gTable.nonterminals.get(i), hashMap);
			}
			//读取每一个产生式将新的预测分析符加到对应非终结符的hashmap中
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
		//根据得到的token序列和对应的类型进行分析
		public void parse(){
			//System.out.println(selecttable.get("P"));
			String suojin="	";
			statestack.add("Program");
			tap.add(0);
			String choice=null;
			int i=0;
			while(i<token.size()||!statestack.isEmpty()){
				//如果栈里是空的
				if(statestack.isEmpty()){
					DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
					defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"多余输入字符"});
					jTable.invalidate();
					break;
				}
				if(i<token.size()){
				Symbol symbol1=new Symbol();
				symbol1.name=token.get(i);
				if(symboltable.contains(symbol1)&&getsymbol(symbol1.name).type.contains("array")){
					int line=Integer.valueOf(linecount.get(i));
					int j;
					for( j=i;j<token.size();j++){
						if(line==Integer.valueOf(linecount.get(j))){
							shuzhu=shuzhu+token.get(j);
						}else{
							break;
						}
					}
					shuzhu=shuzhu.substring(0, shuzhu.length()-1);
					threeadd.put(count, shuzhu);
					String[] strings=shuzhu.split("=");
					four.put(count, "(=,"+strings[1]+",-,"+strings[0]+")");
					count++;
					i=j;
					System.out.println(j+" "+token.get(j));
					continue;
				}
				}
				//如果这个栈顶符号是个终结符
				if(i<token.size()&&(gTable.terminals.contains(statestack.get(statestack.size()-1))||statestack.get(statestack.size()-1).equals("empty"))){
					if(tokenKind(token.get(i),kind.get(i),(String)statestack.get(statestack.size()-1))||statestack.get(statestack.size()-1).equals("empty")){
						if(statestack.get(statestack.size()-1).equals(token.get(i))){
							if (isop((String)statestack.get(statestack.size()-1))) {
								attribute.put("relop", token.get(i));
							}
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"("+linecount.get(i)+")");
							i++;
						}else if(statestack.get(statestack.size()-1).equals("empty")){
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"("+linecount.get(i)+")");
						}else{
							if(statestack.get(statestack.size()-1).equals("id")){
								attribute.put("id.lexeme", token.get(i));
							}else if(statestack.get(statestack.size()-1).equals("ch")){
								attribute.put("ch.lex", token.get(i));
							}else if (statestack.get(statestack.size()-1).equals("num")) {
								numstack.add(token.get(i));
								attribute.put("digit.lex", token.get(i));
							}else if (statestack.get(statestack.size()-1).equals("digit")) {
								attribute.put("digit.lex", token.get(i));
							}
							ouput.add(mulString(tap.get(tap.size()-1), suojin)+statestack.get(statestack.size()-1)+"  :"+token.get(i)+"("+linecount.get(i)+")");
							i++;
						}
						statestack.remove(statestack.size()-1);
						tap.remove(tap.size()-1);
					}
					else{
						System.out.println(statestack.get(statestack.size()-1)+"   "+token.get(i));
						DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
						defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"终结符不匹配"});
						statestack.remove(statestack.size()-1);//弹出此终结符
						jTable.invalidate();
					}
				}
				//栈顶符号是非终结符
				else if(i<token.size()&&gTable.nonterminals.contains(statestack.get(statestack.size()-1))){
				HashMap<String, ArrayList<String>> hashMap=selecttable.get(statestack.get(statestack.size()-1));
				//System.out.println(statestack.get(statestack.size()-1));
				//System.out.println(hashMap);
				for (String str : hashMap.keySet()) {
					if(tokenKind(token.get(i),kind.get(i),str)){
						choice=str;
					}
				}
				//System.out.println(token.get(i)+kind.get(i));
				ArrayList<String> states=hashMap.get(choice);//找到对应的产生式右部
				if(states==null){
					DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
					defaultTableModel.addRow(new Object[]{token.get(i),linecount.get(i),"未找到产生式"});//直接不管恐慌模式
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
			//栈顶符号是非终结符与非终结符的字符串
			else if(statestack.get(statestack.size()-1) instanceof String){
					String action=(String)statestack.get(statestack.size()-1);
					//栈顶符号是语义动作
					if(action.contains("#")){
						action=action.substring(1, action.length()-1);//去除#
						if(action.equals("a1")){
								offset=0;
						}
						else if(action.equals("a2")){
								Symbol symbol=new Symbol();
								symbol.name=(String)attribute.get("id.lexeme");
								symbol.type=(String)attribute.get("T.type");
								symbol.offset=offset;
								if(symboltable.contains(symbol)){
									DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
									 int line=Integer.valueOf(linecount.get(linecount.size()-1));
									   if(i<linecount.size())
									    line=Integer.valueOf(linecount.get(i));
									defaultTableModel.addRow(new Object[]{line,"标识符"+symbol.name+"重复声明"});
									jTable5.invalidate();
									statestack.remove(statestack.size()-1);
									continue;
								}
								symboltable.add(symbol);
								DefaultTableModel sDefaultTableModel=(DefaultTableModel) jTable2.getModel();
								sDefaultTableModel.addRow(new Object[]{symbol.name,symbol.type,offset,linecount.get(i)});
								jTable2.invalidate();
								offset=offset+(int)attribute.get("T.width");
							}
							else if(action.equals("a3")){
								attribute.put("type", "record");
								Symbol symbol=new Symbol();
								symbol.name=(String)attribute.get("id.lexeme");
								symbol.type=(String)attribute.get("type");
								symbol.offset=offset;
								if(symboltable.contains(symbol)){
									DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
									 int line=Integer.valueOf(linecount.get(linecount.size()-1));
									   if(i<linecount.size())
									    line=Integer.valueOf(linecount.get(i));
									defaultTableModel.addRow(new Object[]{line,"标识符"+symbol.name+"重复声明"});
									jTable5.invalidate();
									statestack.remove(statestack.size()-1);
									continue;
								}
								symboltable.add(symbol);
								DefaultTableModel sDefaultTableModel=(DefaultTableModel) jTable2.getModel();
								sDefaultTableModel.addRow(new Object[]{symbol.name,symbol.type,offset,linecount.get(i)});
								jTable2.invalidate();
							}
							else if(action.equals("a4")){
								parmcount=0;
								procname=(String)attribute.get("id.lexeme");
								attribute.put("type", "proc");
								Symbol symbol=new Symbol();
								symbol.name=(String)attribute.get("id.lexeme");
								symbol.type=(String)attribute.get("type");
								symbol.offset=offset;
								if(symboltable.contains(symbol)){
									DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
									 int line=Integer.valueOf(linecount.get(linecount.size()-1));
									   if(i<linecount.size())
									    line=Integer.valueOf(linecount.get(i));
									defaultTableModel.addRow(new Object[]{line,"标识符"+symbol.name+"重复声明"});
									jTable5.invalidate();
									statestack.remove(statestack.size()-1);
									continue;
								}
								symboltable.add(symbol);
								DefaultTableModel sDefaultTableModel=(DefaultTableModel) jTable2.getModel();
								sDefaultTableModel.addRow(new Object[]{symbol.name,symbol.type,offset,linecount.get(i)});
								jTable2.invalidate();
							}
							else if (action.equals("a5")) {
								t=(String)attribute.get("X.type");
								w=(int)attribute.get("X.width");
							}
							else if (action.equals("a6")) {
								String string=(String)attribute.get("C.type");
								attribute.put("T.type", string);
								int width=(int)attribute.get("C.width");
								attribute.put("T.width", width);
							}else if (action.equals("a33")) {
								attribute.put("X.type", "int");
								attribute.put("X.width", 4);
							}else if (action.equals("a34")) {
								attribute.put("X.type", "real");
								attribute.put("X.width", 8);
							}else if (action.equals("a35")) {
								attribute.put("X.type", "char");
								attribute.put("X.width", 1);
							}
							else if (action.equals("a7")) {
									int width=Integer.valueOf(numstack.get(numstack.size()-1))*(int)attribute.get("C.width");
									attribute.put("C.width", width);
									String string="array("+numstack.get(numstack.size()-1)+","+attribute.get("C.type")+")";
									attribute.put("C.type", string);
									numstack.remove(numstack.size()-1);
							}
							else if (action.equals("a8")) {
								attribute.put("C.type", t);
								attribute.put("C.width",w);
							}
							else if(action.equals("a9")){
								String string= (String)attribute.get("id.lexeme");
							   boolean b=lookup(string);
							  // System.out.println(b);
							   if (b==false) {
								   DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								   int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
									defaultTableModel.addRow(new Object[]{line,"标识符"+string+"未声明"});
									jTable5.invalidate();
									statestack.remove(statestack.size()-1);
									continue;
							}else {
								if(iserror){
									statestack.remove(statestack.size()-1);
									continue;
								}
								String string2=string+"="+(String)attribute.get("E.addr");
								String string3="( =,"+(String)attribute.get("E.addr")+",-,"+string+")";
								threeadd.put(count, string2);
								four.put(count, string3);
								count++;
							}
							   ArrayList<String> arrayList=new ArrayList<>();
								attribute.put("S.nextlist", arrayList);
						}else if (action.equals("a10")) {
							ArrayList<String> arrayList=new ArrayList<>();
							attribute.put("S.nextlist", arrayList);
						}else if (action.equals("a11")) {
							String string="return"+(String)attribute.get("F.addr");
							String string2="(return,-,-,"+(String)attribute.get("F.addr")+")";
							threeadd.put(count, string);
							four.put(count, string2);
							count++;
						}else if (action.equals("a12")) {
							if(!Gstack.isEmpty()&&(String)attribute.get("E'.addr")!=null&&!iserror){
							if(symboltable.contains((String)attribute.get("E'.addr"))&&symboltable.contains(Gstack.get(Gstack.size()-1))){
									if(!getsymbol((String)attribute.get("E'.addr")).type.equals(getsymbol(Gstack.get(Gstack.size()-1)))){
										 int line=Integer.valueOf(linecount.get(linecount.size()-1));
										   if(i<linecount.size())
										    line=Integer.valueOf(linecount.get(i));
										DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
										defaultTableModel.addRow(new Object[]{line,"运算分量不匹配"});
										jTable5.invalidate();
										statestack.remove(statestack.size()-1);
										iserror=true;
										continue;
									}
							}else if (((((String)attribute.get("E'.addr")).length()==1)&&!isDigit(Gstack.get(Gstack.size()-1).charAt(0)))||(!(((String)attribute.get("E'.addr")).length()==1)&&isDigit(Gstack.get(Gstack.size()-1).charAt(0)))) {
								 int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
								DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								defaultTableModel.addRow(new Object[]{line,"运算分量不匹配"});
								jTable5.invalidate();
								statestack.remove(statestack.size()-1);
								iserror=true;
								continue;
							}
							lastE=(String)attribute.get("E.addr");
							attribute.put("E.addr", "t"+tcount);
							tcount++;
							String string=(String)attribute.get("E.addr")+"="+(String)attribute.get("E'.addr")+"+"+Gstack.get(Gstack.size()-1);
							threeadd.put(count, string);
							String string2="(+,"+(String)attribute.get("E'.addr")+","+Gstack.get(Gstack.size()-1)+","+(String)attribute.get("E.addr")+")";
							four.put(count, string2);
							count++;
							}else{
								if(iserror){
									//iserror=false;
									statestack.remove(statestack.size()-1);
									continue;
								}
								String string=Gstack.get(Gstack.size()-1);
								lastE=(String)attribute.get("E.addr");
								attribute.put("E.addr", string);
							}
							Gstack.clear();
							attribute.remove("E'.addr");
						}else if (action.equals("a13")) {
							String string=Gstack.get(Gstack.size()-1);
							attribute.put("E'.addr", string);
							Gstack.remove(Gstack.size()-1);
						}else if (action.equals("a14")) {
							if((String)attribute.get("G'.addr")!=null&&!Fstack.isEmpty()&&!iserror){
								if(symboltable.contains((String)attribute.get("G'.addr"))&&symboltable.contains(Fstack.get(Fstack.size()-1))){
									if(!getsymbol((String)attribute.get("G'.addr")).type.equals(getsymbol(Fstack.get(Fstack.size()-1)))){
										 int line=Integer.valueOf(linecount.get(linecount.size()-1));
										   if(i<linecount.size())
										    line=Integer.valueOf(linecount.get(i));
										DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
										defaultTableModel.addRow(new Object[]{line,"运算分量不匹配"});
										jTable5.invalidate();
										iserror=true;
										statestack.remove(statestack.size()-1);
										continue;
									}
							}else if( ((((String)attribute.get("G'.addr")).length()==1)&&!isDigit(Fstack.get(Fstack.size()-1).charAt(0)))||((((String)attribute.get("G'.addr")).length()>1)&&isDigit(Fstack.get(Fstack.size()-1).charAt(0)))) {
								 int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
								DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								defaultTableModel.addRow(new Object[]{line,"运算分量不匹配"});
								jTable5.invalidate();
								iserror=true;
								statestack.remove(statestack.size()-1);
								System.out.println(iserror);
								continue;
							}
							attribute.put("G.addr", "t"+tcount);
							Gstack.add( "t"+tcount);
							tcount++;
							String string=(String)attribute.get("G.addr")+"="+Fstack.get(Fstack.size()-1)+"*"+(String)attribute.get("G'.addr");
							threeadd.put(count, string);
							String string2="(*,"+Fstack.get(Fstack.size()-1)+","+(String)attribute.get("G'.addr")+","+(String)attribute.get("G.addr")+")";
							four.put(count, string2);
							count++;
							}else{
								if(iserror){
									statestack.remove(statestack.size()-1);
									continue;
								}
								String string=Fstack.get(Fstack.size()-1);
								attribute.put("G.addr", string);
								Gstack.add(string);
							}
							Fstack.clear();
							attribute.remove("G'.addr");
						}else if (action.equals("a15")) {
							String string=Fstack.get(Fstack.size()-1);
							attribute.put("G'.addr", string);
							Fstack.remove(Fstack.size()-1);
						}else if (action.equals("a16")) {
							String string=(String)attribute.get("E.addr");
							attribute.put("F..addr", string);
							Fstack.add(string);
						}else if (action.equals("a17")) {
							String string= (String)attribute.get("id.lexeme");
							boolean b=lookup(string);
							if(b==true){
								attribute.put("F.addr", string);
								Fstack.add(string);
							}else {
								 DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								 int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
									defaultTableModel.addRow(new Object[]{line,"标识符"+string+"未声明"});
									jTable5.invalidate();
									statestack.remove(statestack.size()-1);
									continue;
							}
						}else if (action.equals("a18")) {
							String string=(String)attribute.get("digit.lex");
							Fstack.add(string);
							attribute.put("F.addr", string);
						}else if (action.equals("a19")) {
							String string=(String)attribute.get("ch.lex");
							Fstack.add(string);
							attribute.put("F.addr", string);
						}else if (action.equals("a20")) {
							ArrayList<String> arrayList=new ArrayList<>();
							arrayList.add(String.valueOf(count));
							attribute.put("B.truelist", arrayList);
							ArrayList<String> arrayList2=new ArrayList<>();
							arrayList2.add(String.valueOf(count+1));
							attribute.put("B.falselist", arrayList2);
							String string="if "+lastE+" "+(String)attribute.get("relop")+" "+(String)attribute.get("E.addr")+" goto ";
							threeadd.put(count, string);
							String string3="(j"+(String)attribute.get("relop")+","+lastE+","+(String)attribute.get("E.addr")+",";
							four.put(count, string3);
							count++;
							String string2="goto ";
							threeadd.put(count, string2);
							String string4="(j,-,-,";
							four.put(count, string4);
							count++;
						}else if (action.equals("a21")) {
							ArrayList<String> arrayList=new ArrayList<>();
							arrayList.add(String.valueOf(count));
							attribute.put("B.truelist", arrayList);
							String string="goto ";
							threeadd.put(count, string);
							String string4="(j,-,-,";
							four.put(count, string4);
							count++;
						}else if (action.equals("a22")) {
							ArrayList<String> arrayList=new ArrayList<>();
							arrayList.add(String.valueOf(count));
							attribute.put("B.falselist", arrayList);
							String string="goto ";
							threeadd.put(count, string);
							String string4="(j,-,-,";
							four.put(count, string4);
							count++;
						}else if (action.equals("a23")) {
							ArrayList<String> arrayList=(ArrayList<String>)attribute.get("B.truelist");
							ArrayList<String> arrayList2=(ArrayList<String>)attribute.get("B.falselist");
							attribute.put("B.truelist", arrayList);
							attribute.put("B.falselist", arrayList2);
						}else if (action.equals("a24")) {
							ArrayList<String> arrayList=(ArrayList<String>)attribute.get("B.truelist");
							ArrayList<String> arrayList2=(ArrayList<String>)attribute.get("B.falselist");
							attribute.put("I.truelist", arrayList2);
							attribute.put("I.falselist", arrayList);
						}else if (action.equals("a27")) {
							attribute.put("M3.quad", count);
						}else if (action.equals("a28")) {
							ArrayList<String> arrayList=(ArrayList<String>)attribute.get("B.falselist");
							ArrayList<String> arrayList2=(ArrayList<String>)attribute.get("S.nextlist");
							ArrayList<String> result=merge(arrayList, arrayList2);
							attribute.put("S.nextlist", result);
							int huitian=(int)attribute.get("M3.quad");
							ArrayList<String>arrayList3=(ArrayList<String>)attribute.get("B.truelist");
							backpatch(arrayList3, huitian);
							backpatch(result, count);
						}else if (action.equals("a29")) {
							ArrayList<String> arrayList=new ArrayList<>();
							arrayList.add(String.valueOf(count));
							attribute.put("N.nextlist", arrayList);
							String string="goto ";
							threeadd.put(count, string);
							String string4="(j,-,-,";
							four.put(count, string4);
							count++;
						}else if (action.equals("a30")) {
							ArrayList<String> arrayList=(ArrayList<String>)attribute.get("B.falselist");
							ArrayList<String> arrayList2=(ArrayList<String>)attribute.get("S.nextlist");
							ArrayList<String>arrayList3=(ArrayList<String>)attribute.get("B.truelist");
							attribute.put("S.nextlist", arrayList);
							int huitian=(int)attribute.get("M1.quad");
							System.out.println(huitian);
							backpatch(arrayList2, huitian);
							huitian=(int)attribute.get("M2.quad");
							System.out.println(huitian);
							backpatch(arrayList3, huitian);
							huitian=(int)attribute.get("M1.quad");
							String string="goto "+huitian;
							threeadd.put(count, string);
							String string4="(j,-,-,"+huitian+")";
							four.put(count, string4);
							count++;
							backpatch(arrayList, count);
						}else if (action.equals("a31")) {
							int k=parmlist.size();
							String string=procname;
							int j=idparmcount.get(string);
							if(k!=j){
								//
							}else{
								for(int w=0;w<k;w++){
									String string2="param "+parmlist.get(w);
									threeadd.put(count, string2);
									String string4="(param,-,-,"+string2+")";
									four.put(count, string4);
									count++;
								}
								String string3="call "+string+" , "+k;
								threeadd.put(count, string3);
								String string4="(call,-,-,"+string+")";
								four.put(count, string4);
								count++;
								parmlist.clear();
								ArrayList<String> arrayList=new ArrayList<>();
								attribute.put("S.nextlist", arrayList);
							}
						}else if (action.equals("a32")) {
							String string=(String)attribute.get("E.addr");
							parmlist.add(string);
						}else if (action.equals("a36")) {
							parmcount++;
							idparmcount.put(procname, parmcount);
							Symbol symbol=new Symbol();
							symbol.name=(String)attribute.get("id.lexeme");
							symbol.type=(String)attribute.get("X.type");
							symbol.offset=offset;
							if(symboltable.contains(symbol)){
								DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								 int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
								defaultTableModel.addRow(new Object[]{line,"标识符"+symbol.name+"重复声明"});
								jTable5.invalidate();
								statestack.remove(statestack.size()-1);
								continue;
							}
							symboltable.add(symbol);
							DefaultTableModel sDefaultTableModel=(DefaultTableModel) jTable2.getModel();
							sDefaultTableModel.addRow(new Object[]{symbol.name,symbol.type,offset,linecount.get(i)});
							jTable2.invalidate();
							offset=offset+(int)attribute.get("X.width");
						}else if (action.equals("a37")) {
							Symbol symbol=new Symbol();
							parmcount++;
							idparmcount.put(procname, parmcount);
							symbol.name=(String)attribute.get("id.lexeme");
							symbol.type=(String)attribute.get("X.type");
							symbol.offset=offset;
							if(symboltable.contains(symbol)){
								DefaultTableModel defaultTableModel=(DefaultTableModel) jTable5.getModel();
								 int line=Integer.valueOf(linecount.get(linecount.size()-1));
								   if(i<linecount.size())
								    line=Integer.valueOf(linecount.get(i));
								defaultTableModel.addRow(new Object[]{line,"标识符"+symbol.name+"重复声明"});
								jTable5.invalidate();
								statestack.remove(statestack.size()-1);
								continue;
							}
							symboltable.add(symbol);
							DefaultTableModel sDefaultTableModel=(DefaultTableModel) jTable2.getModel();
							sDefaultTableModel.addRow(new Object[]{symbol.name,symbol.type,offset,linecount.get(i)});
							jTable2.invalidate();
							offset=offset+(int)attribute.get("X.width");
						}else if(action.equals("a38")){
							attribute.put("M1.quad", count);
						}else if(action.equals("a39")){
							attribute.put("M2.quad", count);
						}else if(action.equals("a40")){
							String string=(String)attribute.get("E.addr");
							parmlist.clear();
							parmlist.add(string);
							//System.out.println(parmlist);
						}else if(action.equals("a41")){
							procname=(String)attribute.get("id.lexeme");
						}
				}
					statestack.remove(statestack.size()-1);
			}else if(!statestack.isEmpty()&&i>=token.size()){
				if(!gTable.CanEmpty((String)statestack.get(statestack.size()-1))){
				DefaultTableModel defaultTableModel=(DefaultTableModel)jTable.getModel();
				defaultTableModel.addRow(new Object[]{statestack.get(statestack.size()-1),linecount.get(i-1),"缺少输入字符"});//直接不管恐慌模式
				jTable.invalidate();
				}
			}
				if(i<token.size())
				System.out.println(statestack+"  "+token.get(i));
		}
			output();
		for(int k=0;k<symboltable.size();k++){
			System.out.println(symboltable.get(k).name+symboltable.get(k).type+symboltable.get(k).offset);
			}
		DefaultTableModel defaultTableModel=(DefaultTableModel)jTable4.getModel();
			for(int k=1;k<=threeadd.keySet().size();k++){
				System.out.println(threeadd.get(k));
				defaultTableModel.addRow(new Object[]{k,threeadd.get(k),four.get(k)});
			}
			jTable4.invalidate();
	}
		//输出各种东西，select，first，follow
		public void output(){
			File file,file1,file2,file3;
			FileWriter fileWriter;
			BufferedWriter bufferedWriter;
			try {
				file=new File("tree.txt");
				fileWriter=new FileWriter(file);
				bufferedWriter=new BufferedWriter(fileWriter);
				for(int j=0;j<ouput.size();j++){
					bufferedWriter.write(ouput.get(j)+"\r\n");
						//System.out.println(ouput.get(j));
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
		//判断是否在直接比较字符中
		private boolean candirectcompare(String str){
			for(int i=0;i<directcompare.length;i++){
				if(directcompare[i].equals(str)){
					return true;
				}
			}
			return false;
		}
		//判断token对应那种输入符号
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
		//字符串翻倍
		public String mulString(int count,String str){
			StringBuilder stringBuilder=new StringBuilder();
			for(int i=0;i<count;i++){
				stringBuilder.append(str);
			}
			return stringBuilder.toString();
		}
		public boolean lookup(String string){
			int j;
			for( j=0;j<symboltable.size();j++){
				if(symboltable.get(j).name.equals(string)){
					break;
				}
			}
			if(j==symboltable.size()){
				return false;
			}
			return true;
		}
		public ArrayList<String> merge(ArrayList<String > arrayList,ArrayList<String > arrayList2){
			ArrayList<String > result=new ArrayList<>();
			if(arrayList!=null)
			for(int i=0;i<arrayList.size();i++){
				result.add(arrayList.get(i));
			}
			if(arrayList2!=null)
			for(int i=0;i<arrayList2.size();i++){
				if(!result.contains(arrayList2.get(i))){
					result.add(arrayList2.get(i));
				}
			}
			return result;
		}
		public void backpatch(ArrayList<String> arrayList,int huitian){
			ArrayList<String > result=new ArrayList<>();
			//System.out.println(arrayList);
			for(int i=0;i<arrayList.size();i++){
				String string=threeadd.get(Integer.valueOf(arrayList.get(i)));
				String string2=four.get(Integer.valueOf(arrayList.get(i)));
				string=string+huitian;
				string2=string2+huitian+")";
				threeadd.put(Integer.valueOf(arrayList.get(i)), string);
				four.put(Integer.valueOf(arrayList.get(i)), string2);
			}
			
		}
		public boolean isop(String string){
			for(int i=0;i<opStrings.length;i++){
				if(opStrings[i].equals(string))
					return true;
			}
			return false;
		}
		public Symbol getsymbol(String string){
			Symbol symbol=new Symbol();
			symbol.name=string;
			return symboltable.get(symboltable.indexOf(symbol));
		}
		public static Boolean isDigit(char ch)
		{  
	        if( (ch >= '0' && ch <= '9')){
	        	return true;
	        }else{
	        	return false;
	        }
	    }
}
