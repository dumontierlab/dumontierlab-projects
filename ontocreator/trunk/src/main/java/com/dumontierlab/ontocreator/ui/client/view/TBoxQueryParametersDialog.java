package com.dumontierlab.ontocreator.ui.client.view;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class TBoxQueryParametersDialog extends Window {

	enum QueryType {
		SUBCLASS("SubClassOf"), SUPERCLASS("SuperClassOf"), EQUIVALENT_CLASS("EquivalentClassOf");

		private final String label;

		private QueryType(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private final Listener listener;
	private final TextArea queryTextArea;
	private final ListBox queryTypeList;

	public TBoxQueryParametersDialog(Listener listener) {
		super("TBox Query Filter", true, false);
		this.listener = listener;
		queryTextArea = new TextArea();
		queryTypeList = new ListBox();
		initQueryTypeList();
		setLayout(new FitLayout());
		createUi();
		setClosable(true);
		setPaddings(8);
		setWidth(400);
		setHeight(175);
		setCloseAction(Window.CLOSE);
	}

	private void initQueryTypeList() {
		for (QueryType type : QueryType.values()) {
			queryTypeList.addItem(type.toString(), type.name());
		}
	}

	private void createUi() {
		setLayout(new AnchorLayout());
		Label queryTypelabel = new Label("Query type:");
		Grid grid = new Grid(1, 2);
		grid.setWidget(0, 0, queryTypelabel);
		grid.setWidget(0, 1, queryTypeList);
		add(grid);
		Label queryLabel = new Label("Enter a class expression using the Manchester OWL Syntax");
		queryLabel.setWidth("100%");
		add(queryLabel, new AnchorLayoutData("100%"));
		add(queryTextArea, new AnchorLayoutData("100% -45"));

		addButton(new Button("OK", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				listener.onComplete(queryTypeList.getValue(queryTypeList.getSelectedIndex()), queryTextArea.getText());
				close();
			}
		}));
		addButton(new Button("Cancel", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				close();
			}
		}));
		setButtonAlign(Position.CENTER);
	}

	public static interface Listener {
		void onComplete(String queryType, String query);
	}
}
