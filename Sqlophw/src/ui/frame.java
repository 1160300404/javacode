package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import querysql.query;

public class frame extends JFrame{
		private JPanel jp1;//添加到frame中
		private JPanel jp2;//添加到frame中
		private JPanel jp3;
		private JPanel jp4;//
	    private JPanel jp5;//
	    private JPanel jp6;//
	    private JPanel jp7;//
	    private JPanel jp8;//
	    private JPanel jp9;//
	    private JPanel jp10;//
	    private JPanel jp11;//
	    private JPanel jp12;//
	    private JCheckBox jcb1;//sid
	    private JCheckBox jcb2;//sname
	    private JCheckBox jcb3;//age
	    private JCheckBox jcb4;//sex
	    private JCheckBox jcb5;//class
	    private JCheckBox jcb6;//dept
	    private JCheckBox jcb7;//addr
	    private JTextField jtf1;
	    private JTextField jtf2;
	    private JTextField jtf3;
	    private JTextField jtf4;
	    private JTextField jtf5;
	    private JTextField jtf6;
	    private JTextField jtf7;
	    private JTextField jtf8;
	    private JLabel jLabel;
	    private JButton jb1;//查询
	    private JButton jb2;//清除
	    private JTextArea jta1;//显示sql
	    private JTable jt;//显示查询结果
	    private JScrollPane jsp;//添加到frame中
	    public frame(){
	    	init();
	    }
	    public void init(){
	    	jp1=new JPanel();
	    	jp2=new JPanel();
	    	jp3=new JPanel();
	    	jp4=new JPanel();
	    	jp5=new JPanel();
	    	jp6=new JPanel();
	    	jp7=new JPanel();
	    	jp8=new JPanel();
	    	jp9=new JPanel();
	    	jp10=new JPanel();
	    	jp11=new JPanel();
	    	jp12=new JPanel();
	    	jcb1=new JCheckBox("学号");
	    	jcb2=new JCheckBox("姓名");
	    	jcb3=new JCheckBox("年龄自");
	    	jcb4=new JCheckBox("性别");
	    	jcb5=new JCheckBox("班级");
	    	jcb6=new JCheckBox("院系");
	    	jcb7=new JCheckBox("地址");
	    	jLabel=new JLabel("到");
	    	jtf1=new JTextField();
	    	jtf2=new JTextField();
	    	jtf3=new JTextField();
	    	jtf3.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					int keyChar = e.getKeyChar();							
					if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){}
					else{					
							e.consume(); //关键，屏蔽掉非法输入				
						}			
					}		
				@Override
				public void keyReleased(KeyEvent e) {}	
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	    	jtf4=new JTextField();
	    	jtf4.addKeyListener(new KeyListener() {@Override
				public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyChar = e.getKeyChar();							
				if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){}
				else{					
						e.consume(); //关键，屏蔽掉非法输入				
					}			
				}		
			@Override
			public void keyReleased(KeyEvent e) {}		
			@Override
			public void keyPressed(KeyEvent e) {}
		});
	    	jtf5=new JTextField();
	    	jtf6=new JTextField();
	    	jtf7=new JTextField();
	    	jtf8=new JTextField();
	    	jb1=new JButton("查询");
	    	jb2=new JButton("清除");
	    	jta1=new JTextArea();
	    	jta1.setLineWrap(true);;
	    	jt=new JTable();
	    	jsp=new JScrollPane();
	    	jp3.setLayout(new BorderLayout());
	    	jp4.setLayout(new BorderLayout());
	    	jp5.setLayout(new BorderLayout());
	    	jp6.setLayout(new BorderLayout());
	    	jp7.setLayout(new BorderLayout());
	    	jp8.setLayout(new BorderLayout());
	    	jp9.setLayout(new BorderLayout());
	    	jp3.add(jcb1, BorderLayout.WEST);
	    	jp3.add(jtf1,BorderLayout.CENTER);
	    	jp4.add(jcb2, BorderLayout.WEST);
	    	jp4.add(jtf2,BorderLayout.CENTER);
	    	jp5.add(jcb3, BorderLayout.WEST);
	    	jp10.setLayout(new GridLayout(1, 3));
	    	jp10.add(jtf3);
	    	jp10.add(jLabel);
	    	jp10.add(jtf4);
	    	jp5.add(jp10,BorderLayout.CENTER);
	    	jp6.add(jcb4, BorderLayout.WEST);
	    	jp6.add(jtf5,BorderLayout.CENTER);
	    	jp7.add(jcb5, BorderLayout.WEST);
	    	jp7.add(jtf6,BorderLayout.CENTER);
	    	jp8.add(jcb6, BorderLayout.WEST);
	    	jp8.add(jtf7,BorderLayout.CENTER);
	    	jp9.add(jcb7, BorderLayout.WEST);
	    	jp9.add(jtf8,BorderLayout.CENTER);
	    	jp11.setLayout(new GridLayout(4, 2));
	    	jp11.add(jp3);
	    	jp11.add(jp4);
	    	jp11.add(jp5);
	    	jp11.add(jp6);
	    	jp11.add(jp7);
	    	jp11.add(jp8);
	    	jp11.add(jp9);
	    	
	    	jp12.setLayout(new BorderLayout());
	        jp12.add(jp11,BorderLayout.CENTER);
	        jp12.add(jb1,BorderLayout.EAST);
	        jt.setModel(new DefaultTableModel(
					new Object[][] {

					}, new String[] { "Sid", "Sname","Sage","Ssex","Sclass","Sdept","Saddr" }) {
				boolean[] canEdit = new boolean[] { false, false };

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			});
	        jsp.setViewportView(jt);
	        this.setLayout(new GridLayout(3,1));
	        this.add(jp12);
	        this.add(jta1);
	        this.add(jsp);
	    	this.setTitle("作业");
	    	this.setSize(600,500);
	    	this.setLocation(200, 200);
	    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	jb1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					getresult();
				}
			});
	    	jb2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
						clear();
				}
			});
	    }
	  private void getresult(){
		  clear();
		  String sid=null;
		  String sname=null;
		  String sclass=null;
		  String sdept=null;
		  String ssex=null;
		  String beage=null;
		  String enage=null;
		  String saddr=null;
		  if(jcb1.isSelected())
		 sid=jtf1.getText();
		  if(jcb2.isSelected())
		  sname=jtf2.getText();
		  if(jcb3.isSelected()){
		  beage=jtf3.getText();
		  enage=jtf4.getText();
		  }
		  if(jcb4.isSelected())
		  ssex=jtf5.getText();
		  if(jcb5.isSelected())
		  sclass=jtf6.getText();
		  if(jcb6.isSelected())
		  sdept=jtf7.getText();
		  if(jcb7.isSelected())
		   saddr=jtf8.getText();
		  query.init(sid, sname, sclass, sdept, ssex, beage, enage, saddr);
		  jta1.setText(query.setSqlsen());
		 ArrayList<ArrayList<String>> arrayList=query.doquery();
		 if(arrayList!=null)
		 for(int i=0;i<arrayList.size();i++){
			 DefaultTableModel defaultTableModel=(DefaultTableModel)jt.getModel();
			 defaultTableModel.addRow(new Object[]{arrayList.get(i).get(0),arrayList.get(i).get(1),arrayList.get(i).get(2),arrayList.get(i).get(3),arrayList.get(i).get(4),arrayList.get(i).get(5),arrayList.get(i).get(6)});
		 }
	  }
	  private void clear(){
		  jta1.setText("");
		  DefaultTableModel tableModel1 = (DefaultTableModel) jt.getModel();
			tableModel1.setRowCount(0);
	  }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			frame fr1=new frame();
			fr1.setVisible(true);
	}

}
