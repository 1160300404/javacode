package grammercomplier;

import java.util.ArrayList;

public class Production {
		private String left;//����ʽ��
		private String[] right;//����ʽ�Ҳ�,�����ַ�����ַ���ʵ����̫�鷳���Զ������ַ���������
		public ArrayList<String > select=new ArrayList<>();//����ʽ��select��
		public Production(String left,String[] right){
			this.left=left;
			this.right=right;
		}
		//�õ�����ʽ����
		public String getleft(){
			return left;
		}
		//�õ�����ʽ���Ҳ�
		public String[] getright(){
			return right;
		}
		//�õ�����ʽ�Ҳ��ַ�����ʽ
		public String getrightstr(){
			StringBuilder stringBuilder=new StringBuilder();
			for(int i=0;i<right.length;i++){
				stringBuilder.append(right[i]);
			}
			return stringBuilder.toString();
		}
}
