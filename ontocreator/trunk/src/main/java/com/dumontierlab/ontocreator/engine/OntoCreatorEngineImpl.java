package com.dumontierlab.ontocreator.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLTypedConstant;

import com.dumontierlab.ontocreator.model.TabFile;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingRelationshipBean;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean.ColumnMappingType;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingRelationshipBean.ColumnMappingRelationshipType;
import com.google.inject.Singleton;

@Singleton
public class OntoCreatorEngineImpl implements OntoCreatorEngine {

	public OWLOntology generateOntology(OWLOntology ontology, Set<URI> imports, TabFile tabFile,
			Collection<ColumnMappingBean> mappings, OWLOntologyManager ontologyManager)
			throws OWLOntologyCreationException, IOException, OWLOntologyChangeException {

		List<AddAxiom> changes = new LinkedList<AddAxiom>();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		BufferedReader buffer = tabFile.getReader();
		String[] row = null;
		while ((row = tabFile.readRow(buffer)) != null) {
			for (ColumnMappingBean mapping : mappings) {
				ColumnMappingType type = mapping.getMappingType();
				if (type == ColumnMappingType.OWL_CLASS) {
					processOwlClassMapping(mapping, row, ontology, factory, changes);
				} else if (type == ColumnMappingType.OWL_INDIVIDUAL) {
					processOwlIndividualMapping(mapping, row, ontology, factory, changes);
				}
				// if it is a literal then nothing to do. It will only be used
				// in the context of a relationship.
			}

		}
		for (URI importUri : imports) {
			changes.add(new AddAxiom(ontology, factory.getOWLImportsDeclarationAxiom(ontology, importUri)));
		}
		ontologyManager.applyChanges(changes);
		return ontology;
	}

