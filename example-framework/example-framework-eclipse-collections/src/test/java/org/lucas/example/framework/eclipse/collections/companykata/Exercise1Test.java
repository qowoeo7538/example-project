/*
 * Copyright (c) 2018 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.lucas.example.framework.eclipse.collections.companykata;

import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Below are links to APIs that may be helpful during these exercises.
 * <p>
 * <p/>
 * {@link MutableList#collect(Function)}<br>
 * {@link MutableList#select(Predicate)}<br>
 *
 * @see <a href="http://eclipse.github.io/eclipse-collections-kata/company-kata/#/1">Exercise 1 Slides</a>
 */
public class Exercise1Test extends CompanyDomainForKata {
    /**
     * Get the name of each of the company's customers.
     */
    @Test
    public void getCustomerNames() {
        Function<Customer, String> nameFunction = Customer::getName;
        MutableList<String> customerNames = this.company.getCustomers() // this.company.getCustomers()...
                .collect(nameFunction);

        MutableList<String> expectedNames = Lists.mutable.with("Fred", "Mary", "Bill");
        Assert.assertEquals(expectedNames, customerNames);
    }

    /**
     * Get the city for each of the company's customers.
     */
    @Test
    public void getCustomerCities() {
        Function<Customer, String> cityFunction = Customer::getCity;
        MutableList<String> customerCities = this.company.getCustomers() // this.company.getCustomers()...
                .collect(cityFunction);

        MutableList<String> expectedCities = Lists.mutable.with("London", "Liphook", "London");
        Assert.assertEquals(expectedCities, customerCities);
    }

    /**
     * Which customers come from London? Get a collection of those which do.
     */
    @Test
    public void getLondonCustomers() {
        MutableList<Customer> customersFromLondon = this.company.getCustomers() // this.company.getCustomers()...
                .select(customer -> "London".equals(customer.getCity()));

        Verify.assertSize("Should be 2 London customers", 2, customersFromLondon);
    }
}
