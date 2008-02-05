/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.remoteservice;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ecf.internal.remoteservice.messages"; //$NON-NLS-1$
	public static String RemoteCallMethod_EXCEPTION_ARG_WRONG_TYPE;
	public static String RemoteCallMethod_EXCEPTION_ARGS_NOT_RIGHT_LENGTH;
	public static String RemoteCallMethod_EXCEPTION_PARAMETER_NOT_SERIALIZABLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		// nothing
	}
}
