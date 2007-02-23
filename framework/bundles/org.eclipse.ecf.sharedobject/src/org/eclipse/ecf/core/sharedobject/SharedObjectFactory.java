/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.sharedobject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.ecf.core.sharedobject.provider.ISharedObjectInstantiator;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.core.sharedobject.Activator;
import org.eclipse.ecf.internal.core.sharedobject.Messages;
import org.eclipse.ecf.internal.core.sharedobject.SharedObjectDebugOptions;

/**
 * Factory for creating {@link ISharedObject} instances. This class provides ECF
 * clients an entry point to constructing {@link ISharedObject} instances. <br>
 */
public class SharedObjectFactory implements ISharedObjectFactory {

	private static Hashtable sharedobjectdescriptions = new Hashtable();

	protected static ISharedObjectFactory instance = null;

	static {
		instance = new SharedObjectFactory();
	}

	protected SharedObjectFactory() {
	}

	public static ISharedObjectFactory getDefault() {
		return instance;
	}

	private static void trace(String msg) {
		Trace.trace(Activator.getDefault(), msg);
	}

	private static void dumpStack(String msg, Throwable e) {
		Trace.catching(Activator.getDefault(),
				SharedObjectDebugOptions.EXCEPTIONS_CATCHING,
				SharedObjectFactory.class, "dumpStack", e); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#addDescription(org.eclipse.ecf.core.SharedObjectTypeDescription)
	 */
	public SharedObjectTypeDescription addDescription(
			SharedObjectTypeDescription description) {
		trace("addDescription(" + description + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		return addDescription0(description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#getDescriptions()
	 */
	public List getDescriptions() {
		return getDescriptions0();
	}

	protected List getDescriptions0() {
		return new ArrayList(sharedobjectdescriptions.values());
	}

	protected SharedObjectTypeDescription addDescription0(
			SharedObjectTypeDescription n) {
		if (n == null)
			return null;
		return (SharedObjectTypeDescription) sharedobjectdescriptions.put(n
				.getName(), n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#containsDescription(org.eclipse.ecf.core.SharedObjectTypeDescription)
	 */
	public boolean containsDescription(SharedObjectTypeDescription scd) {
		return containsDescription0(scd);
	}

	protected boolean containsDescription0(SharedObjectTypeDescription scd) {
		if (scd == null)
			return false;
		return sharedobjectdescriptions.containsKey(scd.getName());
	}

	protected SharedObjectTypeDescription getDescription0(
			SharedObjectTypeDescription scd) {
		if (scd == null)
			return null;
		return (SharedObjectTypeDescription) sharedobjectdescriptions.get(scd
				.getName());
	}

	protected SharedObjectTypeDescription getDescription0(String name) {
		if (name == null)
			return null;
		return (SharedObjectTypeDescription) sharedobjectdescriptions.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#getDescriptionByName(java.lang.String)
	 */
	public SharedObjectTypeDescription getDescriptionByName(String name)
			throws SharedObjectCreateException {
		trace("getDescriptionByName(" + name + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		SharedObjectTypeDescription res = getDescription0(name);
		if (res == null) {
			throw new SharedObjectCreateException(
					Messages.SharedObjectFactory_Exception_Create_Shared_Object + name
							+ Messages.SharedObjectFactory_Exception_Create_Shared_Object_Not_Found);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(org.eclipse.ecf.core.SharedObjectTypeDescription,
	 *      java.lang.Object[])
	 */
	public ISharedObject createSharedObject(SharedObjectTypeDescription desc,
			Object[] args) throws SharedObjectCreateException {
		trace("createSharedObject(" + desc + "," //$NON-NLS-1$ //$NON-NLS-2$
				+ Trace.getArgumentsString(args) + ")"); //$NON-NLS-1$
		if (desc == null)
			throw new SharedObjectCreateException(
					Messages.SharedObjectFactory_Description_Not_Null);
		SharedObjectTypeDescription cd = getDescription0(desc);
		if (cd == null)
			throw new SharedObjectCreateException(
					Messages.SharedObjectFactory_Exception_Create_Shared_Objec + desc.getName()
							+ Messages.SharedObjectFactory_Exception_Create_Shared_Object_Not_Found);
		ISharedObjectInstantiator instantiator = null;
		try {
			instantiator = cd.getInstantiator();
		} catch (Exception e) {
			SharedObjectCreateException newexcept = new SharedObjectCreateException(
					Messages.SharedObjectFactory_Exception_Create_With_Description + desc
							+ ": " + e.getClass().getName() + ": " //$NON-NLS-1$ //$NON-NLS-2$
							+ e.getMessage(), e);
			dumpStack("Exception in createSharedObject", newexcept); //$NON-NLS-1$
			throw newexcept;
		}
		if (instantiator == null)
			throw new SharedObjectCreateException(
					Messages.SharedObjectFactory_Exception_Create_Instantiator + cd.getName()
							+ Messages.SharedObjectFactory_Exception_Create_Instantiator_Null);
		// Ask instantiator to actually create instance
		return instantiator.createInstance(desc, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(java.lang.String)
	 */
	public ISharedObject createSharedObject(String descriptionName)
			throws SharedObjectCreateException {
		return createSharedObject(getDescriptionByName(descriptionName), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(java.lang.String,
	 *      java.lang.Object[])
	 */
	public ISharedObject createSharedObject(String descriptionName,
			Object[] args) throws SharedObjectCreateException {
		return createSharedObject(getDescriptionByName(descriptionName), args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#removeDescription(org.eclipse.ecf.core.SharedObjectTypeDescription)
	 */
	public SharedObjectTypeDescription removeDescription(
			SharedObjectTypeDescription scd) {
		trace("removeDescription(" + scd + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		return removeDescription0(scd);
	}

	protected SharedObjectTypeDescription removeDescription0(
			SharedObjectTypeDescription n) {
		if (n == null)
			return null;
		return (SharedObjectTypeDescription) sharedobjectdescriptions.remove(n
				.getName());
	}
}