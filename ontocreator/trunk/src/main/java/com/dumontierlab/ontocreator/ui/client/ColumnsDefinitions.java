package com.dumontierlab.ontocreator.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.rpc.DataService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.dumontierlab.ontocreator.ui.client.util.RpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;

public class ColumnsDefinitions extends Composite implements WizardTab {

	private final VerticalPanel defintionsPanel;
	private final Menu columnsMenu;
	private final RpcCommand getColumnsNamesCommand;
	private final Map<String, ColumnItem> columnItems;
	private final List<ColumnDefition> definitions;

	private final Set<ColumnMappingBean> mappings;

	public ColumnsDefinitions(Set<ColumnMappingBean> mappings) {
		this.mappings = mappings;
		columnItems = new HashMap<String, ColumnItem>();
		definitions = new ArrayList<ColumnDefition>();
		defintionsPanel = new VerticalPanel();
		columnsMenu = new Menu();
		getColumnsNamesCommand = createGetColumnNamesCommand();
		initWidget(createUi());
	}

	public String getTabName() {
		return "Step 2: Column definitions";
	}

	public String getTabCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	public void initialize() {
		columnItems.clear();
		columnsMenu.removeAll();
		getColumnsNamesCommand.call();
	}

	public void complete(final AsyncCallback<Boolean> callback) {
		for (ColumnDefition definition : definitions) {
			ColumnMappingBean bean = definition.getColumnMappingBean();
			if (bean == null) {
				callback.onSuccess(false);
				return;
			}
			mappings.add(bean);
		}
		callback.onSuccess(true);

	}

	protected void onComlumnItemSelected(ColumnItem columnItem) {
		ColumnDefition definition = new ColumnDefition(columnItem.getColumnIndex(), columnItem.getColumnName(),
				columnItem.incrementMappingCounter());
		definition.setWidth("100%");
		defintionsPanel.add(definition);
		definitions.add(definition);
	}

	private Widget createUi() {
		defintionsPanel.setSize("100%", "100%");
		defintionsPanel.setSpacing(11);

		Button createMappingButton = new Button("New Column Mapping");
		createMappingButton.setMenu(columnsMenu);
		columnsMenu.addListener(new MenuListenerAdapter() {
			@Override
			public void onItemClick(BaseItem item, EventObject e) {
				ColumnItem columnItem = columnItems.get(item.getItemId());
				if (columnItem != null) {
					onComlumnItemSelected(columnItem);
				}

			}
		});

		defintionsPanel.add(createMappingButton);
		return defintionsPanel;
	}

	private RpcCommand createGetColumnNamesCommand() {
		RpcCommand command = new OnRequestRpcCommand<List<String>>() {
			@Override
			protected void rpcCall(AsyncCallback<List<String>> callback) {
				DataService.Util.getInstance().getColumns(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			}

			@Override
			protected void rpcReturn(List<String> result) {
				int index = 0;
				for (String column : result) {
					ColumnItem columnItem = new ColumnItem(index++, column);
					Item item = new Item(column);
					columnItems.put(item.getItemId(), columnItem);
					columnsMenu.addItem(item);
				}
			}
		};
		return command;
	}

	private static class ColumnItem {

		private final int columnIndex;
		private final String columnName;
		private int mappingCounter;

		public ColumnItem(int columnIndex, String columnName) {
			this.columnIndex = columnIndex;
			this.columnName = columnName;
		}

		public int getColumnIndex() {
			return columnIndex;
		}

		public String getColumnName() {
			return columnName;
		}

		public int incrementMappingCounter() {
			return mappingCounter++;
		}

	}
}
