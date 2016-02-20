/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the base for spring/context tests. Tests that NEED to use calls to the Context class and
 * use Services and/or the database should extend this class. NOTE: Tests that do not need access to
 * spring enabled services do not need this class and extending this will only slow those test cases
 * down. (because spring is started before test cases are run). Normal test cases do not need to
 * extend anything
 */
@Transactional
public abstract class BaseContextSensitiveTest extends BaseNotTransactionalContextSensitiveTest {
	
	private static Log log = LogFactory.getLog(BaseContextSensitiveTest.class);
	
}
