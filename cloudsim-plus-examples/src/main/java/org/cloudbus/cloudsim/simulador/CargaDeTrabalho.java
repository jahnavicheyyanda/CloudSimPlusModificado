package org.cloudbus.cloudsim.simulador;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;

public class CargaDeTrabalho {
	
	public int n;
	
	public CargaDeTrabalho(int n) {
		this.n = n;
	}
	
	public void gerar(DatacenterBroker broker){
      CL c = new CL(broker.getId(),0);
      //c.add(2,4000, 3,300,300);//especifica para qual VM o cloudlet sera enviado
      c.CreateCoudLets(n);
      List<Cloudlet> cloudletList = c.getList();
      broker.submitCloudletList(cloudletList);
	}
	
	
	
		
}
