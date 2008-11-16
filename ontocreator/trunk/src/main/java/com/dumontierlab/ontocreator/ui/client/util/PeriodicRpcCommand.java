package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.Timer;

public abstract class PeriodicRpcCommand<E> extends AbstractRpcCommand<E> {

	private final Timer timer;
	private final int period;
	private boolean running;

	public PeriodicRpcCommand(int periodInMillis) {
		period = periodInMillis;
		timer = new Timer() {
			@Override
			public void run() {
				callAtSuper();
			}
		};
	}

	@Override
	public void call() {
		callAtSuper();
		if (!running) {
			running = true;
			timer.scheduleRepeating(period);
		}
	}

	private void callAtSuper() {
		super.call();
	}

}
