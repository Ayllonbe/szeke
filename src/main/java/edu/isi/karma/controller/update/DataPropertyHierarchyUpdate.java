/*******************************************************************************
 * Copyright 2012 University of Southern California
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code was developed by the Information Integration Group as part 
 * of the Karma project at the Information Sciences Institute of the 
 * University of Southern California.  For more information, publications, 
 * and related projects, please see: http://www.isi.edu/integration
 ******************************************************************************/
package edu.isi.karma.controller.update;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.isi.karma.view.VWorkspace;

public class DataPropertyHierarchyUpdate extends AbstractUpdate {

	private OntModel model;

	private static Logger logger = LoggerFactory
			.getLogger(DatabaseTablesListUpdate.class.getSimpleName());

	public enum JsonKeys {
		data, URI, metadata, children
	}

	public DataPropertyHierarchyUpdate(OntModel model) {
		this.model = model;
	}

	@Override
	public void generateJson(String prefix, PrintWriter pw,
			VWorkspace vWorkspace) {
		Set<String> propertiesAdded = new HashSet<String>();

		ExtendedIterator<DatatypeProperty> propsIter = model
				.listDatatypeProperties();

		try {
			JSONArray dataArray = new JSONArray();

			while (propsIter.hasNext()) {
				DatatypeProperty prop = propsIter.next();
//				System.out.println(prop.getURI());
				if ((prop.listSuperProperties().toList().size() != 0)
						|| propertiesAdded.contains(prop.getLocalName())) {
//					System.out.println("Skipping " + prop.getURI());
					continue;
				}

				JSONObject classObject = new JSONObject();

				if (prop.listSubProperties().toList().size() != 0) {
					JSONArray childrenArray = new JSONArray();
					addSubclassChildren(prop, childrenArray, 0, propertiesAdded);
					classObject.put(JsonKeys.children.name(), childrenArray);
				}

				classObject.put(JsonKeys.data.name(), prop.getLocalName());

				propertiesAdded.add(prop.getLocalName());

				JSONObject metadataObject = new JSONObject();
				metadataObject.put(JsonKeys.URI.name(), prop.getURI());
				classObject.put(JsonKeys.metadata.name(), metadataObject);
				dataArray.put(classObject);
			}

			// Prepare the output JSON
			JSONObject outputObject = new JSONObject();
			outputObject.put(GenericJsonKeys.updateType.name(),
					"DataPropertyListUpdate");
			outputObject.put(JsonKeys.data.name(), dataArray);

			pw.println(outputObject.toString(4));
		} catch (JSONException e) {
			logger.error("Error occured while creating JSON!", e);
		}
	}

	private static void addSubclassChildren(OntProperty prop,
			JSONArray childrenArray, int level, Set<String> propertiesAdded)
			throws JSONException {

		ExtendedIterator<? extends OntProperty> subProperties = prop
				.listSubProperties();
		while (subProperties.hasNext()) {
			OntProperty subProp = subProperties.next();
			if(subProp.getURI().equals(prop.getURI())) {
				continue;
			}
			
//			System.out.println("Working with sub prop " + subProp.getURI() + " at level " + level);
			propertiesAdded.add(subProp.getLocalName());

			JSONObject classObject = new JSONObject();
			classObject.put(JsonKeys.data.name(), subProp.getLocalName());
			JSONObject metadataObject = new JSONObject();
			metadataObject.put(JsonKeys.URI.name(), subProp.getURI());
			classObject.put(JsonKeys.metadata.name(), metadataObject);

			if (subProp.listSubProperties().toList().size() != 0) {
				JSONArray childrenArraySubClass = new JSONArray();
				addSubclassChildren(subProp, childrenArraySubClass, level + 1,
						propertiesAdded);
				classObject
						.put(JsonKeys.children.name(), childrenArraySubClass);
			}
			childrenArray.put(classObject);
		}
	}

}
