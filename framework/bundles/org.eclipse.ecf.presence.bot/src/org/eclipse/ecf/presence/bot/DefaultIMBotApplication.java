/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.presence.bot;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.ecf.internal.presence.bot.Activator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class DefaultIMBotApplication implements IApplication {

	protected Map getIMBotsFromExtensionRegistry() {
		return Activator.getDefault().getIMBots();
	}

	public Object start(IApplicationContext context) throws Exception {

		Map bots = getIMBotsFromExtensionRegistry();

		for (Iterator it = bots.values().iterator(); it.hasNext();) {
			IIMBotEntry entry = (IIMBotEntry) it.next();
			// Create default im bot
			DefaultIMBot bot = new DefaultIMBot(entry);
			// connect
			bot.connect();
		}

		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {
				break;
			}
		}

		return IApplication.EXIT_OK;
	}

	public void stop() {
	}

}
