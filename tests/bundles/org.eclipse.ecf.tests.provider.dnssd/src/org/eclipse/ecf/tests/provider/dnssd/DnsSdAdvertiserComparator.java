/*******************************************************************************
 * Copyright (c) 2010 Markus Alexander Kuppe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.provider.dnssd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ecf.discovery.IServiceInfo;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TXTRecord;

public class DnsSdAdvertiserComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(final Object arg0, final Object arg1) {
		int result = -1;
		if (arg0 instanceof IServiceInfo && arg1 instanceof List) {
			final IServiceInfo serviceInfo = (IServiceInfo) arg0;
			result = (serviceInfo.getServiceProperties().size() + 5) * -1;
			
			final List records = (List) arg1;
			for (final Iterator itr = records.iterator(); itr.hasNext();) {
				final Record record = (Record) itr.next();
				if(record instanceof SRVRecord) {
					final SRVRecord srvRec = (SRVRecord) record;
					final String name = srvRec.getName().toString();
					if(name.startsWith("_" + DnsSdTestHelper.REG_SCHEME + "._" + DnsSdTestHelper.PROTO)) {
						final String target = srvRec.getTarget().toString();
						result += serviceInfo.getPriority() == srvRec.getPriority() ? 1 : -1;
						result += serviceInfo.getWeight() == srvRec.getWeight() ? 1 : -1;
						result += 3600 == srvRec.getTTL() ? 1 : -1;
						result += serviceInfo.getLocation().getHost().equals(target.substring(0, target.length() - 1)) ? 1 : -1;
						result += serviceInfo.getLocation().getPort() == srvRec.getPort() ? 1 : -1;
					}
				} else if(record instanceof TXTRecord) {
					final String[] str = record.rdataToString().split("=");
					final String key = str[0].substring(1);
					final String value = str[1].substring(0, str[1].length() - 1);
					final Object property = serviceInfo.getServiceProperties().getProperty(key);
					result += value.equals(property.toString()) ? 1 : -1;
				}
			}
		}
		return result;
	}
}
