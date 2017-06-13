/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSimple;
import org.cloudbus.cloudsim.schedulers.CloudletSchedulerTimeShared;

/**
 *
 * @author anselmo
 */
public class VM extends NumRandom{
    
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
    
    public VM(int userId,int indice){
    	list = new LinkedList<>();
        size = 10000; //image size (MB)
        bw = 1000;// Largura de banda
        vmm = "Xen"; //VMM name
       this.userId =userId;
       this.indice = indice;
    }
    
    public int getIndice(){
    	return indice;
    }
    
    public Vm add(int ram,int mips,int pesNumber){
        Vm vm = new VmSimple(indice, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
        list.add(vm);
        indice ++;
        return vm;
    }
    
    public Vm add(Host h,int ram,int mips,int pesNumber){
        Vm vm = new VmSimple(indice, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
        vm.setHost(h);
        list.add(vm);
        indice ++;
        return vm;
    }

    
     public void CreateVMs(int n){
            int mips[]={100,200,250,400,500,700,1000};//7
            int ram[]={512,1000,2000};
            int pesN[]={1,2};
            for(int i=0;i<n;i++)
            add( ram[rdm(3)],mips[rdm(7)], pesN[rdm(2)]);
            
        }
    public List<Vm>  getList(){   

        
        return list;
    }
}
