package org.cloudbus.cloudsim.simulador;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;

public class exemplo {

	public static void main(String[] args) {
		
		CloudSim.init();
		
		DC dc= new DC("DC1");
		
		Host h1 = dc.CreateHost("Host1", 3000, 500000);
		Host h2 = dc.CreateHost("Host2", 16000, 1000000);
		Host h3 = dc.CreateHost("Host3", 48000, 1000000);
		Host h4 = dc.CreateHost("Host4", 8000, 500000);
			
		dc.setHost(h1);
		dc.setHost(h2);
		dc.setHost(h3);
		dc.setHost(h4);
		
		dc.CreateDatacenter();
		
		DatacenterBroker broker = new DatacenterBrokerSimple("Broker");
		
		List<Vm> vmlist = new ArrayList<Vm>();
		
		VM v= new VM(broker.getId());
		
		Vm v1 = v.add(h1, 1000, 500);
		Vm v2 = v.add(h2, 2000, 100);
		Vm v3 = v.add(h3, 3000, 500);
		Vm v4 = v.add(h4, 4000, 1000);
		
		vmlist.add(v1);
		vmlist.add(v2);
		vmlist.add(v3);
		vmlist.add(v4);
		
		List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		
		CL cl = new CL(broker.getId());
		
		cl.add(v1.getId(),(long)1000, 100, 100);
		cl.add(v2.getId(),(long)2000, 200, 300);
		cl.add(v3.getId(),(long)3000, 300, 400);
		cl.add(v4.getId(),(long)4000, 400, 100);
		
		cloudletList = cl.getList();
		
		broker.submitVmList(vmlist);
		broker.submitCloudletList(cloudletList);
		
		CloudSim.startSimulation();
		
		CloudSim.stopSimulation();
		
		
		CloudletsTableBuilderHelper.print(new TextTableBuilder(), broker.getCloudletsFinishedList());
		CloudSim.finishSimulation();
	}
	
	
}
