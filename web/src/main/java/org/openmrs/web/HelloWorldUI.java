/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class HelloWorldUI extends UI {
	
	List<Person> persons = new ArrayList<>();
	
	DataProvider dataProvider = new ListDataProvider(persons);
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		GridLayout inputsLayout = new GridLayout(2, 2);
		inputsLayout.setSpacing(true);
		inputsLayout.addComponent(new Label("First Name:"));
		TextField firstname = new TextField();
		inputsLayout.addComponent(firstname);
		inputsLayout.addComponent(new Label("Last Name:"));
		TextField lastname = new TextField();
		inputsLayout.addComponent(lastname);
		
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		formLayout.addComponent(new Label("<b>Add New Person:</b>", ContentMode.HTML));
		formLayout.addComponent(inputsLayout);
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		Button saveButton = new Button("Save", clickEvent -> {
			persons.add(new Person(firstname.getValue(), lastname.getValue()));
			firstname.setValue("");
			lastname.setValue("");
			dataProvider.refreshAll();
		});
		
		buttonsLayout.addComponent(saveButton);
		Button clearButton = new Button("Clear", clickEvent -> {
			firstname.setValue("");
			lastname.setValue("");
		});
		
		buttonsLayout.addComponent(clearButton);
		formLayout.addComponent(buttonsLayout);
		
		HorizontalLayout pageLayout = new HorizontalLayout(formLayout);
		pageLayout.setSpacing(true);
		persons.add(new Person("George", "Semakula"));
		persons.add(new Person("Luke", "Simu"));
		Grid<Person> personsGrid = new Grid(Person.class);
		personsGrid.getColumn("firstname").setCaption("First Name");
		personsGrid.getColumn("lastname").setCaption("Last Name");
		personsGrid.setCaption("<b>Persons<b/>");
		personsGrid.setCaptionAsHtml(true);
		
		personsGrid.setDataProvider(dataProvider);
		pageLayout.addComponent(personsGrid);
		pageLayout.addComponent(formLayout);
		setContent(pageLayout);
	}
	
	//private create
	
	public class Person {
		
		private String firstname;
		
		private String lastname;
		
		Person(String firstname, String lastname) {
			this.firstname = firstname;
			this.lastname = lastname;
		}
		
		public String getLastname() {
			return lastname;
		}
		
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		
		public String getFirstname() {
			return firstname;
		}
		
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
	}
	
}
