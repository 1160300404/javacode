package semanticcompiler;

import java.util.ArrayList;
import java.util.HashMap;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//generateTable generateTable=new generateTable();
		ArrayList<Symbol> arrayList=new ArrayList<>();
		Symbol symbol=new Symbol();
		symbol.name="aaa";
		arrayList.add(symbol);
		Symbol symbol1=new Symbol();
		symbol1.name="aaa"+null;
		System.out.println(symbol.name); 
	}
}

