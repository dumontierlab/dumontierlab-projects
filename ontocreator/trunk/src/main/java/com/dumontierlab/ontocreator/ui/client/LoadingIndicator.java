package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoadingIndicator extends Composite {

	private static final String LOADING_ICON_STYLE_NAME = "waitDialog-icon";
	private static final String STYLE_NAME = "loadingIndicator";
	private static final String DEFAUTL_MESSAGE = "Loading...";
	private boolean m_visible;

	public LoadingIndicator() {
		this(DEFAUTL_MESSAGE);
	}

	public LoadingIndicator(String message) {
		initWidget(createUi(message));
		DOM.setStyleAttribute(getElement(), "display", "none");
	}

	private Widget createUi(String msg) {
		HorizontalPanel panel = new HorizontalPanel();
		Spacer loadingIconPlaceHolder = new Spacer();
		loadingIconPlaceHolder.setStyleName(LOADING_ICON_STYLE_NAME);
		panel.add(loadingIconPlaceHolder);
		Label label = new Label(msg);
		panel.add(label);
		panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);

		panel.setStyleName(STYLE_NAME);
		return panel;
	}

	public void show() {
		m_visible = true;
		setVisible(true);
	}

	public void hide() {
		m_visible = false;
		setVisible(false);
	}

	@Override
	public boolean isVisible() {
		return m_visible;
	}
}
