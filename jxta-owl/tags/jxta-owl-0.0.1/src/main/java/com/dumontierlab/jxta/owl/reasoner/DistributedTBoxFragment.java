package com.dumontierlab.jxta.owl.reasoner;

import java.util.List;
import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.tbox.impl.TBoxExpImpl;
import org.mindswap.pellet.utils.Pair;

import aterm.ATermAppl;

public class DistributedTBoxFragment extends TBoxExpImpl {

	public DistributedTBoxFragment(KnowledgeBase kb) {
		super(kb);
	}

	@Override
	public List<Pair<ATermAppl, Set<ATermAppl>>> unfold(ATermAppl c) {
		List<Pair<ATermAppl, Set<ATermAppl>>> localUnfold = super.unfold(c);
		// TODO for each term that is not local do remote unfolding.
		return localUnfold;
	}
}
