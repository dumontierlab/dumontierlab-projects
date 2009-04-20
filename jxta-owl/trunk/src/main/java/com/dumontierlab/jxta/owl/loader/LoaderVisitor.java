package com.dumontierlab.jxta.owl.loader;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.datatypes.Datatype;
import org.mindswap.pellet.datatypes.DatatypeReasoner;
import org.mindswap.pellet.datatypes.UnknownDatatype;
import org.mindswap.pellet.datatypes.XSDAtomicType;
import org.mindswap.pellet.exceptions.UnsupportedFeatureException;
import org.mindswap.pellet.utils.ATermUtils;
import org.mindswap.pellet.utils.Comparators;
import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLConstantAnnotation;
import org.semanticweb.owl.model.OWLDataAllRestriction;
import org.semanticweb.owl.model.OWLDataComplementOf;
import org.semanticweb.owl.model.OWLDataExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataOneOf;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataRangeFacetRestriction;
import org.semanticweb.owl.model.OWLDataRangeRestriction;
import org.semanticweb.owl.model.OWLDataSomeRestriction;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDataValueRestriction;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectAllRestriction;
import org.semanticweb.owl.model.OWLObjectAnnotation;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectOneOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSelfRestriction;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectUnionOf;
import org.semanticweb.owl.model.OWLObjectValueRestriction;
import org.semanticweb.owl.model.OWLObjectVisitor;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.model.OWLUntypedConstant;
import org.semanticweb.owl.model.SWRLAtomConstantObject;
import org.semanticweb.owl.model.SWRLAtomDVariable;
import org.semanticweb.owl.model.SWRLAtomIVariable;
import org.semanticweb.owl.model.SWRLAtomIndividualObject;
import org.semanticweb.owl.model.SWRLBuiltInAtom;
import org.semanticweb.owl.model.SWRLClassAtom;
import org.semanticweb.owl.model.SWRLDataRangeAtom;
import org.semanticweb.owl.model.SWRLDataValuedPropertyAtom;
import org.semanticweb.owl.model.SWRLDifferentFromAtom;
import org.semanticweb.owl.model.SWRLObjectPropertyAtom;
import org.semanticweb.owl.model.SWRLRule;
import org.semanticweb.owl.model.SWRLSameAsAtom;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;
import org.semanticweb.owl.vocab.OWLRestrictedDataRangeFacetVocabulary;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.exception.UnsupportedOWLProfileException;

public class LoaderVisitor implements OWLObjectVisitor {

	private static final Logger LOG = Logger.getLogger(LoaderVisitor.class);

	private final KnowledgeBase kb;
	private ATermAppl term;
	private final boolean enforceProfile;

	public LoaderVisitor(KnowledgeBase kb) {
		this(kb, false);
	}

	public LoaderVisitor(KnowledgeBase kb, boolean enforceProfile) {
		this.kb = kb;
		this.enforceProfile = enforceProfile;
	}

	@Override
	public void visit(OWLClass c) {
		URI uri = c.getURI();

		if (uri.equals(OWLRDFVocabulary.OWL_THING.getURI())) {
			term = ATermUtils.TOP;
		} else if (uri.equals(OWLRDFVocabulary.OWL_NOTHING.getURI())) {
			term = ATermUtils.BOTTOM;
		} else {
			term = ATermUtils.makeTermAppl(uri.toString());
		}
		kb.addClass(term);

	}

	@Override
	public void visit(OWLIndividual ind) {
		if (ind.isAnonymous()) {
			term = ATermUtils.makeBnode(ind.getURI().toString());
		} else {
			term = ATermUtils.makeTermAppl(ind.getURI().toString());
		}

		kb.addIndividual(term);
	}

	@Override
	public void visit(OWLObjectProperty prop) {
		term = ATermUtils.makeTermAppl(prop.getURI().toString());
		kb.addObjectProperty(term);
	}

