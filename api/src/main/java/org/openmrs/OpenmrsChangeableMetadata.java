/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs;

import java.util.Date;

public interface OpenmrsChangeableMetadata extends OpenmrsMetadata {
	
	@Override
	User getChangedBy();
	
	@Override
	Date getDateChanged();
	
	@Override
	void setChangedBy(User changedBy);
	
	@Override
	void setDateChanged(Date dateChanged);
	
}
