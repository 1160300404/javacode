package grammercomplier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ietf.jgss.Oid;


public class generateTable {
	public ArrayList<Production> productions;
	public ArrayList<String> terminals;
	public ArrayList<String> nonterminals;
	public HashMap<String, ArrayList<String>> firsts;
	public HashMap<String, ArrayList<String>> follows;
	public HashMap<String, ArrayList<String>> selects;
	public generateTable(){
		productions=new ArrayList<>();
		terminals=new ArrayList<>();
		nonterminals=new ArrayList<>();
		firsts=new HashMap<String, ArrayList<String>>();
		follows=new HashMap<String, ArrayList<String>>();
		selects=new HashMap<String, ArrayList<String>>();
		getproductionandnonterminals();
		getterminals();
		getFirst();
		getFollow();
		getSelect();
	}
	//获得产生式以及非终结符（产生式左部）
	public void getproductionandnonterminals(){
		try {
			File file=new File("src/LL.txt");
			FileReader fReader;
			fReader = new FileReader(file);
			BufferedReader bfReader=new BufferedReader(fReader);
			String line;
			while((line=bfReader.readLine())!=null){
				String[] strings=line.split("->");
				productions.add(new Production(strings[0].trim(), strings[1].trim().split(" ")));
				if(!nonterminals.contains(strings[0].trim()))
					nonterminals.add(strings[0].trim());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获得终结符
	public void getterminals(){
		try {
			File file=new File("src/LL.txt");
			FileReader fReader;
			fReader = new FileReader(file);
			BufferedReader bfReader=new BufferedReader(fReader);
			String line;
			while((line=bfReader.readLine())!=null){
				String[] strings=line.split("->");
				String rights[]= strings[1].trim().split(" ");
				for(int i=0;i<rights.length;i++){
					if(!terminals.contains(rights[i])&&!nonterminals.contains(rights[i])&&!rights[i].equals("empty"))
					terminals.add(rights[i]);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获得first集合
	public void getFirst(){
		//首先终结符的first集合都是自己
		for(int i=0;i<terminals.size();i++){
			ArrayList<String> arrayList=new ArrayList<>();
			arrayList.add(terminals.get(i));
			firsts.put(terminals.get(i), arrayList);
		}
		//为了在之后方便处理所以我们应该想给非终结符也创立空的first集合
		for(int i=0;i<nonterminals.size();i++){
			ArrayList<String> arrayList=new ArrayList<>();
			firsts.put(nonterminals.get(i), arrayList);
		}
		//对于非终结符之后使用产生式向first其中添加
		while(true){
			String left;
			String right[];
			boolean ifchange=false;
			for(int i=0;i<productions.size();i++){
				left=productions.get(i).getleft();
				right=productions.get(i).getright();
				for(int j=0;j<right.length;j++){
					if(!right[j].equals("empty")){
						for(int k=0;k<firsts.get(right[j]).size();k++){
							if(!firsts.get(left).contains(firsts.get(right[j]).get(k))&&!firsts.get(right[j]).get(k).equals("empty")){
								firsts.get(left).add(firsts.get(right[j]).get(k));
								ifchange=true;
							}
						}
					}
					if((j==right.length-1&&(CanEmpty(right[j]))||right[j].equals("empty"))){
						if(!firsts.get(left).contains("empty")){
							firsts.get(left).add("empty");
							ifchange=true;
						}
					}
					if(CanEmpty(right[j])){
						continue;
					}else{
						break;
					}
				}
			}
			if(ifchange==false){
				break;
			}
		}
	}
	//获得follow集合
	public void getFollow(){
		ArrayList<String> follow;
		for (int i = 0; i < nonterminals.size(); i++) {
			follow = new ArrayList<String>();
			follows.put(nonterminals.get(i), follow);
		}
		follows.get("Program").add("$");
		boolean ifchange;
		boolean fab;
		while (true) {
			ifchange = false;
			//循环，遍历所有产生式
			for (int i = 0; i < productions.size(); i++) {
				String left;
				String[] rights;
				rights = productions.get(i).getright();
				//遍历产生式右部
				for (int j = 0; j < rights.length; j++) {
					//非终结符
					if (nonterminals.contains( rights[j])) {
						fab = true;
						for(int k = j+1; k < rights.length; k++) {
						    //查找first集
							for(int v = 0; v < firsts.get(rights[k]).size(); v++) {
									//将后一个元素的first集加入到前一个元素的follow集中
									if(!follows.get( rights[j]).contains(firsts.get(rights[k]).get(v))&&!firsts.get(rights[k]).get(v).equals("empty")) {
										follows.get( rights[j]).add(firsts.get(rights[k]).get(v));
										ifchange=true;
									}
								}
								if (CanEmpty(rights[k])) {
									continue;
								}
								else {
									fab = false;
									break;
								}
							}
						    //如果存在一个产生式A→αB，或存在产生式A→αBβ且FIRST(β) 包含ε，
						    //那么 FOLLOW(A)中的所有符号都在FOLLOW(B)中
							if(fab) {
								left = productions.get(i).getleft();
								for (int p = 0; p < follows.get(left).size(); p++) {
									if (!follows.get( rights[j]).contains(follows.get(left).get(p))) {
										follows.get( rights[j]).add(follows.get(left).get(p));
										ifchange = true;
									}
								}
							}
						}				
					}
				}
			    //全部处理后跳出循环
				if(ifchange==false){
					break;
				}	
			}			
	}
	//获得Select集合
	public void getSelect(){
		String left;
		String right[];
		for(int i=0;i<nonterminals.size();i++){
			ArrayList<String> select=new ArrayList<>();
			selects.put(nonterminals.get(i), select);
		}
		//计算每个产生式的select集合
		for(int i=0;i<productions.size();i++){
			left=productions.get(i).getleft();
			right=productions.get(i).getright();
			boolean ifempyt=true;
			for(int j=0;j<right.length;j++){
				if(firsts.get(right[j])!=null){
				for(int k=0;k<firsts.get(right[j]).size();k++){
				if(!productions.get(i).select.contains(firsts.get(right[j]).get(k))&&!firsts.get(right[j]).get(k).equals("empty")){
					productions.get(i).select.add(firsts.get(right[j]).get(k));
				}
			}
			if(!CanEmpty(right[j])&&!right[j].equals("empty")){
					ifempyt=false;
					break;
					}
				}
			}
			if(ifempyt){
				for (int j = 0; j < follows.get(left).size(); j++) {
					if (!productions.get(i).select.contains(follows.get(left).get(j))&&follows.get(left).get(j)!="$") {
						productions.get(i).select.add(follows.get(left).get(j));
					}
				}
			}
		} 
	}
	public boolean CanEmpty(String str){
		for(int i=0;i<productions.size();i++){
			if(productions.get(i).getleft().equals(str)){
				for(int j=0;j<productions.get(i).getright().length;j++){ 
					if(productions.get(i).getright()[j].equals("empty")||firsts.get(productions.get(i).getright()[j]).contains("empty")){
						return true;
					}else{
						break;
					}
				}
			}
		}
		return false;
	}
}