	@Override
	public void visit(OWLObjectPropertyInverse propInv) {
		// propInv.getInverse().accept(this);
		// ATermAppl p = term;

		// term = ATermUtils.makeInv(p);
		throw new UnsupportedFeatureException("Inverse properties are not supported: " + propInv);
	}

	@Override
	public void visit(OWLDataProperty prop) {
		term = ATermUtils.makeTermAppl(prop.getURI().toString());
		kb.addDatatypeProperty(term);

	}

	@Override
	public void visit(OWLTypedConstant constant) {
		String lexicalValue = constant.getLiteral();
		constant.getDataType().accept(this);
		ATerm datatype = term;

		term = ATermUtils.makeTypedLiteral(lexicalValue, datatype.toString());
	}

	@Override
	public void visit(OWLUntypedConstant constant) {
		String lexicalValue = constant.getLiteral();
		String lang = constant.getLang();

		if (lang != null) {
			term = ATermUtils.makePlainLiteral(lexicalValue, lang);
		} else {
			term = ATermUtils.makePlainLiteral(lexicalValue);
		}
	}

	@Override
	public void visit(OWLDataType ocdt) {
		term = ATermUtils.makeTermAppl(ocdt.getURI().toString());

		kb.addDatatype(term);
	}

	@Override
	public void visit(OWLObjectIntersectionOf and) {
		Set<OWLDescription> operands = and.getOperands();
		ATerm[] terms = new ATerm[operands.size()];
		int size = 0;
		for (OWLDescription desc : operands) {
			desc.accept(this);
			terms[size++] = term;
		}
		// create a sorted set of terms so we will have a stable
		// concept creation and removal using this concept will work
		ATermList setOfTerms = size > 0 ? ATermUtils.toSet(terms, size) : ATermUtils.EMPTY_LIST;
		term = ATermUtils.makeAnd(setOfTerms);
	}

	@Override
	public void visit(OWLObjectUnionOf or) {
		// Set<OWLDescription> operands = or.getOperands();
		// ATerm[] terms = new ATerm[operands.size()];
		// int size = 0;
		// for (OWLDescription desc : operands) {
		// desc.accept(this);
		// terms[size++] = term;
		// }
		// create a sorted set of terms so we will have a stable
		// concept creation and removal using this concept will work
		// ATermList setOfTerms = size > 0 ? ATermUtils.toSet(terms, size) :
		// ATermUtils.EMPTY_LIST;
		// term = ATermUtils.makeOr(setOfTerms);
		throw new UnsupportedFeatureException("Concept disjunction is not supported: " + or);
	}

	@Override
	public void visit(OWLObjectComplementOf not) {
		OWLDescription desc = not.getOperand();
		desc.accept(this);

		term = ATermUtils.makeNot(term);
		if (!ATermUtils.isPrimitive(term)) {
			throw new UnsupportedFeatureException("Negation of complex concepts is not supported: " + not);
		}
	}

	@Override
	public void visit(OWLObjectOneOf enumeration) {
		Set<OWLIndividual> operands = enumeration.getIndividuals();
		if (operands.size() > 1) {
			throw new UnsupportedFeatureException("Enumerations with more than 1 individual are not supported: "
					+ enumeration);
		}
		ATerm[] terms = new ATerm[operands.size()];
		int size = 0;
		for (OWLIndividual ind : operands) {
			ind.accept(this);
			terms[size++] = ATermUtils.makeValue(term);
		}
		// create a sorted set of terms so we will have a stable
		// concept creation and removal using this concept will work
		ATermList setOfTerms = size > 0 ? ATermUtils.toSet(terms, size) : ATermUtils.EMPTY_LIST;
		term = ATermUtils.makeOr(setOfTerms);
	}

	@Override
	public void visit(OWLObjectSomeRestriction restriction) {
		restriction.getProperty().accept(this);
		ATermAppl p = term;
		restriction.getFiller().accept(this);
		ATermAppl c = term;

		term = ATermUtils.makeSomeValues(p, c);
	}

