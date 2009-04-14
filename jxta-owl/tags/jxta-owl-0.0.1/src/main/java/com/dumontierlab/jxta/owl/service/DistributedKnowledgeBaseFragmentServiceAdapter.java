package com.dumontierlab.jxta.owl.service;

import java.rmi.RemoteException;

import org.mindswap.pellet.utils.ATermUtils;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentServiceAdapter implements DistributedKnowledgeBaseFragment {

	private final DistributedKnowledgeBaseFragmentService service;

	public DistributedKnowledgeBaseFragmentServiceAdapter(DistributedKnowledgeBaseFragmentService service) {
		this.service = service;
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) throws RemoteException {
		service.addAsymmetricProperty(serialize(p));
	}

	@Override
	public void addClass(ATermAppl c) {
		service.addClass(serialize(c));
	}

	@Override
	public void addDatatypeProperty(ATermAppl p) {
		service.addDatatypeProperty(serialize(p));
	}

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		service.addDifferent(serialize(i1), serialize(i2));
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		service.addDisjointClass(serialize(c1), serialize(c2));
	}

	@Override
	public void addDomain(ATermAppl p, ATermAppl c) {
		service.addDomain(serialize(p), serialize(c));
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		service.addEquivalentClass(serialize(c1), serialize(c2));
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		service.addPropertyValue(serialize(p), serialize(s), serialize(o));
	}

	@Override
	public void addRange(ATermAppl p, ATermAppl c) {
		service.addRange(serialize(p), serialize(c));
	}

	@Override
	public void addIndividual(ATermAppl i) {
		service.addIndividual(serialize(i));
	}

	@Override
	public void addType(ATermAppl i, ATermAppl c) {
		service.addType(serialize(i), serialize(c));
	}

	@Override
	public void addObjectProperty(ATermAppl p) {
		service.addObjectProperty(serialize(p));
	}

	@Override
	public void addEquivalentProperty(ATermAppl p1, ATermAppl p2) {
		service.addEquivalentProperty(serialize(p1), serialize(p2));
	}

	@Override
	public void addTransitiveProperty(ATermAppl p) {
		service.addTransitiveProperty(serialize(p));
	}

	@Override
	public void addSame(ATermAppl i1, ATermAppl i2) {
		service.addSame(serialize(i1), serialize(i2));
	}

	private String serialize(ATerm term) {
		StringBuilder sb = new StringBuilder();
		serialize(term, sb);
		return sb.toString();
	}

	private void serialize(ATerm term, StringBuilder sb) {
		if (term instanceof ATermAppl) {
			ATermAppl appl = (ATermAppl) term;
			if (ATermUtils.isPrimitive(appl)) {
				sb.append("\"" + appl.getAFun().getName() + "\"");
			} else {
				AFun fun = appl.getAFun();
				sb.append(fun.getName());
				if (fun.getArity() > 0) {
					sb.append("(");
					for (ATerm arg : appl.getArgumentArray()) {
						serialize(arg, sb);
						sb.append(", ");
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.deleteCharAt(sb.length() - 1);
					sb.append(")");
				}
			}
		} else if (term instanceof ATermList) {
			ATermList list = (ATermList) term;
			sb.append("[");
			if (!list.isEmpty()) {
				int i = 0;
				for (; i < list.getLength() - 1; i++) {
					serialize(list.elementAt(i), sb);
					sb.append(", ");
				}
				serialize(list.elementAt(i), sb);
			}
			sb.append("]");
		} else {
			sb.append(term.toString());
		}
	}
}
