package com.dumontierlab.jxta.owl.reasoner;

import java.util.Collection;
import java.util.logging.Logger;

import org.mindswap.pellet.DependencySet;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.service.OWLReasonerService;

public class DistributesKnowledBased extends KnowledgeBase {

	private Collection<OWLReasonerService> peers;
	private static final Logger LOG = Logger.getLogger(DistributesKnowledBased.class.getName());

	@Override
	public void addClass(ATermAppl c) {
		if (c.equals(ATermUtils.TOP) || ATermUtils.isComplexClass(c)) {
			return;
		}

	}

	@Override
	public void addAllDifferent(ATermList list) {
		// TODO Auto-generated method stub
		super.addAllDifferent(list);
	}

	@Override
	public boolean addAnnotation(ATermAppl s, ATermAppl p, ATermAppl o) {
		// TODO Auto-generated method stub
		return super.addAnnotation(s, p, o);
	}

	@Override
	public boolean addAnnotationProperty(ATermAppl p) {
		// TODO Auto-generated method stub
		return super.addAnnotationProperty(p);
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) {
		// TODO Auto-generated method stub
		super.addAsymmetricProperty(p);
	}

	@Override
	public void addComplementClass(ATermAppl c1, ATermAppl c2) {
		// TODO Auto-generated method stub
		super.addComplementClass(c1, c2);
	}

	@Override
	public boolean addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		// TODO Auto-generated method stub
		return super.addPropertyValue(p, s, o);
	}

	@Override
	public void addDatatype(ATerm p) {
		// TODO Auto-generated method stub
		super.addDatatype(p);
	}

	@Override
	public boolean addDatatypeProperty(ATerm p) {
		// TODO
		return super.addDatatypeProperty(p);
	};

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		// TODO Auto-generated method stub
		super.addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		// TODO Auto-generated method stub
		super.addDisjointClass(c1, c2);
	}

	@Override
	public void addDisjointClasses(ATermList classes) {
		// TODO Auto-generated method stub
		super.addDisjointClasses(classes);
	}

	@Override
	public void addDomain(ATerm p, ATermAppl c) {
		// TODO Auto-generated method stub
		super.addDomain(p, c);
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		// TODO Auto-generated method stub
		super.addEquivalentClass(c1, c2);
	}

	@Override
	public void addDomain(ATerm p, ATermAppl c, DependencySet ds) {
		// TODO Auto-generated method stub
		super.addDomain(p, c, ds);
	}

}
