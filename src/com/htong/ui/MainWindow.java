package com.htong.ui;


import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htong.communication.CommunicationController;
import com.htong.domain.ElecDataMSSQL;
import com.htong.domain.SQLData;
import com.htong.domain.SQLWell;
import com.htong.idataserver.DataServerController;
import com.htong.persist.MySessionFactory;
import com.htong.persist.SQLDataDao;
import com.htong.persist.SQLWellDao;
import com.htong.status.RunStatus;
import com.htong.util.DateUtil;

public class MainWindow extends ApplicationWindow {
	private static Text text;
	private static Text text_1;
	private Button suoyou;
	private Button zhiding;
	private Button start;
	private Button stop;
	private Button zhuangtai;
	private Label qidongtime;
	private Text text_dtu;
	private Text text_device;
	
	private  Tray tray;

	/**
	 * Create the application window.
	 */
	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		setShellStyle(SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);

	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		group.setText("自动采集");

		start = new Button(group, SWT.NONE);

		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addText("启动采集程序...");
				System.out.println("启动采集程序...");

				DataServerController.INSTANCE.startServer();

				start.setEnabled(false);
				stop.setEnabled(true);
				zhiding.setEnabled(true);
				suoyou.setEnabled(true);
				zhuangtai.setEnabled(true);
				qidongtime.setText(DateUtil.getCurrentTime());
				addText("采集程序已启动。");
				
//				SQLWell well = new SQLWell();
//				SQLWellDao wellDao = new SQLWellDao();
//				well.setWellnum("123");
//				well.setBengjing(123);
//				well.setBengshen(0.455f);
//				well.setHanshui(96.5f);
//				well.setNiandu(4789.3f);
//				well.setDtu("0091");
//				wellDao.insert(well);
//				
//				SQLData data = new SQLData();
//				SQLDataDao dataDao = new SQLDataDao();
//				data.setChongcheng(0.12f);
//				data.setChongci(0.23f);
//				data.setWeiyi("sddd");
//				data.setZaihe("dfd");
//				data.setTime(new Date());
//				data.setSqlWell(well);
//				dataDao.insert(data);
				
			}
		});
		start.setBounds(88, 15, 90, 35);
		start.setText("启动采集");

		stop = new Button(group, SWT.NONE);
		stop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stop.setEnabled(false);

				start.setEnabled(true);
				zhiding.setEnabled(false);
				suoyou.setEnabled(false);
				zhuangtai.setEnabled(false);
				if (RunStatus.Instance.isRun()) {
					addText("停止采集程序...");
					DataServerController.INSTANCE.stopServer();
				}

				addText("采集程序已停止。");
			}
		});
		stop.setBounds(210, 15, 90, 35);
		stop.setText("停止采集");
		stop.setEnabled(false);

		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		group_1.setText("主动采集");



		Label lblDtu = new Label(group_1, SWT.NONE);
		lblDtu.setBounds(44, 48, 42, 12);
		lblDtu.setText("DTU号：");

		suoyou = new Button(group_1, SWT.NONE);
		suoyou.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						CommunicationController.INSTANCE.executeOneQuery();
					}
				});
			}
		});
		suoyou.setBounds(255, 10, 103, 27);
		suoyou.setText("采集所有油井");
		suoyou.setEnabled(false);

		zhiding = new Button(group_1, SWT.NONE);
		zhiding.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String dtu = text_dtu.getText().trim();
				final String device = text_device.getText().trim();
				if("".equals(dtu)) {
					MessageDialog.openError(getShell(), "错误", "DTU号不能为空！");
					return;
				}
				try {
					int temp = Integer.parseInt(dtu);
				} catch (NumberFormatException e1) {
					MessageDialog.openError(getShell(), "错误", "非法DTU号！");
					e1.printStackTrace();
					return;
				}
				
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						CommunicationController.INSTANCE.executeOneQuery(dtu, device);
						
					}
				});
			}
		});
		zhiding.setBounds(255, 41, 103, 27);
		zhiding.setText("采集指定油井");
		zhiding.setEnabled(false);
		
		text_dtu = new Text(group_1, SWT.BORDER);
		text_dtu.setBounds(86, 45, 42, 18);
		
		Label label_1 = new Label(group_1, SWT.NONE);
		label_1.setBounds(143, 48, 54, 12);
		label_1.setText("仪表地址：");
		
		text_device = new Text(group_1, SWT.BORDER);
		text_device.setBounds(203, 45, 42, 18);

		Group group_2 = new Group(container, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		group_2.setText("状态");

		zhuangtai = new Button(group_2, SWT.NONE);
		zhuangtai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new DTUStatusDialog(getShell()).open();
			}
		});
		zhuangtai.setBounds(152, 18, 99, 28);
		zhuangtai.setText("DTU状态查询");
		zhuangtai.setEnabled(false);

		Group group_3 = new Group(container, SWT.NONE);
		group_3.setLayout(new GridLayout(3, false));
		group_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		group_3.setText("信息");

		Label label = new Label(group_3, SWT.NONE);
		label.setText("启动时间：");

		qidongtime = new Label(group_3, SWT.NONE);
		GridData gd_qidongtime = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_qidongtime.widthHint = 150;
		qidongtime.setLayoutData(gd_qidongtime);

		Button btnNewButton_2 = new Button(group_3, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnNewButton_2.widthHint = 70;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.setText("");
			}
		});
		btnNewButton_2.setText("清空显示");

		text_1 = new Text(group_3, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL
				| SWT.MULTI);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		tray = Display.getCurrent().getSystemTray();
		

		// 弹出式菜单
		final Menu trayMenu = new Menu(getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(trayMenu, SWT.PUSH);
		menuItem.setText("退出系统");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (MessageDialog
						.openConfirm(getShell(), "提示", "确定要关闭数据采集软件吗？")) {
					closeWindow();
				}
			}
		});

		final TrayItem item = new TrayItem(tray, SWT.NONE);
		item.setToolTipText("采油井数据采集软件");
		item.setImage(ResourceManager.getPluginImage("ofw-idataserver",
				"icons/huatong.ico"));
		item.addListener(SWT.Selection, new Listener() {// 单击托盘
					@Override
					public void handleEvent(Event arg0) {
						if (!getShell().isVisible()) {
							getShell().setMinimized(false);
							getShell().setVisible(true);
							
						}

					}
				});
		item.addListener(SWT.MenuDetect, new Listener() {// 右击托盘
					@Override
					public void handleEvent(Event arg0) {
						// TODO Auto-generated method stub
						trayMenu.setVisible(true);
					}
				});

		//最小化时
		getShell().addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
				
				getShell().setVisible(false);
			}
			

		});
		


		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MainWindow window = new MainWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setImage(ResourceManager.getPluginImage("ofw-idataserver",
				"icons/huatong.ico"));
		super.configureShell(newShell);
		newShell.setText("油井数据采集软件2013-1-3");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(438, 412);
	}

	@Override
	public boolean close() {
		getShell().setVisible(false);
		return false;
//		if (RunStatus.Instance.isRun()) {
//			DataServerController.INSTANCE.stopServer();
//		}
//
//		return super.close();
	}
	
	public void closeWindow() {
		if (RunStatus.Instance.isRun()) {
			DataServerController.INSTANCE.stopServer();
		}
		tray.dispose();
		super.close();
	}

	public static synchronized void addText(String appendText) {
		text_1.append(DateUtil.getCurrentTime() + " :" + appendText + "\r\n");
	}
}
