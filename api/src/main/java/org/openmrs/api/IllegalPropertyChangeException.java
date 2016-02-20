/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.api;

import org.openmrs.api.context.Context;
import org.openmrs.util.LocaleUtility;

import java.util.Locale;

/**
 * An instance of this exception is thrown when an immutable property of a persistent object is
 * changed.
 * 
 * @since 2.0.0
 */
public class IllegalPropertyChangeException extends APIException {
	
	public static final long serialVersionUID = 1L;
	
	public IllegalPropertyChangeException() {
		this("Editing some entities is not permitted");
	}
	
	/**
	 * General constructor to give the end user a helpful message that relates to why this error
	 * occurred.
	 *
	 * @param message helpful message string for the end user
	 */
	public IllegalPropertyChangeException(String message) {
		super(message);
	}
	
	/**
	 * General constructor to give the end user a helpful message and to also propagate the parent
	 * error exception message.
	 *
	 * @param message helpful message string for the end user
	 * @param cause the parent exception cause that this ImmutableEntityException is wrapping around
	 */
	public IllegalPropertyChangeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor used to simply chain a parent exception cause to an ImmutableEntityException.
	 * Preference should be given to the {@link #IllegalPropertyChangeException(Throwable)} (String,
	 * Throwable)} constructor if at all possible instead of this one.
	 *
	 * @param cause the parent exception cause that this ImmutableEntityException is wrapping around
	 */
	public IllegalPropertyChangeException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor to give the end user a helpful message that relates to why this error occurred.
	 *
	 * @param messageKey message code to retrieve
	 * @param parameters message parameters
	 */
	public IllegalPropertyChangeException(String messageKey, Object[] parameters) {
		super(Context.getMessageSourceService().getMessage(messageKey, parameters, Context.getLocale()));
	}
	
	/**
	 * Constructor to give the end user a helpful message and to also propagate the parent error
	 * exception message.
	 *
	 * @param messageKey message code to retrieve
	 * @param parameters message parameters
	 * @param cause the parent exception cause that this IllegalPropertyChangeException is wrapping
	 *            around
	 */
	public IllegalPropertyChangeException(String messageKey, Object[] parameters, Throwable cause) {
		super(Context.getMessageSourceService().getMessage(messageKey, parameters, Context.getLocale()), cause);
	}
	
}
