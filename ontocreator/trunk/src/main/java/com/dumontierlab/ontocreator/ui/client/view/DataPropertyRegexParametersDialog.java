package com.dumontierlab.ontocreator.ui.client.view;

import java.util.List;

import com.dumontierlab.ontocreator.ui.client.UserMessage;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

public class DataPropertyRegexParametersDialog extends Window {

	private final Listener listener;
	private final TextArea regexTextArea;
	private final ListBox propertiesList;

	public DataPropertyRegexParametersDialog(Listener listener) {
		super("Data Property Regex", true, false);
		this.listener = listener;
		regexTextArea = new TextArea();
		propertiesList = new ListBox();
		initPropertiesList();
		createUi();
		setClosable(true);
		setPaddings(8);
		setWidth(400);
		setHeight(175);
		setCloseAction(Window.CLOSE);
	}

	private void initPropertiesList() {
		OntologyService.Util.getInstace().getInputDataProperties(new AsyncCallback<List<OWLPropertyBean>>() {
			public void onFailure(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			};

			public void onSuccess(List<OWLPropertyBean> result) {
				for (OWLPropertyBean propertyBean : result) {
					propertiesList.addItem(propertyBean.getLabel(), propertyBean.getUri());
				}
			};
		});
	}

	private void createUi() {
		setLayout(new AnchorLayout());
		Label queryTypelabel = new Label("Data Property:");
		Grid grid = new Grid(1, 2);
		grid.setWidget(0, 0, queryTypelabel);
		grid.setWidget(0, 1, propertiesList);
		add(grid);
		Label queryLabel = new Label("Enter a (java-style) regular expression");
		queryLabel.setWidth("100%");
		add(queryLabel, new AnchorLayoutData("100%"));
		add(regexTextArea, new AnchorLayoutData("100% -45"));

		addButton(new Button("OK", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				listener.onComplete(propertiesList.getValue(propertiesList.getSelectedIndex()), regexTextArea.getText());
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
		void onComplete(String property, String regex);
	}

}
