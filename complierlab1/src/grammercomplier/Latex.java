package grammercomplier;
import java.io.File;
import java.util.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
//import com.ui.*;

public class Latex
{
	private String text;
	private JTable jtable1;
	private ArrayList<String> arrayList;
	public  ArrayList<String> DFA=new ArrayList<>();
	public static String keywords[] = { "auto", "double", "int", "struct",  
        "break", "else", "long", "switch", "case", "enum", "register",  
        "typedef", "char", "extern", "return", "union", "const", "float",  
        "short", "unsigned", "continue", "for", "signed", "void",  
        "default", "goto", "sizeof", "volatile", "do", "if", "while",  
        "static", "main", "String"};
	 public static char operator[] = { '+', '-', '*', '=', '<', '>', '&', '|', '~',  
         '^', '!', '%' };
	public static char boundary[] = { ',', ';', '[', ']', '(', ')', '.', '{', '}'};
	public ArrayList<ArrayList<String>> resut=new ArrayList<>();
	public Latex(String text, JTable jtable1,ArrayList<String> arrayList)
	{
		this.text = text;
		this.jtable1 = jtable1;
		this.arrayList=arrayList;
		String string=null;
		for(int i=0;i<arrayList.size();i++){
			DFA.add(arrayList.get(i));
			}
	}

