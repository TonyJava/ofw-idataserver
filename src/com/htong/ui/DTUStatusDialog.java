package com.htong.ui;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.GridViewerColumnSorter;

import com.htong.domain.WellModel;
import com.htong.idataserver.TagDataBase;
import com.htong.status.DTUStatus;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DTUStatusDialog extends Dialog {
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			WellModel well = (WellModel)element;
			String dtuId = well.getDtuId();
			if(columnIndex == 0) {//井号
				return well.getNum();
			}  else if(columnIndex == 1) {//DTU号
				return dtuId;
			} else if(columnIndex == 2) {//连接状态
				Boolean b = DTUStatus.instance.getDtuStatusMap().get(dtuId);
				if(b == null || b.equals(Boolean.FALSE)) {
					return "未连接";
				} else {
					return "已连接";
				}
			} else if(columnIndex == 3) {//上次心跳时间
				String time = DTUStatus.instance.getHeartBeatMap().get(dtuId);
				if(time != null ) {
					return time;
				} else {
					return "无心跳";
				}
			} else if(columnIndex == 4) {//通讯状态
				Boolean b = DTUStatus.instance.getCommStatusMap().get(dtuId);
				if(b == null || b.equals(Boolean.FALSE)) {
					return "离线";
				} else {
					return "在线";
				}
			}
			
			return "";
		}
	}

	private Grid table;
	private GridTableViewer tableViewer;
	private List<WellModel> wellList;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DTUStatusDialog(Shell parentShell) {
		
		super(parentShell);
		setShellStyle(SWT.MAX);
		init();
	}
	
	private void init() {
		wellList = TagDataBase.INSTANCE.getWellList();
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		tableViewer = new GridTableViewer(container, SWT.BORDER | SWT.V_SCROLL);
		table = tableViewer.getGrid();
		table.setRowHeaderVisible(true);
		table.setColumnScrolling(true);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		GridViewerColumn tableViewerColumn_1 = new GridViewerColumn(tableViewer, SWT.NONE);
		GridColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(104);
		tableColumn_1.setText("井号");
		
		GridViewerColumn tableViewerColumn_2 = new GridViewerColumn(tableViewer, SWT.NONE);
		GridColumn tblclmnDtu = tableViewerColumn_2.getColumn();
		tblclmnDtu.setWidth(54);
		tblclmnDtu.setText("DTU号");
		new GridViewerColumnSorter(tableViewerColumn_2) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				WellModel v1 = (WellModel) e1;
				WellModel v2 = (WellModel) e2;
				int i1 = Integer.valueOf(v1.getDtuId());
				int i2 = Integer.valueOf(v2.getDtuId());
				
				return i1-i2;
			}
		};
		
		GridViewerColumn tableViewerColumn_3 = new GridViewerColumn(tableViewer, SWT.NONE);
		GridColumn tableColumn_2 = tableViewerColumn_3.getColumn();
		tableColumn_2.setWidth(85);
		tableColumn_2.setText("连接状态");
		
		GridViewerColumn tableViewerColumn_4 = new GridViewerColumn(tableViewer, SWT.NONE);
		GridColumn tableColumn_3 = tableViewerColumn_4.getColumn();
		tableColumn_3.setWidth(146);
		tableColumn_3.setText("上次心跳时间");
		
		GridViewerColumn tableViewerColumn_5 = new GridViewerColumn(tableViewer, SWT.NONE);
		GridColumn tableColumn_4 = tableViewerColumn_5.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("通讯状态");
		
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(wellList);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.NO_ID, "刷新",
				true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				init();
				tableViewer.refresh();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				"关闭", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(594, 438);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("DTU状态查询");
		super.configureShell(newShell);
	}

}
