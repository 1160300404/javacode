package pack;

import java.util.ArrayList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import querysql.sqlop;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectUI extends ApplicationWindow {
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text txtAaaa;
	private Text text;

	/**
	 * Create the application window.
	 */
	public SelectUI() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		{
		
			Button btnCheckButton_7 = new Button(container, SWT.CHECK);
			btnCheckButton_7.setBounds(10, 10, 69, 17);
			btnCheckButton_7.setText("\u4ED3\u5E93\u4FE1\u606F");
		
			Button btnCheckButton_8 = new Button(container, SWT.CHECK);
			btnCheckButton_8.setBounds(10, 56, 117, 17);
			btnCheckButton_8.setText("\u91CD\u540D\u8005\u7684\u5E73\u5747\u5E74\u9F84");
			Button button = new Button(container, SWT.CHECK);
			button.setBounds(10, 33, 61, 17);
			button.setText("\u51FA\u8D27\u5355");
			Button btnNewButton = new Button(container, SWT.NONE);
			
			text_1 = new Text(container, SWT.BORDER);
			text_1.setBounds(194, 7, 73, 23);
			
			text_2 = new Text(container, SWT.BORDER);
			text_2.setBounds(194, 30, 73, 23);
			
			text_3 = new Text(container, SWT.BORDER);
			text_3.setBounds(194, 53, 73, 23);
			
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setBounds(85, 10, 61, 17);
			lblNewLabel.setText("\u8D27\u7269\u6570\u91CF");
			
			Label lblNewLabel_1 = new Label(container, SWT.NONE);
			lblNewLabel_1.setBounds(88, 33, 100, 17);
			lblNewLabel_1.setText("\u51FA\u8D27\u4ED3\u5E93\u7BA1\u7406\u4EBA");
			
			Label label = new Label(container, SWT.NONE);
			label.setBounds(133, 56, 61, 17);
			label.setText("\u90E8\u95E8\u53F7");
			Button btnCheckButton = new Button(container, SWT.CHECK);
			btnCheckButton.setBounds(10, 79, 98, 17);
			btnCheckButton.setText("\u90E8\u95E8\u4E0E\u5BF9\u5E94\u4ED3\u5E93");
			Button btnid = new Button(container, SWT.CHECK);
			btnid.setBounds(10, 100, 152, 17);
			btnid.setText("\u67E5\u8BE20304\u90E8\u95E8\u51FA\u8D27\u5355");
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					txtAaaa.setText("");
					text.setText("");
					if(btnCheckButton_7.getSelection()){
						ArrayList<String> arrayList=sqlop.connectSelect(text_1.getText());
						text.setText("Select * from warehouse natural storerecord where count="+text_1.getText());
						txtAaaa.setText("houseid pid housename departid goodsid\t\n");
						for(int i=0;i<arrayList.size();i++){
							txtAaaa.append(arrayList.get(i)+"\t\n\r");
						}
					}else if(button.getSelection()){
						ArrayList<String> arrayList=sqlop.embedSelect(text_2.getText());
						text.setText("Select * from outtable where wid in (Select warehouseid from warehouse where pid in (Select pid from administrator where name = "+text_2.getText()+"))");
						txtAaaa.append("outid wid departid\t\n\r");
						for(int i=0;i<arrayList.size();i++){
							txtAaaa.append(arrayList.get(i)+"\t\n\r");
						}
					}else if(btnCheckButton_8.getSelection()){
						ArrayList<String> arrayList=sqlop.groupSelect(text_3.getText());
						System.out.println(arrayList);
						text.setText("Select AVG(age) from administrator where departid =  "+text_3.getText()+" Group by name");
						txtAaaa.setText("average\t\n");
						for(int i=0;i<arrayList.size();i++){
							txtAaaa.append(arrayList.get(i)+"\t\n\r");
						}
					}else if(btnCheckButton.getSelection()){
						ArrayList<String> arrayList=sqlop.useview();
						text.setText("select dename,housename from 部门与对应仓库");
						txtAaaa.append("dename  housename\t\n");
						for(int i=0;i<arrayList.size();i++){
							txtAaaa.append(arrayList.get(i)+"\t\n\r");
						}
					}else if(btnid.getSelection()){
						ArrayList<String> arrayList=sqlop.useindex();
						text.setText("select outid,wid from outtable where departid = '0304'  ");
						txtAaaa.append("outid  wid\t\n");
						for(int i=0;i<arrayList.size();i++){
							txtAaaa.append(arrayList.get(i)+"\t\n\r");
						}
					}
				}
			});
			btnNewButton.setBounds(344, 56, 80, 27);
			btnNewButton.setText("\u67E5\u8BE2");
			
		}
		
		txtAaaa = new Text(container, SWT.BORDER|SWT.MULTI);
		txtAaaa.setEditable(false);
		txtAaaa.setBounds(10, 163, 414, 69);
		
		text = new Text(container, SWT.BORDER|SWT.MULTI);
		text.setBounds(10, 123, 414, 34);
		
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setBounds(10, 79, 98, 17);
		btnCheckButton.setText("\u90E8\u95E8\u4E0E\u5BF9\u5E94\u4ED3\u5E93");
		
		Button btnid = new Button(container, SWT.CHECK);
		btnid.setBounds(10, 100, 152, 17);
		btnid.setText("\u67E5\u8BE2\u6240\u6709\u7BA1\u7406\u4EBAid\u4E0E\u59D3\u540D");
		
		
        
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}



	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			SelectUI window = new SelectUI();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
