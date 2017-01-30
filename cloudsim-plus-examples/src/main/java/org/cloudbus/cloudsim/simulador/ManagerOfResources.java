/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import java.util.List;
import org.cloudbus.cloudsim.Log;
/**
 *
 * @author thejott
 */
public class ManagerOfResources {
    private Datacenter datacenter;
    private List<Host> ListHost;

    public ManagerOfResources(Datacenter datacenter) {
        this.datacenter = datacenter;
        ListHost = datacenter.getHostList();
        printHost();
    }
    
    public void printHost(){
      for(Host h:ListHost)
        Log.printFormattedLine(h.getId()+" "+h.getRamCapacity()+" "+h.getStorageCapacity()+" "+h.getPeList().get(h.getId()).getMips());
    }
    
    
}