	public ArrayList<ArrayList<String>> analyze()
	{
		ArrayList<String> tokens=new ArrayList<>();
		ArrayList<String> kind=new ArrayList<>();
		ArrayList<String> linecount=new ArrayList<>();
		String[] texts = text.split("\n");
		for(int m = 0; m < texts.length; m++)
		{
			String str = texts[m];
			if(str.equals(""))
				continue;
			else {
				char[] strline = str.toCharArray();
				for(int i = 0; i < strline.length; i++) 
				{
					char ch = strline[i];
					String token = "";
					if (isAlpha(ch)) 
                    {  
						int state=19;
                        do {  
                            token += ch;  
                            i++;  
                            if(i >= strline.length) break;  
                            ch = strline[i];  
                            char sch=DFA.get(state).toCharArray()[19];
                            if(!letterDFA(ch,sch)){
                            	break;
                            }
                        } while (true);  
                        --i;   

                        if (isMatchKeyword(token.toString()))   
                        {  
                            DefaultTableModel tableModel = (DefaultTableModel) jtable1.getModel();
                            tableModel.addRow(new Object[] {token, "<"+token+",_>","关键字"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("IDN");
                            jtable1.invalidate();
                        }

                        else {
                        	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                            tableModel1.addRow(new Object[] {token, "<``,"+token+">","标识符"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("IDN");
                            jtable1.invalidate();
                        }
                        token = "";
                        
                    }

					else if(isDigit(ch))
					{
						//System.out.println(ch);
						int state = 1;
						int k;
                        Boolean isfloat = false;  
                        while ( (ch != '\0') && (isDigit(ch) || ch == '.' || ch == 'e' || ch == '-'))
                        {
                        	if (ch == '.' || ch == 'e')  
                              isfloat = true; 
                            for (k = 0; k <= 6; k++) 
                            {  
                                char dfastr[] = DFA.get(state).toCharArray();  
                                if (ch != '#' && 1 == digitDFA(ch, dfastr[k])) 
                                {  
                                    token += ch;  
                                    state = k;  
                                    break;  
                                }  
                            }
                            if (k > 6) break;
                            i++; 
                            if(i>=strline.length) break;  
                            ch = strline[i]; 
                        }
                        while(ch==' '){
                        	i++;
                        	ch=strline[i];
                        }
                        Boolean haveMistake = false;  
                        
                        if (state == 2 || state == 4 || state == 5) 
                        {  
                            haveMistake = true;  
                        } 
                        
                        else//1,3,6  
                        {  
                            if (((!isOp(ch)&&(!isbordar(ch)) )|| ch == '.') && !isDigit(ch)&&!isAlpha(ch))  
                                haveMistake = true;  
                        }  
                        
                        if (haveMistake)  
                        {  
                        	while (ch != '\0' && ch != ',' && ch != ';' && ch != ' ')
                            {  
                                token += ch;  
                                i++;
                                if(i >= strline.length) break;  
                                ch = strline[i];  
                            }  
                        }
                        else 
                        {  
                            if (isfloat) 
                            {  
                            	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                                tableModel1.addRow(new Object[] {token, "<CONST,"+token+">","浮点型常数"});
                                tokens.add(token);
                                linecount.add(String.valueOf(m));
                                kind.add("CONSTFLOAT");
                                jtable1.invalidate();    
                            } 
                            else
                            {  
                            	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                                tableModel1.addRow(new Object[] {token, "<CONST,"+token+">","整型常数"});
                                tokens.add(token);
                                linecount.add(String.valueOf(m));
                                kind.add("CONSTINT");
                                jtable1.invalidate();   
                            }  
                        }
                        i--;
                        token = "";
                    }

					else if(ch == '\'')
					{
						int state = 7;				        
                        token += ch;
                        while (state != 10) {  
                            i++;
                            if(i >= strline.length-1) break;
                            ch = strline[i]; 
                            for (int k = 7; k <=10; k++) 
                            {  
                                char dfastr[] = DFA.get(state).toCharArray();  
                                if (charDFA(ch, dfastr[k])) 
                                {            
                                    token += ch;
                                    state = k;  
                                    break;  
                                }  
                            }  
                        }
                        if (state != 10) {  
                            i--;  
                            i--;
                        } 
                        else 
                        {  
                        	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                            tableModel1.addRow(new Object[] {token,"<"+token+",_>", "字符常量"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("CHAR");
                            jtable1.invalidate(); 
                        }     
                        token = "";
					}

					else if (ch == '"')
					{
						String string = "";  
                        string += ch;  
                        token+=ch;
                        int state = 11;  
                        Boolean haveMistake = false;
                        while (state != 14 ) {  
                            i++; 
                            if(i>=strline.length-1) {  
                                haveMistake = true;  
                                break;  
                            }                        
                            ch = strline[i];          
                            //System.out.println(ch);
                            if (ch == '\0') {  
                                haveMistake = true;  
                                break;  
                            }
                            
                            for (int k = 11; k <= 14; k++) {  
                                char dfastr[] = DFA.get(state).toCharArray();  
                                if (stringDFA(ch, dfastr[k])) {  
                                    string += ch;  
                                    if (k == 13 && state == 12) {  
                                        if (istrunwd(ch)) 
                                            token = token + '\\' + ch;  
                                        else  
                                            token += ch;  
                                    } else if ( k != 12)  
                                        token += ch;  
                                    state = k;  
                                    //System.out.println(state);
                                    break;  
                                }  
                            }  
                        }
                        if (haveMistake) {  
                            --i; 
                            --i;
                            //System.out.println(i);
                        } else {  
                        	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                            tableModel1.addRow(new Object[] {token, "<"+token+",_>","字符串常量"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("String");
                            jtable1.invalidate();  
                        }  
                        token = "";		
					}

					else if (isOp(ch))   
                    {
						int k;
						int state=0;
						while(isOp(ch)){
						for(k=20;k<=24;k++){
							char[] dfastr=DFA.get(state).toCharArray();
							if(opDFA(ch,dfastr[k])){
								token += ch; 
								state=k;
								break;
							}
						}
						if(k>24) break;
						i++;
						if(i>=strline.length) break;
						ch=strline[i];
						//System.out.println(ch);
						}
						--i;
                        	DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                            tableModel1.addRow(new Object[] {token, "<"+token+",_>","操作符"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("OP");
                            jtable1.invalidate();
                        
                        token = "";	
                    }
					else if(isbordar(ch)){
						DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                        tableModel1.addRow(new Object[] {ch, "<"+ch+",_>","边界符"});
                        tokens.add(String.valueOf(ch));
                        linecount.add(String.valueOf(m));
                        kind.add("BORDER");
                        jtable1.invalidate();
					}
					else if (ch == '/')
					{
						token += ch;  
                        i++;
                        if(i>=strline.length) break;  
                        ch = strline[i];
                        if (ch != '*')   
                        {  
                            if (ch == '=')  
                                token += ch; // /=  
                            else 
                            {  
                                --i;   
                            }  
                            DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                            tableModel1.addRow(new Object[] {token, "<"+token+",_>","操作符"});
                            tokens.add(token);
                            linecount.add(String.valueOf(m));
                            kind.add("OP");
                            jtable1.invalidate();    
                            token = "";  
                        }
                        else 
                        {
                        	Boolean haveMistake = false;
                        	int State = 0;
                        	if (ch == '*') 
                        	{  
                        		// ch == '*'
                        		token += ch;  
                                int state = 16;  

                                while (state != 4) 
                                {  
                                    i++;
                                    if(i>=strline.length) break;  
                                    ch = strline[i];
                                    
                                    if (ch == '\0') {  
                                        haveMistake = true;  
                                        break;  
                                    }  
                                    for (int k = 16; k <= 18; k++) { 
                                        char dfastr[] = DFA.get(state).toCharArray();  
                                        if (noteDFA(ch, dfastr[k])) {  
                                            token += ch;  
                                            state = k;  
                                            break;  
                                        }  
                                    }  
                                }
                                State = state;
                            //if '*'
                            }
                        	if(haveMistake || State != 18)
                        	{
                                --i;
                        	}
                        	else
                        	{
                        		DefaultTableModel tableModel1 = (DefaultTableModel) jtable1.getModel();
                                tableModel1.addRow(new Object[] {token, "<"+token+",_>","注释"});
                                tokens.add(token);
                                linecount.add(String.valueOf(m));
                                kind.add("NOTE");
                                jtable1.invalidate();
                        	}
                        	token = "";
                        }	
					}
				}
			}
		}
		resut.add(tokens);
		resut.add(kind);
		resut.add(linecount);
		return resut;
    }

	public static Boolean isAlpha(char ch)
	{
	   if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_')){
		  return true;
	   }else{
		   return false;
	   }
	}

	public static Boolean isDigit(char ch)
	{  
        if( (ch >= '0' && ch <= '9')){
        	return true;
        }else{
        	return false;
        }
    }
	public static Boolean isOp(char ch) 
    {  
        for (int i = 0; i < operator.length; i++)  
            if (ch == operator[i]) {  
                return true;  
            }  
        return false;  
    }
  public static Boolean isbordar(char ch){
	  for(int i=0;i<boundary.length;i++){
		  if(ch==boundary[i]){
			  return true;
		  }
	  }
	  return false;
  }
	public static Boolean isMatchKeyword(String str) {  
        Boolean flag = false;  
        for (int i = 0; i < keywords.length; i++) {  
            if (str.equals(keywords[i])) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    }
	public static Boolean Canequalop(char ch)   
    {  
        if( ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '='  || ch == '>' || ch == '<' || ch == '&' || ch == '|'  || ch == '^'){
        	return true;
        }else{
        	return false;
        }
    }
	public static Boolean isPlusSame(char ch)   
    {  
       if(ch == '+' || ch == '-' || ch == '&' || ch == '|'||ch=='='){ 
    	   return true;
       }else{
    	   return false;
       }
    }
	public static Boolean istrunwd(char ch) {  
        if( ch == 'a' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't' || ch == 'v' || ch == '?' || ch == '0')
        	return true;  
        else {
			return false;
		}
    }
	public static Boolean opDFA(char ch,char key){
		if(key=='g'){
			if(isPlusSame(ch))
				return true;
		}else if(key=='h'){
			if(Canequalop(ch))
				return true;
		}else if(key=='i'){
			if(ch=='!'||ch=='~')
				return true;
		}else if(key=='='){
			if(ch=='=')
				return true;
		}
		return false;
	}
	public static Boolean letterDFA(char ch,char key){
		if(ch!='\0'&&(isAlpha(ch)||isDigit(ch))){
		return true;
		}
		else {
			return false;
		}
	}
	public static Boolean stringDFA(char ch, char key) {  
        if (key == 'a')  
            return istrunwd(ch);  
        if (key == '\\')  
            return ch == key;  
        if (key == '"')  
            return ch == key;  
        if (key == 'b')  
            return ch != '\\' && ch != '"';  
        return false;  
    }
	public static Boolean charDFA(char ch, char key) {  
        if (key == 'a')  
            return istrunwd(ch);  
        if (key == '\\')  
            return ch == key;  
        if (key == '\'')  
            return ch == key;  
        if (key == 'b')  
            return ch != '\\' && ch != '\'';  
        return false;  
    }
	public static int digitDFA(char ch, char test) 
	{  
        if (test == 'd') {  
            if (isDigit(ch))  
                return 1;  
            else  
                return 0;  
        }  
        else
        {
        	if (ch == test)
        		return 1;
        	else
        		return 0;
        }
    }
	public static Boolean noteDFA(char ch, char nD) {   
            if (nD == 'c') 
            {  
                if (ch != '*') return true;  
                else return false;  
            }    
            if (nD == 'f') {  
                if (ch != '*' && ch != '/') return true;  
                else return false;  
            }  
        return ch == nD;  
    }
}