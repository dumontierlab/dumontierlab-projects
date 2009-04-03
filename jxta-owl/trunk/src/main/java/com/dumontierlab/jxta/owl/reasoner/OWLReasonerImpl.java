package com.dumontierlab.jxta.owl.reasoner;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;

public class OWLReasonerImpl implements OWLReasoner {

	@Override
	public boolean isConsistent(OWLOntology ontology) throws OWLReasonerException {
		// tODO
		return false;
	}

	@Override
	public void classify() throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearOntologies() throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<OWLOntology> getLoadedOntologies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClassified() throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefined(OWLClass arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefined(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefined(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefined(OWLIndividual arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRealised() throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void realise() throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unloadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLClass> getEquivalentClasses(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEquivalentClass(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubClassOf(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSatisfiable(OWLDescription arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0, OWLObjectPropertyExpression arg1)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLConstant> getRelatedValues(OWLIndividual arg0, OWLDataPropertyExpression arg1)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasDataPropertyRelationship(OWLIndividual arg0, OWLDataPropertyExpression arg1, OWLConstant arg2)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasObjectPropertyRelationship(OWLIndividual arg0, OWLObjectPropertyExpression arg1, OWLIndividual arg2)
			throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLDescription> getRanges(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLDataRange> getRanges(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAntiSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFunctional(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInverseFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIrreflexive(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReflexive(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTransitive(OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
	}

}
