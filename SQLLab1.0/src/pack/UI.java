package pack;
import java.awt.*;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class UI extends ApplicationWindow {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create the application window.
	 */
	public UI() {
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
		container.setBounds(10, 10, 434, 185);
		container.setLayout(null);
		
		Button button = formToolkit.createButton(container, "\u63D2\u5165", SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					InsertUI window = new InsertUI();
					window.setBlockOnOpen(true);
					window.open();
					//Display.getCurrent().dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(156, 10, 80, 27);
		
		Button button_1 = formToolkit.createButton(container, "\u5220\u9664", SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DelectUI delectUI=new DelectUI();
				delectUI.setBlockOnOpen(true);
				delectUI.open();
			}
		});
		button_1.setBounds(156, 56, 80, 27);
		
		Button button_2 = formToolkit.createButton(container, "\u67E5\u8BE2", SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectUI selectUI=new SelectUI();
				selectUI.setBlockOnOpen(true);
				selectUI.open();
			}
		});
		button_2.setBounds(156, 109, 80, 27);
		
		Button button_3 = formToolkit.createButton(container, "\u9000\u51FA", SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Display.getCurrent().dispose();
				Display.getCurrent().close();
			}
		});
		button_3.setBounds(156, 158, 80, 27);

		return container;
	}
    private void createInsert(Composite parent){
    	Composite container = new Composite(parent, SWT.NONE);
		container.setBounds(10, 10, 434, 185);
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
			UI window = new UI();
			window.setBlockOnOpen(true);
			window.open();
			//Display.getCurrent().dispose();
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
		newShell.setText("仓库管理系统");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
