/*******************************************************************************
 * Copyright (c) 2004 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import org.eclipse.ecf.core.identity.ID;

/**
 * Restores Java objects from the underlying stream by using the classloader
 * returned from the call to given IClassLoaderMapper with the Namespace/ID
 * specified by the associated IdentifiableObjectOutputStream.
 * 
 * @author pnehrer
 * @author slewis
 */
public class IdentifiableObjectInputStream extends ObjectInputStream {

    IClassLoaderMapper mapper;
    
    public IdentifiableObjectInputStream(IClassLoaderMapper map, InputStream ins) throws IOException {
        super(ins);
        this.mapper = map;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    protected Class resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {

        ID annotateID = (ID) readObject();
        if (annotateID == null || mapper == null) {
            return super.resolveClass(desc);
        } else {
            ClassLoader cl = mapper.mapIDToClassLoader(annotateID);
            if (cl == null) return super.resolveClass(desc);
            else return cl.loadClass(desc.getName());
        }
    }

}
