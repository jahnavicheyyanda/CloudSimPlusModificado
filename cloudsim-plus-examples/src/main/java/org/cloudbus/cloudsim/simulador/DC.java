/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.DatacenterCharacteristicsSimple;
import org.cloudbus.cloudsim.DatacenterSimple;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostSimple;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Bandwidth;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.resources.Ram;
import org.cloudbus.cloudsim.schedulers.VmSchedulerTimeShared;


 /**
      * Classe que cria um datacenter
      * @param name //Nome do Datacenter
      * @return datacenter //datacenter criado
      * 
      *     O data center no CloudSim gerencia uma quantidade de máquinas físicas,
      * que no simulador é chamado de host. Para as máquinas físicas são designadas uma ou
      * mais máquinas virtuais dependendo na política de alocação de máquinas virtuais que foi
      * definido pelo provedor do serviço da nuvem. Aqui a política serve para as operações de
      * controle relacionados ao ciclo de vida das máquinas virtuais, como designar uma máquina
      * física à uma máquina virtual, criação de máquina virtual, a destruição das mesmas e a
      * migração das máquinas virtuais.
      *     Um data center pode gerenciar diversas máquinas físicas, que por sua vez
      * gerencia os ciclos de vidas das máquinas virtuais.
      */
public class DC extends NumRandom{
    private List<Host> hostList;
    private List<Pe> peList;
    private int hostId;
    private long bw;
    private String name;
    private int mips;
    
    
    public DC(String name){
        hostList = new ArrayList<>();
        peList = new ArrayList<>();
                bw = 10000;
        this.name=name;
        hostId=0;
        mips =1000;
    }
    
    /*
     * Os Hosts Possuem informações sobre a quantidade de
     * processamento, armazenamento, largura de banda e quantidade de memória disponível.
     * Responsável por compartilhar recursos entre as máquinas virtuais.
     */
    public Host CreateHost(String Nome,int ram,long storage){
        peList.add(new PeSimple(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        
        /*
         * VmScheduler implementa as políticas de distribuição de processamento da
         * máquina física entre as máquinas virtuais alocadas nesta máquina física.
         */
        
        //Lista de maquinas Físicas (Host)
        Host h = new HostSimple(Nome,hostId,new ResourceProvisionerSimple<>(new Ram(ram)),new ResourceProvisionerSimple<>(new Bandwidth(bw)), storage, peList,new VmSchedulerTimeShared(peList));
        hostList.add(h); 
        hostId++;//id do próximo Host
        return h;
    
    }
    public void setHost(Host h){
    	hostList.add(h); 
    }


    
//        public void CreateHosts(int n){
//            int mips[]={500,1000,1500,2000};
//            int ram[]={512,1000,2000,4000};
//            long storage[]={500000,1000000,1500000,2000000};
//            for(int i=0;i<n;i++)
//            CreateHost(mips[rdm(4)], ram[rdm(4)], storage[rdm(4)]);
//            
//        }
//      
        public DatacenterSimple CreateDatacenter() {

        

        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // Zona horaria onde está situado o datacenter
        double cost = 0.0; // Custo por hora de uma instância
        double costPerMem = 0.0; // Custo por usar RAM
        double costPerStorage = 0.0; // Custo por usar o armazenamento
        double costPerBw = 0.0; // custo por usar BW
       
        
        LinkedList<FileStorage> storageList = new LinkedList<>(); // we are not adding SAN
        // devices by now
        
        /*
         * DatacenterCharacteristics contém as informações referentes aos recursos do centro de dados.
         */
        DatacenterCharacteristics characteristics = new DatacenterCharacteristicsSimple(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        DatacenterSimple datacenter = null;
        try {
            /*
             * VmAllocationPolicy representa a política de alocação das máquinas virtuais nas
             * máquinas físicas. A principal funcionalidade desta classe é verificar se uma máquina física
             * tem requisitos suficientes para alocar uma máquina virtual em específico.
             */
            datacenter = new DatacenterSimple(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

       return datacenter;
    }
       
}
