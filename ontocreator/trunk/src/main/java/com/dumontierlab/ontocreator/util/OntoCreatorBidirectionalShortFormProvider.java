package com.dumontierlab.ontocreator.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.util.CachingBidirectionalShortFormProvider;
import org.semanticweb.owl.util.ReferencedEntitySetProvider;

import uk.ac.manchester.owl.tutorial.LabelExtractor;

public class OntoCreatorBidirectionalShortFormProvider extends CachingBidirectionalShortFormProvider implements
		OWLOntologyChangeListener {

	private final OWLOntologyManager ontologyManager;

	public OntoCreatorBidirectionalShortFormProvider(OWLOntologyManager manager) {
		ontologyManager = manager;
		rebuild(new ReferencedEntitySetProvider(ontologyManager.getOntologies()));
		ontologyManager.addOntologyChangeListener(this);
	}

	@Override
	protected String generateShortForm(OWLEntity entity) {
		for (OWLOntology ontology : ontologyManager.getOntologies()) {
			String label = getLabel(entity, ontology);
			if (label != null) {
				return label;
			}
		}
		return RenderingHelper.getLabelFromUri(entity.getURI());
	}

	private String getLabel(OWLEntity entity, OWLOntology ontology) {
		LabelExtractor extractor = new LabelExtractor();
		for (OWLAnnotation<OWLObject> annotation : entity.getAnnotations(ontology)) {
			annotation.accept(extractor);
		}
		return extractor.getResult();
	}

	public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
		Set<OWLEntity> processed = new HashSet<OWLEntity>();
		for (OWLOntologyChange change : changes) {
			if (change instanceof AddAxiom) {
				AddAxiom addAx = (AddAxiom) change;
				for (OWLEntity ent : addAx.getEntities()) {
					if (!processed.contains(ent)) {
						processed.add(ent);
						add(ent);
					}
				}
			} else if (change instanceof RemoveAxiom) {
				RemoveAxiom remAx = (RemoveAxiom) change;
				for (OWLEntity ent : remAx.getEntities()) {
					if (!processed.contains(ent)) {
						processed.add(ent);
						boolean stillRef = false;
						for (OWLOntology ont : ontologyManager.getOntologies()) {
							if (ont.containsEntityReference(ent)) {
								stillRef = true;
								break;
							}
						}
						if (!stillRef) {
							remove(ent);
						}
					}
				}
			}
		}
	}

}
