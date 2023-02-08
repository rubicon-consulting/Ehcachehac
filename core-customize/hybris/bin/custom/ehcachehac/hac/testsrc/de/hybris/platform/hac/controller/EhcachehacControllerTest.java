/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.hac.controller;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for {@link EhcachehacController}.
 */
@IntegrationTest
public class EhcachehacControllerTest
{

	/**
	 * Code under test.
	 */
	protected EhcachehacController cut;

	/**
	 * Set up the code under test.
	 */
	@Before
	public void setup()
	{
		cut = new EhcachehacController();
	}

	/**
	 * Clean up the code under test.
	 */
	@After
	public void teardown()
	{
		cut = null;
	}

	@Test
	public void testSayHello()
	{
	}
}
