package semanticcompiler;

public class Symbol {
	public String name;//���ŵ�����
	public String type;// ���ŵ�����
	public int offset;//���ŵ�ƫ����
	
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