	private void processOwlIndividualMapping(ColumnMappingBean mapping, String[] row, OWLOntology ontology,
			OWLDataFactory factory, List<AddAxiom> changes) {
		OWLIndividual indv = (OWLIndividual) getOWLObject(mapping, row[mapping.getColumnIndex()], ontology, factory);
		changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(indv, factory.getOWLThing())));
		if (mapping.getInstanceOf() != null) {
			OWLClass c = factory.getOWLClass(URI.create(mapping.getInstanceOf()));
			changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(indv, c)));
		}
		for (Map.Entry<ColumnMappingBean, ColumnMappingRelationshipBean> relationshipEntry : mapping.getRelationships()
				.entrySet()) {
			ColumnMappingRelationshipBean relationship = relationshipEntry.getValue();
			ColumnMappingRelationshipType relationshipType = relationship.getType();
			ColumnMappingBean targetMapping = relationshipEntry.getKey();

			if (relationshipType == ColumnMappingRelationshipType.DATA_PROPERTY) {
				OWLTypedConstant literal = (OWLTypedConstant) getOWLObject(targetMapping, row[targetMapping
						.getColumnIndex()], ontology, factory);
				OWLDataProperty property = factory.getOWLDataProperty(URI.create(relationship.getUri()));
				changes.add(new AddAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(indv, property, literal)));
			} else if (relationshipType == ColumnMappingRelationshipType.INSTANCE_OF) {
				OWLClass c = (OWLClass) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()], ontology,
						factory);
				changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(indv, c)));
			} else if (relationshipType == ColumnMappingRelationshipType.OBJECT_PROPERTY) {
				OWLIndividual obj = (OWLIndividual) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()],
						ontology, factory);
				OWLObjectProperty property = factory.getOWLObjectProperty(URI.create(relationship.getUri()));
				changes.add(new AddAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(indv, property, obj)));
			} else if (relationshipType == ColumnMappingRelationshipType.SAME_AS) {
				OWLIndividual other = (OWLIndividual) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()],
						ontology, factory);
				Set<OWLIndividual> sameIndividuals = new HashSet<OWLIndividual>();
				sameIndividuals.add(indv);
				sameIndividuals.add(other);
				changes.add(new AddAxiom(ontology, factory.getOWLSameIndividualsAxiom(sameIndividuals)));
			} else if (relationshipType == ColumnMappingRelationshipType.ANNOTATION_PROPERTY) {
				OWLTypedConstant literal = (OWLTypedConstant) getOWLObject(targetMapping, row[targetMapping
						.getColumnIndex()], ontology, factory);
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(indv, URI.create(relationship
						.getUri()), literal)));
			}

		}
	}

	private void processOwlClassMapping(ColumnMappingBean mapping, String[] row, OWLOntology ontology,
			OWLDataFactory factory, List<AddAxiom> changes) {

		OWLClass c = (OWLClass) getOWLObject(mapping, row[mapping.getColumnIndex()], ontology, factory);

		changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(c, factory.getOWLThing())));
		if (mapping.getSubclassOf() != null) {
			OWLClass superC = factory.getOWLClass(URI.create(mapping.getSubclassOf()));
			changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(c, superC)));
		}
		if (mapping.getEquivalentTo() != null) {
			OWLClass equivC = factory.getOWLClass(URI.create(mapping.getEquivalentTo()));
			changes.add(new AddAxiom(ontology, factory.getOWLEquivalentClassesAxiom(c, equivC)));
		}
		if (mapping.getSuperclassOf() != null) {
			OWLClass subC = factory.getOWLClass(URI.create(mapping.getSuperclassOf()));
			changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(subC, c)));
		}

		for (Map.Entry<ColumnMappingBean, ColumnMappingRelationshipBean> relationshipEntry : mapping.getRelationships()
				.entrySet()) {
			ColumnMappingRelationshipBean relationship = relationshipEntry.getValue();
			ColumnMappingRelationshipType relationshipType = relationship.getType();
			ColumnMappingBean targetMapping = relationshipEntry.getKey();

			if (relationshipType == ColumnMappingRelationshipType.COMPLEMENT_OF) {
				OWLClass targetC = (OWLClass) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()], ontology,
						factory);
				OWLObjectComplementOf targetComplement = factory.getOWLObjectComplementOf(targetC);
				changes.add(new AddAxiom(ontology, factory.getOWLEquivalentClassesAxiom(c, targetComplement)));
			} else if (relationshipType == ColumnMappingRelationshipType.DISJOINT_WITH) {
				OWLClass targetC = (OWLClass) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()], ontology,
						factory);
				changes.add(new AddAxiom(ontology, factory.getOWLDisjointClassesAxiom(c, targetC)));
			} else if (relationshipType == ColumnMappingRelationshipType.EQUIVALENT_TO) {
				OWLClass targetC = (OWLClass) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()], ontology,
						factory);
				changes.add(new AddAxiom(ontology, factory.getOWLEquivalentClassesAxiom(c, targetC)));
			} else if (relationshipType == ColumnMappingRelationshipType.SUBCLASS_OF) {
				OWLClass targetC = (OWLClass) getOWLObject(targetMapping, row[targetMapping.getColumnIndex()], ontology,
						factory);
				changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(c, targetC)));
			} else if (relationshipType == ColumnMappingRelationshipType.ANNOTATION_PROPERTY) {
				OWLTypedConstant literal = (OWLTypedConstant) getOWLObject(targetMapping, row[targetMapping
						.getColumnIndex()], ontology, factory);
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(c, URI.create(relationship
						.getUri()), literal)));
			}

		}
	}

	private OWLObject getOWLObject(ColumnMappingBean mapping, String columnValue, OWLOntology ontology,
			OWLDataFactory factory) {
		if (mapping.getMappingType() == ColumnMappingType.OWL_CLASS) {
			return factory.getOWLClass(createUri(mapping.getUri(), columnValue, ontology.getURI()));
		} else if (mapping.getMappingType() == ColumnMappingType.OWL_INDIVIDUAL) {
			return factory.getOWLIndividual(createUri(mapping.getUri(), columnValue, ontology.getURI()));
		} else if (mapping.getMappingType() == ColumnMappingType.LITERAL) {
			return factory.getOWLTypedConstant(columnValue, factory.getOWLDataType(URI.create(mapping.getDatatype())));
		}
		assert false : "Unknown mapping type";
		return null;
	}

	private URI createUri(String uriTemplate, String columnValue, URI ontologyUri) {
		String result = uriTemplate.replace("${ontologyUri}", ontologyUri.toString());
		result = result.replace("${columnValue}", toCammelCase(columnValue));
		return URI.create(result);
	}

	private String toCammelCase(String inputString) {
		String[] parts = inputString.split("\\s");
		StringBuilder buffer = new StringBuilder();
		for (String part : parts) {
			if (parts.length > 0) {
				buffer.append(Character.toUpperCase(part.charAt(0)));
				if (part.length() > 1) {
					buffer.append(part.substring(1).toLowerCase());
				}
			}
		}
		return buffer.toString();
	}
}
