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

package edu.isi.karma.modeling;

import edu.isi.karma.service.Namespaces;

public interface ModelingParams{

	// URIs
	public static String THING_URI = Namespaces.OWL + "Thing";
	public static String HAS_SUBCLASS_URI = "http://example.com#hasSubClass";
	public static final String SUBCLASS_URI = Namespaces.RDFS + "subClassOf"; 

	// Building Graph
	public static double DEFAULT_WEIGHT = 1.0;	
	public static double MIN_WEIGHT = 0.000001; // need to be fixed later	
	public static double MAX_WEIGHT = 1000000;

	// Prefixes
	public static String KARMA_SOURCE_PREFIX = "http://isi.edu/integration/karma/sources/";
	public static String KARMA_SERVICE_PREFIX = "http://isi.edu/integration/karma/services/";

	
}