	@Override
	public void visit(OWLObjectAllRestriction restriction) {
		restriction.getProperty().accept(this);
		ATermAppl p = term;
		restriction.getFiller().accept(this);
		ATermAppl c = term;

		if (!ATermUtils.isPrimitive(c)) {
			throw new UnsupportedFeatureException(
					"Universal qualifications of complex class expressions are not supported: " + restriction);
		}

		term = ATermUtils.makeAllValues(p, c);

	}

	@Override
	public void visit(OWLObjectValueRestriction restriction) {
		restriction.getProperty().accept(this);
		ATerm p = term;
		restriction.getValue().accept(this);
		ATermAppl ind = term;

		term = ATermUtils.makeHasValue(p, ind);
	}

	@Override
	public void visit(OWLObjectExactCardinalityRestriction restriction) {
		// addSimpleProperty(restriction.getProperty());

		// restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeCard(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);
	}

	@Override
	public void visit(OWLObjectMaxCardinalityRestriction restriction) {
		// addSimpleProperty(restriction.getProperty());

		// restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeMax(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);

	}

	@Override
	public void visit(OWLObjectMinCardinalityRestriction restriction) {
		// addSimpleProperty(restriction.getProperty());

		// /restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeMin(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);
	}

	@Override
	public void visit(OWLDataExactCardinalityRestriction restriction) {
		// restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeCard(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);
	}

	@Override
	public void visit(OWLDataMaxCardinalityRestriction restriction) {
		// restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeMax(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);
	}

	@Override
	public void visit(OWLDataMinCardinalityRestriction restriction) {
		// restriction.getProperty().accept(this);
		// ATerm p = term;
		// int n = restriction.getCardinality();
		// restriction.getFiller().accept(this);
		// ATermAppl desc = term;

		// term = ATermUtils.makeMin(p, n, desc);
		throw new UnsupportedFeatureException("Cardinality restrictions are not supported: " + restriction);
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		Set<OWLDescription> descriptions = axiom.getDescriptions();
		int size = descriptions.size();
		if (size > 1) {
			ATermAppl[] terms = new ATermAppl[size];
			int index = 0;
			for (OWLDescription desc : descriptions) {
				desc.accept(this);
				terms[index++] = term;
			}
			Arrays.sort(terms, 0, size, Comparators.hashCodeComparator);

			ATermAppl c1 = terms[0];

			for (int i = 1; i < terms.length; i++) {
				ATermAppl c2 = terms[i];
				kb.addEquivalentClass(c1, c2);
			}
		}
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		Set<OWLDescription> descriptions = axiom.getDescriptions();
		int size = descriptions.size();
		if (size > 1) {
			ATermAppl[] terms = new ATermAppl[size];
			int index = 0;
			for (OWLDescription desc : descriptions) {
				desc.accept(this);
				terms[index++] = term;
			}

			ATermList list = ATermUtils.toSet(terms, size);
			kb.addDisjointClasses(list);
		}
	}

