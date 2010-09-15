/*******************************************************************************
* Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.tests.server.generic;

import java.net.URI;

import org.eclipse.ecf.server.generic.IGenericServerContainerGroup;
import org.eclipse.ecf.server.generic.IGenericServerContainerGroupFactory;

import junit.framework.TestCase;

public class GenericServerContainerGroupFactoryTest extends TestCase {

	private static final String hostname = "localhost";
	private static final int port = 4000;
	
	private IGenericServerContainerGroupFactory gscgFactory;
	
	protected void setUp() throws Exception {
		super.setUp();
		gscgFactory = Activator.getDefault().getGenericServerContainerGroupFactory();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		gscgFactory = null;
	}
	
	public void testCreateContainerGroup() throws Exception {
		IGenericServerContainerGroup containerGroup = gscgFactory.createContainerGroup(hostname, port);
		assertNotNull(containerGroup);
		URI groupEndpoint = containerGroup.getGroupEndpoint();
		assertNotNull(groupEndpoint);
		assertTrue(groupEndpoint.getHost().equals(hostname));
		assertTrue(groupEndpoint.getPort()==port);
	}
}
