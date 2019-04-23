package grammercomplier;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.ExpandVetoException;


public class UI extends JFrame {
	private JMenu jMenu1;//����ѡ���ļ��Ĳ˵�
	private JMenu jMenu2;//���������Ĳ˵�
	private JMenuBar jMenuBar1;//װmenu1
	private JMenuItem jMenuItem1;//ѡ�����
	private JMenuItem jMenuItem2;//��ʼ����
	private JMenuItem jMenuItem3;//���������
	private   JMenuItem jMenuItem4;//ѡ��DFA
	private   JMenuItem jMenuItem5;//�﷨����
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane4;
	private JTable jTable1;
	private JTable jTable2;
	private JTable jTable3;
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
		jScrollPane4=new JScrollPane();
		jTable1 = new JTable();
		jTable2=new  JTable();
		jTable3=new JTable();
		jMenuBar1 = new JMenuBar();
		jMenu1 = new JMenu();
		jMenuItem1 = new JMenuItem();
		jMenuItem3 = new JMenuItem();
		jMenuItem4 = new JMenuItem();
		jMenuItem5=new JMenuItem();
		jMenu2 = new JMenu();
		jMenuItem2 = new JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setFocusable(false);

		jTextArea1.setColumns(15);
		jTextArea1.setRows(5);
		jTextArea2.setColumns(15);
		jTextArea2.setRows(5);
		jScrollPane3.setViewportView(jTextArea2);
		jTable2.setModel(new DefaultTableModel(new Object[][]{}, new String[]{" ","digit","'","\"","/","letter_",".","e","-","\\","b",
				"ת���ַ�","*","��*��all","��*,/��all","���ظ�op","�ɼ�=op","����op","="}));
		jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		importJTA();
		jScrollPane1.setViewportView(jTable2);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jTable1.setModel(new DefaultTableModel(
				new Object[][] {}, new String[] { "����","TOKEN", "���"}) {
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane2.setViewportView(jTable1);
		jTable3.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "TOKEN","�к�", "ԭ��"}));
		jScrollPane4.setViewportView(jTable3);
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
		jMenuItem5.setText("�﷨����");
		jMenuItem5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				beginsyntax(e);
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
		jMenu2.add(jMenuItem5);
		jMenuBar1.add(jMenu2);
		setJMenuBar(jMenuBar1);
		this.setLayout(new GridLayout(2, 2));
		this.add(jScrollPane1);
		this.add(jScrollPane3);
		this.add(jScrollPane2);
		this.add(jScrollPane4);
		pack();
	}
	private void beginsyntax(java.awt.event.ActionEvent evt){
		ArrayList<ArrayList<String>> arrayList1=new ArrayList<>();
		String program = jTextArea2.getText();
		// ���ԭ����
		DefaultTableModel tableModel1 = (DefaultTableModel) jTable1.getModel();
		tableModel1.setRowCount(0);
		jTable1.invalidate();
		// ���ԭ����
		DefaultTableModel tableModel2 = (DefaultTableModel) jTable3.getModel();
		tableModel2.setRowCount(0);
		jTable3.invalidate();
		//�����ʷ�������
		Latex latex = new Latex(program, jTable1,arrayList);
		arrayList1=latex.analyze();
		syntax syntax1=new syntax(arrayList1.get(0), arrayList1.get(1), arrayList1.get(2), jTable3);
		syntax1.parse();
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
				jTextArea1.append(aline+"\r\n");
				arrayList.add(aline);
			}
			//importJTA();
			fileReader.close();
			bufferedReader.close();
			return arrayList;
		}catch(IOException e){
			System.out.println(e);
		}
		return null;
	}
	private void importJTA(){
		DefaultTableModel defaultTableModel=(DefaultTableModel)jTable2.getModel();
		defaultTableModel.addRow(new Object[]{"0","1","8","11","-1","11","15","19","-1","-1","-1","-1","-1","-1","20","21","22","-1"});
		defaultTableModel.addRow(new Object[]{'1',"1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'2',"3","-1","-1","-1","-1","2","4","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'3',"3","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'4',"6","-1","-1","-1","-1","-1","4","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'5',"6","-1","-1","-1","-1","5","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'6',"6","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'7',"-1","-1","-1","-1","-1","-1","-1","-1","8","9","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'8',"-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","9","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{'9',"-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"10","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"11","-1","-1","-1","-1","-1","-1","-1","-1","12","13","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"12","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","13","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"13","-1","-1","14","-1","-1","-1","-1","-1","12","13","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"14","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"15","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","16","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"16","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","17","16","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"17","-1","-1","-1","18","-1","-1","-1","-1","-1","-1","-1","17","-1","16","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"18","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"19","18","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object[]{"20","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","23","-1"});
		defaultTableModel.addRow(new Object[]{"21","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","24"});
		defaultTableModel.addRow(new Object[]{"22","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object []{"23","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
		defaultTableModel.addRow(new Object []{"24","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"});
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