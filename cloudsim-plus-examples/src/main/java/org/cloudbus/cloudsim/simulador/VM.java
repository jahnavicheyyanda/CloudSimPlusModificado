/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSimple;
import org.cloudbus.cloudsim.schedulers.CloudletSchedulerTimeShared;

/**
 *
 * @author anselmo
 */
public class VM {
    
    private List<Vm> list;
            //Parametros das VMs
    private long size;
    private long bw;
    private String vmm;
    private int userId;
    private int indice;
    
    public VM(int userId){
        list = new LinkedList<>();
        size = 10000; //image size (MB)
        bw = 1000;// Largura de banda
        vmm = "Xen"; //VMM name
       this.userId =userId;
       indice = 0;
    }
    
    public void add(int ram,int mips,int pesNumber){
        Vm vm = new VmSimple(indice, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
        list.add(vm);
        indice ++;
    }
    
    public List<Vm>  getList(){   
        return list;
    }
}
