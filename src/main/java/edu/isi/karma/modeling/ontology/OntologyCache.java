package edu.isi.karma.modeling.ontology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OntologyCache {
	
	static Logger logger = Logger.getLogger(OntologyCache.class.getName());

	// hashmap: class -> properties whose domain(direct) includes this class 
	private static HashMap<String, List<String>> directOutDataProperties; 
	private static HashMap<String, List<String>> indirectOutDataProperties; 
	private static HashMap<String, List<String>> directOutObjectProperties; 
	private static HashMap<String, List<String>> indirectOutObjectProperties; 
	// hashmap: class -> properties whose range(direct) includes this class 
	private static HashMap<String, List<String>> directInObjectProperties; 
	private static HashMap<String, List<String>> indirectInObjectProperties;
	
	// hashmap: property -> direct domains
	private static HashMap<String, List<String>> propertyDirectDomains;
	private static HashMap<String, List<String>> propertyIndirectDomains;
	// hashmap: property -> direct ranges
	private static HashMap<String, List<String>> propertyDirectRanges;
	private static HashMap<String, List<String>> propertyIndirectRanges;
	
	// hashmap: domain+range -> object properties
	private static HashMap<String, List<String>> directDomainRangeProperties;
	private static HashMap<String, List<String>> indirectDomainRangeProperties;

	public HashMap<String, List<String>> getDirectOutDataProperties() {
		return directOutDataProperties;
	}

	public HashMap<String, List<String>> getIndirectOutDataProperties() {
		return indirectOutDataProperties;
	}

	public HashMap<String, List<String>> getDirectOutObjectProperties() {
		return directOutObjectProperties;
	}

	public HashMap<String, List<String>> getIndirectOutObjectProperties() {
		return indirectOutObjectProperties;
	}

	public HashMap<String, List<String>> getDirectInObjectProperties() {
		return directInObjectProperties;
	}

	public HashMap<String, List<String>> getIndirectInObjectProperties() {
		return indirectInObjectProperties;
	}

	public HashMap<String, List<String>> getPropertyDirectDomains() {
		return propertyDirectDomains;
	}

	public HashMap<String, List<String>> getPropertyIndirectDomains() {
		return propertyIndirectDomains;
	}

	public HashMap<String, List<String>> getPropertyDirectRanges() {
		return propertyDirectRanges;
	}

	public HashMap<String, List<String>> getPropertyIndirectRanges() {
		return propertyIndirectRanges;
	}

	
	public HashMap<String, List<String>> getDirectDomainRangeProperties() {
		return directDomainRangeProperties;
	}

	public HashMap<String, List<String>> getIndirectDomainRangeProperties() {
		return indirectDomainRangeProperties;
	}
	
	private static OntologyCache _InternalInstance = null;
	public static OntologyCache Instance()
	{
		if (_InternalInstance == null)
		{
			directOutDataProperties = new HashMap<String, List<String>>();
			indirectOutDataProperties = new HashMap<String, List<String>>();
			directOutObjectProperties = new HashMap<String, List<String>>();
			indirectOutObjectProperties = new HashMap<String, List<String>>();
			directInObjectProperties = new HashMap<String, List<String>>();
			indirectInObjectProperties = new HashMap<String, List<String>>();
			
			propertyDirectDomains = new HashMap<String, List<String>>();
			propertyIndirectDomains = new HashMap<String, List<String>>();
			propertyDirectRanges = new HashMap<String, List<String>>();
			propertyIndirectRanges = new HashMap<String, List<String>>();
			
			directDomainRangeProperties = new HashMap<String, List<String>>();
			indirectDomainRangeProperties = new HashMap<String, List<String>>();
			
			_InternalInstance = new OntologyCache();
			
			_InternalInstance.init();
			
		}
		return _InternalInstance;
	}

	private void init() {

		long start = System.currentTimeMillis();
		fillDataPropertiesHashMaps();
		fillObjectPropertiesHashMaps();
		float elapsedTimeSec = (System.currentTimeMillis() - start)/1000F;
		logger.info("time to build ontology cache: " + elapsedTimeSec);
	}

	
	private void fillObjectPropertiesHashMaps() {
		
		List<OntResource> directDomains = new ArrayList<OntResource>();
		List<OntResource> allDomains = new ArrayList<OntResource>();
		List<OntResource> directRanges = new ArrayList<OntResource>();
		List<OntResource> allRanges = new ArrayList<OntResource>();
		List<String> temp; 
		
		ExtendedIterator<ObjectProperty> itrOP = OntologyManager.Instance().getOntModel().listObjectProperties();
		OntResource d;
		OntResource r;
		
		while (itrOP.hasNext()) {
			
			directDomains.clear();
			allDomains.clear();
			directRanges.clear();
			allRanges.clear();
			
			ObjectProperty op = itrOP.next();
			
			// getting domains and subclasses
			ExtendedIterator<? extends OntResource> itrDomains = op.listDomain();
			while (itrDomains.hasNext()) {
				d = itrDomains.next();
				OntologyManager.Instance().getMembers(d, directDomains, false);
			}

			propertyDirectDomains.put(op.getURI(), OntologyManager.Instance().getResourcesURIs(directDomains));
			
			for (int i = 0; i < directDomains.size(); i++) {
				temp = directOutObjectProperties.get(directDomains.get(i).getURI());
				if (temp == null) {
					temp = new ArrayList<String>();
					directOutObjectProperties.put(directDomains.get(i).getURI(), temp);
				}
				temp.add(op.getURI());
			}

			for (int i = 0; i < directDomains.size(); i++) {
				allDomains.add(directDomains.get(i));
				OntologyManager.Instance().getChildren(directDomains.get(i), allDomains, true);
			}

			propertyIndirectDomains.put(op.getURI(), OntologyManager.Instance().getResourcesURIs(allDomains));

			for (int i = 0; i < allDomains.size(); i++) {
				temp = indirectOutObjectProperties.get(allDomains.get(i).getURI());
				if (temp == null) { 
					temp = new ArrayList<String>();
					indirectOutObjectProperties.put(allDomains.get(i).getURI(), temp);
				}
				temp.add(op.getURI());
			}
			
			// getting ranges and subclasses
			ExtendedIterator<? extends OntResource> itrRanges = op.listRange();
			while (itrRanges.hasNext()) {
				r = itrRanges.next();
				OntologyManager.Instance().getMembers(r, directRanges, false);
			}

			propertyDirectRanges.put(op.getURI(), OntologyManager.Instance().getResourcesURIs(directRanges));
			
			for (int i = 0; i < directRanges.size(); i++) {
				temp = directInObjectProperties.get(directRanges.get(i).getURI());
				if (temp == null) {
					temp = new ArrayList<String>();
					directInObjectProperties.put(directRanges.get(i).getURI(), temp);
				}
				temp.add(op.getURI());
			}
			
			for (int i = 0; i < directRanges.size(); i++) {
				allRanges.add(directRanges.get(i));
				OntologyManager.Instance().getChildren(directRanges.get(i), allRanges, true);
			}
			
			propertyIndirectRanges.put(op.getURI(), OntologyManager.Instance().getResourcesURIs(allRanges));
			
			for (int i = 0; i < allRanges.size(); i++) {
				temp = indirectInObjectProperties.get(allRanges.get(i).getURI());
				if (temp == null) {
					temp = new ArrayList<String>();
					indirectInObjectProperties.put(allRanges.get(i).getURI(), temp);
				}
				temp.add(op.getURI());
			}
			
			for (int i = 0; i < directDomains.size(); i++) {
				for (int j = 0; j < directRanges.size(); j++) {
					temp = 
						directDomainRangeProperties.get(directDomains.get(i).toString() + directRanges.get(j).toString());
					if (temp == null) {
						temp = new ArrayList<String>();
						directDomainRangeProperties.put(directDomains.get(i).toString() + directRanges.get(j).toString(), temp);
					}
					if (temp.indexOf(op.getURI()) == -1)
						temp.add(op.getURI());
				}
			}
			
			for (int i = 0; i < allDomains.size(); i++) {
				for (int j = 0; j < allRanges.size(); j++) {
					temp = 
						indirectDomainRangeProperties.get(allDomains.get(i).toString() + allRanges.get(j).toString());
					if (temp == null) {
						temp = new ArrayList<String>();
						indirectDomainRangeProperties.put(allDomains.get(i).toString() + allRanges.get(j).toString(), temp);
					}
					if (temp.indexOf(op.getURI()) == -1)
						temp.add(op.getURI());
				}
			}

		}	

	}
	
	private void fillDataPropertiesHashMaps() {
		
		List<OntResource> directDomains = new ArrayList<OntResource>();
		List<OntResource> allDomains = new ArrayList<OntResource>();
		List<OntResource> directRanges = new ArrayList<OntResource>();
		List<OntResource> allRanges = new ArrayList<OntResource>();
		List<String> temp; 
		
		ExtendedIterator<DatatypeProperty> itrDP = OntologyManager.Instance().getOntModel().listDatatypeProperties();
		OntResource d;
		OntResource r;
		
		while (itrDP.hasNext()) {
			
			directDomains.clear();
			allDomains.clear();
			directRanges.clear();
			allRanges.clear();
			
			DatatypeProperty dp = itrDP.next();
			
			// getting domains and subclasses
			ExtendedIterator<? extends OntResource> itrDomains = dp.listDomain();
			while (itrDomains.hasNext()) {
				d = itrDomains.next();
				OntologyManager.Instance().getMembers(d, directDomains, false);
			}

			propertyDirectDomains.put(dp.getURI(), OntologyManager.Instance().getResourcesURIs(directDomains));
			
			for (int i = 0; i < directDomains.size(); i++) {
				temp = directOutDataProperties.get(directDomains.get(i).getURI());
				if (temp == null) {
					temp = new ArrayList<String>();
					directOutDataProperties.put(directDomains.get(i).getURI(), temp);
				}
				temp.add(dp.getURI());
			}

			for (int i = 0; i < directDomains.size(); i++) {
				allDomains.add(directDomains.get(i));
				OntologyManager.Instance().getChildren(directDomains.get(i), allDomains, true);
			}

			propertyIndirectDomains.put(dp.getURI(), OntologyManager.Instance().getResourcesURIs(allDomains));

			for (int i = 0; i < allDomains.size(); i++) {
				temp = indirectOutDataProperties.get(allDomains.get(i).getURI());
				if (temp == null) { 
					temp = new ArrayList<String>();
					indirectOutDataProperties.put(allDomains.get(i).getURI(), temp);
				}
				temp.add(dp.getURI());
			}
			
			// getting ranges and subclasses
			ExtendedIterator<? extends OntResource> itrRanges = dp.listRange();
			while (itrRanges.hasNext()) {
				r = itrRanges.next();
				OntologyManager.Instance().getMembers(r, directRanges, false);
			}

			propertyDirectRanges.put(dp.getURI(), OntologyManager.Instance().getResourcesURIs(directRanges));

			
			for (int i = 0; i < directRanges.size(); i++) {
				allRanges.add(directRanges.get(i));
				OntologyManager.Instance().getChildren(directRanges.get(i), allRanges, true);
			}
			
			propertyIndirectRanges.put(dp.getURI(), OntologyManager.Instance().getResourcesURIs(allRanges));

		}		
	}
}
