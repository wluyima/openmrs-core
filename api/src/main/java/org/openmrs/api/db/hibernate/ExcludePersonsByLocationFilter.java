/*
 * Add Copyright
 */
package org.openmrs.api.db.hibernate;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;

public class ExcludePersonsByLocationFilter extends QueryWrapperFilter {
	
	public ExcludePersonsByLocationFilter() {
		//We could fetch the location of the currently logged in user and filter on that
		//The location could be a user property or person attribute of the logged in user
		super(new TermQuery(new Term("value", "someValue")));
	}
}
