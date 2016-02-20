/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.api.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Stack;

import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.openmrs.Obs;
import org.openmrs.api.IllegalPropertyChangeException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.ObsDAO;
import org.springframework.stereotype.Component;

/**
 * ImmutableEntityInterceptor for Obs, it catches any edited Obs, voids and replaces it with a new
 * one. I also sets the original Obs as the previous Obs for the newly created one. The exceptions
 * are when editing an already voided Obs
 * 
 * @see ImmutableEntityInterceptor
 * @since 2.0.0
 */
@Component("immutableObsInterceptor")
public class ImmutableObsInterceptor extends ImmutableEntityInterceptor {
	
	/*
	 * Use stacks to take care of nested transactions to avoid NPE since on each transaction
	 * completion the ThreadLocals get nullified, see code below, i.e a stack of two elements
	 * implies the element at the top of the stack is the inserts made in the inner/nested
	 * transaction
	 */
	private ThreadLocal<Stack<HashSet<Obs>>> newObservations = new ThreadLocal<>();
	
	private static final String[] MUTABLE_PROPERTY_NAMES = new String[] { "voided", "dateVoided", "voidedBy", "voidReason" };
	
	//We should have the same date field values for all new and voided obs in the transaction
	private ThreadLocal<Stack<Date>> date = new ThreadLocal<>();
	
	/**
	 * @see ImmutableEntityInterceptor#getSupportedType()
	 */
	@Override
	protected Class<?> getSupportedType() {
		return Obs.class;
	}
	
	/**
	 * @see ImmutableEntityInterceptor#getMutablePropertyNames()
	 */
	@Override
	protected String[] getMutablePropertyNames() {
		return MUTABLE_PROPERTY_NAMES;
	}
	
	/**
	 * @see ImmutableEntityInterceptor#ignoreVoidedOrRetiredObjects()
	 */
	@Override
	protected boolean ignoreVoidedOrRetiredObjects() {
		return true;
	}
	
	/**
	 * @see org.hibernate.EmptyInterceptor#afterTransactionBegin(Transaction) ()
	 */
	@Override
	public void afterTransactionBegin(Transaction tx) {
		
		if (newObservations.get() == null) {
			newObservations.set(new Stack<>());
		}
		newObservations.get().push(new HashSet<>());
		
		if (date.get() == null) {
			date.set(new Stack<>());
		}
		date.get().push(new Date());
	}
	
	/**
	 * @see ImmutableEntityInterceptor#onFlushDirty(Object, Serializable, Object[], Object[],
	 *      String[], Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
	                            String[] propertyNames, Type[] types) {
		
		try {
			return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
		}
		catch (IllegalPropertyChangeException e) {
			Obs obs = (Obs) entity;
			Obs newObs = Obs.newInstance(obs);
			newObs.setVoided(false);
			newObs.setVoidReason(null);
			newObs.setDateVoided(null);
			newObs.setVoidedBy(null);
			newObs.setCreator(Context.getAuthenticatedUser());
			newObs.setDateCreated(date.get().peek());
			newObs.setPreviousVersion(obs);
			
			newObservations.get().peek().add(newObs);
			
			//Revert the changes by copying back the original values and mark it as voided
			for (int i = 0; i < propertyNames.length; i++) {
				if ("voided".equals(propertyNames[i])) {
					currentState[i] = true;
				} else if ("voidedBy".equals(propertyNames[i])) {
					currentState[i] = Context.getAuthenticatedUser();
				} else if ("voidReason".equals(propertyNames[i])) {
					currentState[i] = "Voided and replaced with a new Obs";
				} else if ("dateVoided".equals(propertyNames[i])) {
					currentState[i] = date.get().peek();
				} else {
					currentState[i] = previousState[i];
				}
			}
			
			//Current state has been changed, so we have to return true
			return true;
		}
	}
	
	/**
	 * @see org.hibernate.EmptyInterceptor#beforeTransactionCompletion(Transaction)
	 */
	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		try {
			if (!newObservations.get().peek().isEmpty()) {
				ObsDAO obsDAO = Context.getRegisteredComponent("obsDAO", ObsDAO.class);
				newObservations.get().peek().forEach(obsDAO::saveObs);
			}
		}
		finally {
			//In case of nested transactions, the current transaction is complete, 
			//we are done with the Set of edited obs in it which is on top of the stack
			newObservations.get().pop();
			date.get().pop();
			
			//Get rid of the thread local now that the outer most transaction is also completed
			//This is useful especially in a web environment where threads are reused and can stick
			//around for long periods, we don't want these stacks to hang around too
			if (newObservations.get().empty()) {
				newObservations.remove();
			}
			if (date.get().empty()) {
				date.remove();
			}
		}
	}
}
