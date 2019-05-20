package pack;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DelectUI extends ApplicationWindow {
	private Text text;

	/**
	 * Create the application window.
	 */
	public DelectUI() {
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
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 15, SWT.NORMAL));
		lblNewLabel.setBounds(139, 21, 120, 27);
		lblNewLabel.setText("\u8F93\u5165\u5220\u9664\u8868\u540D");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(129, 73, 139, 27);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String string=null;
				string=text.getText();
				if(string.equals("")){
					MessageBox dialog=new MessageBox(new Shell(),SWT.OK);
			        dialog.setText("警告");
			        dialog.setMessage("输入为空");
			        dialog.open();
				}else if (!string.equals("administrator")&&!string.equals("department")&&!string.equals("goods")&&!string.equals("intable")&&!string.equals("outtable")&&!string.equals("storerecord")&&!string.equals("supplier")&&!string.equals("warehouse")) {
					MessageBox dialog=new MessageBox(new Shell(),SWT.OK);
			        dialog.setText("警告");
			        dialog.setMessage("没有这个表");
			        dialog.open();
			        text.setText("");
				}else{
					try {
						DelectData window = new DelectData(string);
						window.setBlockOnOpen(true);
						window.open();
						 text.setText("");
						//Display.getCurrent().dispose();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(158, 127, 80, 27);
		btnNewButton.setText(" \u786E\u5B9A");

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
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DelectUI window = new DelectUI();
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
