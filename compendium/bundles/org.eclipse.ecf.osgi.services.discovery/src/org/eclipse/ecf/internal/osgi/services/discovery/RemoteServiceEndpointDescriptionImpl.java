/*******************************************************************************
 * Copyright (c) 2009 Markus Alexander Kuppe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.discovery;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription;
import org.eclipse.ecf.osgi.services.discovery.RemoteServicePublication;

public class RemoteServiceEndpointDescriptionImpl extends
		RemoteServiceEndpointDescription {

	private final ID endpointId;
	private final IServiceID serviceId;
	private ID targetId = null;

	public RemoteServiceEndpointDescriptionImpl(IServiceInfo serviceInfo) {
		super(((ServiceProperties) serviceInfo.getServiceProperties())
				.asProperties());
		this.serviceId = serviceInfo.getServiceID();

		// create the endpoint id
		IServiceProperties serviceProperties = serviceInfo
				.getServiceProperties();
		final byte[] endpointBytes = serviceProperties
				.getPropertyBytes(RemoteServicePublication.ENDPOINT_CONTAINERID);
		if (endpointBytes == null)
			throw new IDCreateException(
					"ServiceEndpointDescription endpointBytes cannot be null");
		final String endpointStr = new String(endpointBytes);
		final String namespaceStr = serviceProperties
				.getPropertyString(RemoteServicePublication.ENDPOINT_CONTAINERID_NAMESPACE);
		if (namespaceStr == null) {
			throw new IDCreateException(
					"ServiceEndpointDescription namespaceStr cannot be null");
		}
		endpointId = IDFactory.getDefault().createID(namespaceStr, endpointStr);

		// create the target id, if it's there
		final byte[] targetBytes = serviceProperties
				.getPropertyBytes(RemoteServicePublication.TARGET_CONTAINERID);
		// If this is null, we're ok with it
		if (targetBytes != null) {
			final String targetStr = new String(endpointBytes);
			String targetNamespaceStr = serviceProperties
					.getPropertyString(RemoteServicePublication.TARGET_CONTAINERID_NAMESPACE);
			if (targetNamespaceStr == null)
				targetNamespaceStr = namespaceStr;
			targetId = IDFactory.getDefault().createID(targetNamespaceStr,
					targetStr);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription
	 * #getECFEndpointID()
	 */
	public ID getEndpointAsID() throws IDCreateException {
		return endpointId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.osgi.services.discovery.RemoteServiceEndpointDescription
	 * #getECFTargetID()
	 */
	public ID getConnectTargetID() {
		return targetId;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteServiceEndpointDescriptionImpl other = (RemoteServiceEndpointDescriptionImpl) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	public IServiceID getServiceID() {
		return serviceId;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("ServiceEndpointDescriptionImpl["); //$NON-NLS-1$
		sb.append(";providedinterfaces=").append(getProvidedInterfaces()); //$NON-NLS-1$
		sb.append(";location=").append(getLocation()); //$NON-NLS-1$
		sb.append(";serviceid=").append(getServiceID()); //$NON-NLS-1$
		sb.append(";osgiEndpointID=").append(getEndpointID()); //$NON-NLS-1$
		sb.append(";ecfEndpointID=").append(getEndpointAsID()); //$NON-NLS-1$
		sb.append(";ecfTargetID=").append(getConnectTargetID()); //$NON-NLS-1$
		sb.append(";ecfFilter=").append(getRemoteServicesFilter()); //$NON-NLS-1$
		sb.append(";props=").append(getProperties()).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

}