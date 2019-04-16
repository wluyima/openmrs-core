/*
 * Add Copyright
 */
package org.openmrs.api.db.hibernate;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;

public class ExcludePersonsByGenderFilter {
	
	private String gender;
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Factory
	public Filter getFilter() {
		Query query = new TermQuery(new Term("gender", gender));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
	
}
