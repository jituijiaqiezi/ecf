/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.sharedobject.events;

import org.eclipse.ecf.core.identity.ID;

public class SharedObjectActivatedEvent implements ISharedObjectActivatedEvent {
	private ID activatedID;

	private ID localContainerID;

	/**
	 * @since 2.6
	 */
	public SharedObjectActivatedEvent() {

	}

	public SharedObjectActivatedEvent(ID container, ID act) {
		super();
		this.localContainerID = container;
		this.activatedID = act;
	}

	public ID getActivatedID() {
		return activatedID;
	}

	public ID getLocalContainerID() {
		return localContainerID;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("SharedObjectActivatedEvent["); //$NON-NLS-1$
		sb.append(getLocalContainerID()).append(";"); //$NON-NLS-1$
		sb.append(getActivatedID()).append("]"); //$NON-NLS-1$
		return sb.toString();
	}
}