package grammercomplier;

import java.util.ArrayList;

public class Production {
		private String left;//产生式左部
		private String[] right;//产生式右部,由于字符变成字符串实在是太麻烦所以都当成字符串处理了
		public ArrayList<String > select=new ArrayList<>();//产生式的select集
		public Production(String left,String[] right){
			this.left=left;
			this.right=right;
		}
		//得到产生式的左部
		public String getleft(){
			return left;
		}
		//得到产生式的右部
		public String[] getright(){
			return right;
		}
		//得到产生式右部字符串形式
		public String getrightstr(){
			StringBuilder stringBuilder=new StringBuilder();
			for(int i=0;i<right.length;i++){
				stringBuilder.append(right[i]);
			}
			return stringBuilder.toString();
		}
}
