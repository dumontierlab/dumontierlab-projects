package com.dumontierlab.jxta.owl.reasoner.impl;

import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATermAppl;

public class DistributedReasoningHelper {

	private static final ATermAppl REMOTE = ATermUtils.makeTermAppl("_REMOTE_");

	public static ATermAppl getRemoteClass() {
		return REMOTE;
	}

}
