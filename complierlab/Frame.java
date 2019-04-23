package wordcomplier;
import wordcomplier.Latex.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.ExpandVetoException;


public class Frame extends JFrame {
	private JMenu jMenu1;//����ѡ���ļ��Ĳ˵�
	private JMenu jMenu2;//���������Ĳ˵�
	private JMenuBar jMenuBar1;//װmenu1
	private JMenuItem jMenuItem1;//ѡ�����
	private JMenuItem jMenuItem2;//��ʼ����
	private JMenuItem jMenuItem3;//���������
	private   JMenuItem jMenuItem4;//ѡ��DFA
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JTable jTable1;
	private JTextArea jTextArea1;
	private JTextArea jTextArea2;
   private ArrayList<String> arrayList;
  
	/** Creates new form Frame */
	public UI() {
		init();
	}


	private void init() {

		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jTextArea2=new JTextArea();
		jScrollPane2 = new JScrollPane();
		jScrollPane3 = new JScrollPane();
		jTable1 = new JTable();
		jMenuBar1 = new JMenuBar();
		jMenu1 = new JMenu();
		jMenuItem1 = new JMenuItem();
		jMenuItem3 = new JMenuItem();
		jMenuItem4 = new JMenuItem();
		jMenu2 = new JMenu();
		jMenuItem2 = new JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setFocusable(false);

		jTextArea1.setColumns(15);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);
		jTextArea2.setColumns(15);
		jTextArea2.setRows(5);
		jScrollPane3.setViewportView(jTextArea2);
		jTable1.setModel(new DefaultTableModel(
				new Object[][] {}, new String[] { "����","TOKEN", "���"}) {
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane2.setViewportView(jTable1);
		jMenu1.setText("ѡ���ļ�");
		jMenuItem1.setText("�������");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				importtest(evt);
			}
		});
		jMenu1.add(jMenuItem1);
		jMenuItem3.setText("���");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearall(evt);
			}
		});
		jMenuItem4.setText("����DFA");
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				arrayList=importDFA(evt);
			}
		});
		jMenu1.add(jMenuItem4);
		jMenu1.add(jMenuItem3);
		jMenuBar1.add(jMenu1);
		jMenu2.setText("����");
		jMenuItem2.setText("��ʼ����");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				beginanalyze(evt);
			}
		});
		jMenu2.add(jMenuItem2);
		jMenuBar1.add(jMenu2);
		setJMenuBar(jMenuBar1);
		this.setLayout(new GridLayout(1, 3));
		this.add(jScrollPane1);
		this.add(jScrollPane3);
		this.add(jScrollPane2);
		pack();
	}

	private void beginanalyze(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		String program = jTextArea2.getText();
		// ���ԭ����
		DefaultTableModel tableModel1 = (DefaultTableModel) jTable1.getModel();
		tableModel1.setRowCount(0);
		jTable1.invalidate();
		//�����ʷ�������
		Latex latex = new Latex(program, jTable1,arrayList);
		latex.analyze();
	}

	private void clearall(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		jTextArea1.setText("");
	}
   
	private ArrayList<String> importDFA(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		JFileChooser jFileChoose=new JFileChooser();
		jFileChoose.setCurrentDirectory(new File("."));
		jFileChoose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//�����ļ���Ŀ¼����ѡ��
		Frame frame=null;
		jFileChoose.showOpenDialog(frame);
		File file=jFileChoose.getSelectedFile();
		jTextArea1.setText("");
		try{
			FileReader fileReader=new FileReader(file);
			BufferedReader bufferedReader=new BufferedReader(fileReader);
			String aline;
			ArrayList<String> arrayList=new ArrayList<String>();
			while ((aline = bufferedReader.readLine()) != null){
				arrayList.add(aline);
				jTextArea1.append(aline + "\r\n");
			}
			fileReader.close();
			bufferedReader.close();
			return arrayList;
		}catch(IOException e){
			System.out.println(e);
		}
		return null;
	}
	
	private void importtest(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:

		JFileChooser jFileChoose=new JFileChooser();
		jFileChoose.setCurrentDirectory(new File("."));
		jFileChoose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//�����ļ���Ŀ¼����ѡ��
		Frame frame=null;
		jFileChoose.showOpenDialog(frame);
		File file=jFileChoose.getSelectedFile();
		try {
			//��textarea���
			jTextArea2.setText("");
			FileReader filereader = new FileReader(file);
			BufferedReader bufferreader = new BufferedReader(filereader);
			String aline;
			while ((aline = bufferreader.readLine()) != null)
				jTextArea2.append(aline + "\r\n");
			filereader.close();
			bufferreader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String args[]) {
		new UI().setVisible(true);
	}
}