	@Override
	public void visit(OWLSubClassAxiom axiom) {
		axiom.getSubClass().accept(this);
		ATermAppl c1 = term;
		axiom.getSuperClass().accept(this);
		ATermAppl c2 = term;

		kb.addSubClass(c1, c2);
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		int size = axiom.getProperties().size();
		OWLObjectPropertyExpression[] props = new OWLObjectPropertyExpression[size];
		axiom.getProperties().toArray(props);
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				props[i].accept(this);
				ATermAppl p1 = term;
				props[j].accept(this);
				ATermAppl p2 = term;

				kb.addEquivalentProperty(p1, p2);
			}
		}
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {

		int size = axiom.getProperties().size();
		OWLDataPropertyExpression[] props = new OWLDataPropertyExpression[size];
		axiom.getProperties().toArray(props);
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				props[i].accept(this);
				ATermAppl p1 = term;
				props[j].accept(this);
				ATermAppl p2 = term;

				kb.addEquivalentProperty(p1, p2);
			}
		}
	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {

		if (axiom.getIndividuals().size() == 2) {
			Iterator<OWLIndividual> iter = axiom.getIndividuals().iterator();
			iter.next().accept(this);
			ATermAppl i1 = term;
			iter.next().accept(this);
			ATermAppl i2 = term;
			kb.addDifferent(i1, i2);
		} else {
			ATermAppl[] terms = new ATermAppl[axiom.getIndividuals().size()];
			int i = 0;
			for (OWLIndividual ind : axiom.getIndividuals()) {
				ind.accept(this);
				terms[i++] = term;
			}
			kb.addAllDifferent(ATermUtils.makeList(terms));
		}
	}

	@Override
	public void visit(OWLSameIndividualsAxiom axiom) {

		Iterator<OWLIndividual> eqs = axiom.getIndividuals().iterator();
		if (eqs.hasNext()) {
			eqs.next().accept(this);
			ATermAppl i1 = term;

			while (eqs.hasNext()) {
				eqs.next().accept(this);
				ATermAppl i2 = term;

				kb.addSame(i1, i2);
			}
		}
	}

	@Override
	public void visit(OWLDataOneOf enumeration) {
		ATermList ops = ATermUtils.EMPTY_LIST;
		Set<OWLConstant> values = enumeration.getValues();
		if (values.size() > 1) {
			throw new UnsupportedFeatureException("Enumerations with more than 1 individual are not supported: "
					+ enumeration);
		}
		for (OWLConstant value : values) {
			value.accept(this);
			ops = ops.insert(ATermUtils.makeValue(term));
		}
		term = ATermUtils.makeOr(ops);
	}

	@Override
	public void visit(OWLDataAllRestriction restriction) {
		// TODO: Can we support universal data restrictions?
		restriction.getProperty().accept(this);
		ATerm p = term;
		restriction.getFiller().accept(this);
		ATerm c = term;

		term = ATermUtils.makeAllValues(p, c);
	}

	@Override
	public void visit(OWLDataSomeRestriction restriction) {
		restriction.getProperty().accept(this);
		ATerm p = term;
		restriction.getFiller().accept(this);
		ATerm c = term;

		term = ATermUtils.makeSomeValues(p, c);
	}

	@Override
	public void visit(OWLDataValueRestriction restriction) {
		restriction.getProperty().accept(this);
		ATermAppl p = term;
		restriction.getValue().accept(this);
		ATermAppl dv = term;

		term = ATermUtils.makeHasValue(p, dv);
	}

	@Override
	public void visit(OWLOntology ont) {
		for (OWLAxiom axiom : ont.getAxioms()) {
			LOG.debug("Loading axiom: " + axiom);
			try {
				axiom.accept(this);
			} catch (UnsupportedFeatureException e) {
				if (enforceProfile) {
					throw new UnsupportedOWLProfileException("This ontology (" + ont.getURI()
							+ ") does not comply with the OWL 2 EL profile.", e);
				}
				LOG.warn(e.getMessage());
			}
		}
	}

	@Override
	public void visit(OWLObjectSelfRestriction restriction) {

		restriction.getProperty().accept(this);
		ATermAppl p = term;

		term = ATermUtils.makeSelf(p);
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		throw new UnsupportedFeatureException("Disjoint Properties are not supported: " + axiom);
		// Object[] disjs = axiom.getProperties().toArray();
		// for (int i = 0; i < disjs.length - 1; i++) {
		// OWLObjectProperty prop1 = (OWLObjectProperty) disjs[i];
		// addSimpleProperty(prop1);
		// for (int j = i + 1; j < disjs.length; j++) {
		// OWLObjectProperty prop2 = (OWLObjectProperty) disjs[j];
		// addSimpleProperty(prop2);
		// prop1.accept(this);
		// ATermAppl p1 = term;
		// prop2.accept(this);
		// ATermAppl p2 = term;
		//
		// kb.addDisjointProperty(p1, p2);
		// }
		// }
	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		throw new UnsupportedFeatureException("Disjoint Properties are not supported: " + axiom);
		// if (!addAxioms) {
		// reloadRequired = true;
		// return;
		// }
		//
		// Object[] disjs = axiom.getProperties().toArray();
		// for (int i = 0; i < disjs.length; i++) {
		// for (int j = i + 1; j < disjs.length; j++) {
		// OWLDataProperty desc1 = (OWLDataProperty) disjs[i];
		// OWLDataProperty desc2 = (OWLDataProperty) disjs[j];
		// desc1.accept(this);
		// ATermAppl p1 = term;
		// desc2.accept(this);
		// ATermAppl p2 = term;
		//
		// kb.addDisjointProperty(p1, p2);
		// }
		// }
	}

	@Override
	public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
		throw new UnsupportedFeatureException("Property chains are not supported: " + axiom);

		// compositePropertyAxioms.add(getNamedProperty(axiom.getSuperProperty()),
		// axiom);
		//
		// axiom.getSuperProperty().accept(this);
		// ATermAppl prop = result();
		//
		// List<OWLObjectPropertyExpression> propChain =
		// axiom.getPropertyChain();
		// ATermList chain = ATermUtils.EMPTY_LIST;
		// for (int i = propChain.size() - 1; i >= 0; i--) {
		// propChain.get(i).accept(this);
		// chain = chain.insert(result());
		// }
		//
		// kb.addSubProperty(chain, prop);
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		axiom.getOWLClass().accept(this);
		ATermAppl c = term;

		ATermList classes = ATermUtils.EMPTY_LIST;
		for (OWLDescription desc : axiom.getDescriptions()) {
			desc.accept(this);
			classes = classes.insert(term);
		}

		kb.addDisjointClasses(classes);
		kb.addEquivalentClass(c, ATermUtils.makeOr(classes));
	}

	@Override
	public void visit(OWLDataComplementOf node) {
		String name = "Datatype" + node.hashCode();

		DatatypeReasoner dtReasoner = kb.getDatatypeReasoner();
		Datatype datatype = UnknownDatatype.instance;

		node.getDataRange().accept(this);
		Datatype baseDatatype = dtReasoner.getDatatype(term);

		datatype = dtReasoner.negate(baseDatatype);

		kb.addDatatype(name, datatype);
		term = ATermUtils.makeTermAppl(name);
	}

	@Override
	public void visit(OWLDataRangeRestriction node) {
		String name = "Datatype" + node.hashCode();

		DatatypeReasoner dtReasoner = kb.getDatatypeReasoner();
		Datatype datatype = UnknownDatatype.instance;

		node.getDataRange().accept(this);
		Datatype baseDatatype = dtReasoner.getDatatype(term);

		if (baseDatatype instanceof XSDAtomicType) {
			XSDAtomicType xsdType = (XSDAtomicType) baseDatatype;

			for (OWLDataRangeFacetRestriction restr : node.getFacetRestrictions()) {
				OWLRestrictedDataRangeFacetVocabulary facet = restr.getFacet();
				OWLConstant facetValue = restr.getFacetValue();

				if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.MIN_INCLUSIVE)) {
					Object value = xsdType.getPrimitiveType().getValue(facetValue.getLiteral(), xsdType.getURI());
					xsdType = xsdType.restrictMinInclusive(value);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.MAX_INCLUSIVE)) {
					Object value = xsdType.getPrimitiveType().getValue(facetValue.getLiteral(), xsdType.getURI());
					xsdType = xsdType.restrictMaxInclusive(value);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.MIN_EXCLUSIVE)) {
					Object value = xsdType.getPrimitiveType().getValue(facetValue.getLiteral(), xsdType.getURI());
					xsdType = xsdType.restrictMinExclusive(value);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.MAX_EXCLUSIVE)) {
					Object value = xsdType.getPrimitiveType().getValue(facetValue.getLiteral(), xsdType.getURI());
					xsdType = xsdType.restrictMaxExclusive(value);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.TOTAL_DIGITS)) {
					int n = Integer.parseInt(facetValue.getLiteral());
					xsdType = xsdType.restrictTotalDigits(n);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.FRACTION_DIGITS)) {
					int n = Integer.parseInt(facetValue.getLiteral());
					xsdType = xsdType.restrictFractionDigits(n);
				} else if (facet.equals(OWLRestrictedDataRangeFacetVocabulary.PATTERN)) {
					String str = facetValue.getLiteral();
					xsdType = xsdType.restrictPattern(str);
				} else {
					LOG.warn("Unrecognized facet " + facet);
				}
			}

			datatype = xsdType;
		} else {
			LOG.warn("Unrecognized base datatype " + node.getDataRange());
		}

		kb.addDatatype(name, datatype);
		term = ATermUtils.makeTermAppl(name);
	}

	@Override
	public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {

		// axiom.getProperty().accept(this);
		// ATermAppl p = term;
		//
		// kb.addAsymmetricProperty(p);
		throw new UnsupportedFeatureException("Asymmetric object properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		axiom.getProperty().accept(this);
		ATermAppl p = term;

		kb.addReflexiveProperty(p);
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		// axiom.getProperty().accept(this);
		// ATermAppl p = term;
		//
		// kb.addFunctionalProperty(p);
		throw new UnsupportedFeatureException("Functional object properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {

		axiom.getSubject().accept(this);
		ATermAppl s = term;
		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getObject().accept(this);
		ATermAppl o = term;

		kb.addNegatedPropertyValue(p, s, o);
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {

		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getDomain().accept(this);
		ATermAppl c = term;

		kb.addDomain(p, c);
	}

	@Override
	public void visit(OWLImportsDeclaration axiom) {
		LOG.debug("Ignoring imports declaration: " + axiom);
	}

	@Override
	public void visit(OWLAxiomAnnotationAxiom axiom) {
		LOG.debug("Ignoring axiom annotation: " + axiom);

	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {

		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getDomain().accept(this);
		ATermAppl c = term;

		kb.addDomain(p, c);
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {

		axiom.getSubject().accept(this);
		ATermAppl s = term;
		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getObject().accept(this);
		ATermAppl o = term;

		kb.addNegatedPropertyValue(p, s, o);
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {

		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getRange().accept(this);
		ATermAppl c = term;

		kb.addRange(p, c);
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {

		axiom.getSubject().accept(this);
		ATermAppl subj = term;
		axiom.getProperty().accept(this);
		ATermAppl pred = term;
		axiom.getObject().accept(this);
		ATermAppl obj = term;

		kb.addPropertyValue(pred, subj, obj);
	}

	@Override
	public void visit(OWLObjectSubPropertyAxiom axiom) {

		axiom.getSubProperty().accept(this);
		ATermAppl sub = term;
		axiom.getSuperProperty().accept(this);
		ATermAppl sup = term;

		kb.addSubProperty(sub, sup);
	}

	@Override
	public void visit(OWLDeclarationAxiom axiom) {
		axiom.getEntity().accept(this);
	}

	@Override
	public void visit(OWLEntityAnnotationAxiom axiom) {
		axiom.getSubject().accept(this);
		ATermAppl s = term;

		URI uri = axiom.getAnnotation().getAnnotationURI();
		ATermAppl p = ATermUtils.makeTermAppl(uri.toASCIIString());
		kb.addAnnotationProperty(p);

		axiom.getAnnotation().accept(this);
		ATermAppl o = term;

		kb.addAnnotation(s, p, o);
	}

	@Override
	public void visit(OWLOntologyAnnotationAxiom axiom) {
		LOG.debug("Ignoring ontology annotation: " + axiom);
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		// axiom.getProperty().accept(this);
		// ATermAppl p = term;
		//
		// kb.addSymmetricProperty(p);
		throw new UnsupportedFeatureException("Symetric properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {

		axiom.getProperty().accept(this);
		ATermAppl p = term;
		axiom.getRange().accept(this);
		ATermAppl c = term;

		kb.addRange(p, c);
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		throw new UnsupportedFeatureException("functional properties are not supported.");
		// axiom.getProperty().accept(this);
		// ATermAppl p = term;

		// kb.addFunctionalProperty(p);
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {

		axiom.getIndividual().accept(this);
		ATermAppl ind = term;
		axiom.getDescription().accept(this);
		ATermAppl c = term;

		kb.addType(ind, c);
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {

		axiom.getSubject().accept(this);
		ATermAppl subj = term;
		axiom.getProperty().accept(this);
		ATermAppl pred = term;
		axiom.getObject().accept(this);
		ATermAppl obj = term;

		kb.addPropertyValue(pred, subj, obj);
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {

		axiom.getProperty().accept(this);
		ATermAppl p = term;

		kb.addTransitiveProperty(p);
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		// axiom.getProperty().accept(this);
		// ATermAppl p = term;
		//
		// kb.addIrreflexiveProperty(p);
		throw new UnsupportedFeatureException("Irreflexive properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLDataSubPropertyAxiom axiom) {

		axiom.getSubProperty().accept(this);
		ATermAppl p1 = term;
		axiom.getSuperProperty().accept(this);
		ATermAppl p2 = term;

		kb.addSubProperty(p1, p2);
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		// axiom.getProperty().accept(this);
		// ATermAppl p = term;
		//
		// kb.addInverseFunctionalProperty(p);
		throw new UnsupportedFeatureException("Inverse functional object properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		// axiom.getFirstProperty().accept(this);
		// ATermAppl p1 = term;
		// axiom.getSecondProperty().accept(this);
		// ATermAppl p2 = term;
		//
		// kb.addInverseProperty(p1, p2);
		throw new UnsupportedFeatureException("Inverse  properties are not supported: " + axiom);
	}

	@Override
	public void visit(OWLDataRangeFacetRestriction node) {
		// nothing to do here
	}

	@Override
	public void visit(OWLObjectAnnotation a) {
		a.getAnnotationValue().accept(this);
		// if( log.isLoggable( Level.FINE ) )
		// log.fine( "Ignoring object annotation: " + annotation );
	}

	@Override
	public void visit(OWLConstantAnnotation a) {
		a.getAnnotationValue().accept(this);
		// if( log.isLoggable( Level.FINE ) )
		// log.fine( "Ignoring constant annotation: " + annotation );
	}

	@Override
	public void visit(SWRLRule rule) {
		throw new UnsupportedFeatureException("Rules are not supported: " + rule);
	}

	@Override
	public void visit(SWRLDataRangeAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLObjectPropertyAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLDataValuedPropertyAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLSameAsAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLDifferentFromAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLBuiltInAtom atom) {
		throw new UnsupportedFeatureException("Rules are not supported: " + atom);
	}

	@Override
	public void visit(SWRLAtomDVariable dvar) {
		throw new UnsupportedFeatureException("Rules are not supported: " + dvar);
	}

	@Override
	public void visit(SWRLAtomIVariable ivar) {
		throw new UnsupportedFeatureException("Rules are not supported: " + ivar);
	}

	@Override
	public void visit(SWRLAtomIndividualObject iobj) {
		throw new UnsupportedFeatureException("Rules are not supported: " + iobj);
	}

	@Override
	public void visit(SWRLAtomConstantObject cons) {
		throw new UnsupportedFeatureException("Rules are not supported: " + cons);
	}

	@Override
	public void visit(SWRLClassAtom node) {

		throw new UnsupportedFeatureException("Rules are not supported: " + node);
	}

}
