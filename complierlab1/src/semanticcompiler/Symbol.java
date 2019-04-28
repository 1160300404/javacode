package semanticcompiler;

public class Symbol {
	public String name;//符号的名字
	public String type;// 符号的类型
	public int offset;//符号的偏移量
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Symbol){
			if(((Symbol)obj).name.equals(this.name)){
				return true;
			}else {
				return false;
			}
		}else {
			return true;
		}
		
	}
}
