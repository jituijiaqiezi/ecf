/*******************************************************************************
 * Copyright (c) 2018 Composent, Inc. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Scott Lewis - initial API and implementation
 ******************************************************************************/
package com.mycorp.examples.osgi.async.consumer;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mycorp.examples.osgi.async.DescriptiveStatsService;

@Component
public class DescriptiveStatsServiceConsumer {

	@Reference
	void bindDescriptiveStatsService(DescriptiveStatsService svc) {
		svc.sum(100l,200l,300l,400l,500l).whenComplete((sum,t) -> {
			if (t != null)
				t.printStackTrace();
			else
				System.out.println("Long sum = "+sum);
		});
		
		List<Float> data = new ArrayList<Float>();
		data.add(10f);
		data.add(20f);
		data.add(30f);
		
		svc.mean(data).whenComplete((mean,t) -> {
			if (t != null)
				t.printStackTrace();
			else
				System.out.println("Float mean = "+mean);
		});
		
		List<Double> ddata = new ArrayList<Double>();
		ddata.add(1000.0);
		ddata.add(2000.0);
		ddata.add(3000.0);
		
		svc.sumsq(ddata).then(p -> {
			System.out.println("Double sumsq = "+p.getValue());
			return null;
		});

	}
}
