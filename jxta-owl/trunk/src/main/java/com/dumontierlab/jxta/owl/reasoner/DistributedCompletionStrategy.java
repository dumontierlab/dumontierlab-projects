package com.dumontierlab.jxta.owl.reasoner;

import org.mindswap.pellet.ABox;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.tableau.blocking.Blocking;
import org.mindswap.pellet.tableau.blocking.OptimizedDoubleBlocking;
import org.mindswap.pellet.tableau.completion.CompletionStrategy;

import com.clarkparsia.pellet.expressivity.Expressivity;

public class DistributedCompletionStrategy extends CompletionStrategy {

	public DistributedCompletionStrategy(ABox abox, Blocking blocking) {
		super(abox, blocking);
	}

	public DistributedCompletionStrategy(ABox abox) {
		this(abox, new OptimizedDoubleBlocking());
	}

	@Override
	public void complete(Expressivity expr) {
		PelletOptions.PRINT_ABOX = true;
		abox.printTree();
		abox.setComplete(true);
	}

}
