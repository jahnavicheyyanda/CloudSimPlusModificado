/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.DatacenterCharacteristicsSimple;
import org.cloudbus.cloudsim.DatacenterSimple;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostSimple;
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
public class DC {
    private List<Host> hostList;
    private List<Pe> peList;
    private int hostId;
    private long bw;
    private String name;
    
    
    public DC(String name){
        hostList = new ArrayList<>();
        peList = new ArrayList<>();
        hostId = 0;
        bw = 10000;
        this.name=name;
    }
    
    public void CreateHost(int mips,int ram,long storage){
        peList.add(new PeSimple(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        /*
         * Os Hosts Possuem informações sobre a quantidade de
         * processamento, armazenamento, largura de banda e quantidade de memória disponível.
         * Responsável por compartilhar recursos entre as máquinas virtuais.
         */
        
        /*
         * VmScheduler implementa as políticas de distribuição de processamento da
         * máquina física entre as máquinas virtuais alocadas nesta máquina física.
         */
        
        //Lista de maquinas Físicas (Host)
        hostList.add(new HostSimple(hostId,new ResourceProvisionerSimple<>(new Ram(ram)),new ResourceProvisionerSimple<>(new Bandwidth(bw)), storage, peList,new VmSchedulerTimeShared(peList)) ); // This is our machine
        hostId++;
    
    }
    
      public DatacenterSimple CreateAndGetDatacenter() {

        

        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
        // resource
        double costPerBw = 0.0; // the cost of using bw in this resource
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
