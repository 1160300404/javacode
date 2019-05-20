package pack;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import querysql.sqlop;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DelectData extends ApplicationWindow {
	private String tname=null;
	private Text text;
	/**
	 * Create the application window.
	 */
	public DelectData(String tname) {
		super(null);
		createActions();
		this.tname=tname;
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
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 15, SWT.NORMAL));
			lblNewLabel.setBounds(110, 27, 180, 33);
			lblNewLabel.setText("\u8BF7\u8F93\u5165\u8981\u5220\u9664\u7684\u6761\u4EF6");
		}
		{
			Label lblNewLabel_1 = new Label(container, SWT.NONE);
			lblNewLabel_1.setAlignment(SWT.CENTER);
			lblNewLabel_1.setBounds(21, 66, 390, 17);
			if(tname.equals("administrator")){
				lblNewLabel_1.setText("pid;departid;name;age;sex");
			}
			else if(tname.equals("department")){
				lblNewLabel_1.setText("departid;dename");
			}else if(tname.equals("goods")){
				lblNewLabel_1.setText("goodsid;goodsname;price;suid");
			}else if (tname.equals("intable")) {
				lblNewLabel_1.setText("inid;houseid;departid");
			}else if (tname.equals("outtable")) {
				lblNewLabel_1.setText("outid;wid;departid");
			}else if (tname.equals("storerecord")) {
				lblNewLabel_1.setText("warehouseid;goodsid;count");
			}else if (tname.equals("supplier")) {
				lblNewLabel_1.setText("suid;sname");
			}else if (tname.equals("warehouse")) {
				lblNewLabel_1.setText("houseid;pid;housename;departid");
			}
		}
		{
			text = new Text(container, SWT.BORDER);
			text.setBounds(75, 103, 250, 23);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String result=null;
					String[] strings=text.getText().trim().split(";");
					if(strings.length==2){
						result=sqlop.delete(tname, strings[0], strings[1]);
					}else if (strings.length==3) {
						result=sqlop.delete(tname, strings[0], strings[1],strings[2]);
					}else if (strings.length==4) {
						result=sqlop.delete(tname, strings[0], strings[1],strings[2],strings[3]);
					}else if (strings.length==5) {
						result=sqlop.delete(tname, strings[0], strings[1],strings[2],strings[3],strings[4]);
					}
					MessageBox dialog=new MessageBox(new Shell(),SWT.OK);
			        dialog.setText("ÌבÊ¾");
			        dialog.setMessage(result);
			        dialog.open();
			        text.setText("");
				}
			});
			button.setBounds(167, 148, 80, 27);
			button.setText("\u786E\u5B9A");
		}
		
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
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
