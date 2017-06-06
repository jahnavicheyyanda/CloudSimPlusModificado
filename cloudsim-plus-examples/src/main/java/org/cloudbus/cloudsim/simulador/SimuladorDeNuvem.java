/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;


import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.CloudSimExample7;
import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;

/**
 * Classe Simulador de Nuvem
 * @author Anselmo
 */
public class SimuladorDeNuvem {

    private static List<Cloudlet> cloudletList; //Lista de Cloudlett
    private static List<Vm> vmList; //Lista de maquinas virtuais
  
    
    public static void main(String[] args) {
    	
        Log.printFormattedLine("Iniciando simulação!");
        //int Nuser = 1; // N° usuarios da nuvem
        //Calendar calendar = Calendar.getInstance(); //Pegando data e hora atual
        //boolean trace_flag = false; // trace events
        //CloudSim.init(Nuser, calendar);//Inicializando Bibliotecas do CloudSim 
        CloudSim.init();
        
        //****************************************************************************
        //Criando dataCenter
        DC dc= new DC("ELAN");
        
        //dc1.CreateHosts(4);
         //CreateHost(mips, ram, storage);
        dc.CreateHost(1000, 2048, 1000000);
        dc.CreateDatacenter();
        
        //****************************************************************************
       
        /*
        * Criando Broker
        * DatacenterBroker modela o agente que age como o mediador entre o usuário e o
        * provedor da nuvem, é através dele que são enviados os Cloudlets para serem executados
        * e também a alocação das máquinas virtuais.
        */
        
        DatacenterBroker broker =  new DatacenterBrokerSimple("Broker");
        int brokerId = broker.getId(); // Para o primeiro Broker criado o id = 3
        //----------------------------------------------------------------------------
        //****************************************************************************
        /*
         * VM (máquina virtual), que é gerenciada e alocada em uma
         * máquina física (Host)
         */
        VM v = new VM(brokerId);
        //v.CreateVMs(5);
        v.add(512,250,1);
        vmList = v.getList();
        
        //----------------------------------------------------------------------------
        
        //*****************************************************************************
        
        /*
         * A classe CL (Cloudlet) modela as aplicações que irão ser executadas na nuvem. Possui
         * atributos necessários para a sua execução como tamanho e quantidade de núcleos
         * necessários.
         */
        
            CL cl = new CL(brokerId);
            cl.CreateCoudLets(10);
//          cl.add(4000, 1,300,10);
            cloudletList = cl.getList();
        
        //----------------------------------------------------------------------------
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);
//        try{
//        ThreadMonitor monitor = new ThreadMonitor();
//            monitor.start();
//            Thread.sleep(1000);
//        }catch(Exception e){}
//      NW nw=new NW();
//      nw.ActiveNetWorkTopology(datacenter, broker);
        CloudSim.startSimulation();//Iniciando simulação

     //  CloudSim.finishSimulation();
     //  CloudSim.runStop();
        Log.printFormattedLine("Simulação finalizado!");
        
        List<Cloudlet> newList = broker.getCloudletsFinishedList();
        CloudletsTableBuilderHelper.print(new TextTableBuilder(), newList);

        
        
    }
}


class ThreadMonitor extends Thread {
    /**
     * The DatacenterBroker created inside the thread.
     */
    private DatacenterBroker broker = null;

    @Override
    public void run() {
        CloudSim.pauseSimulation(10);

        while (true) {
            if (CloudSim.isPaused()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.printLine("\n\n\n" + CloudSim.clock() + ": The simulation is paused for 5 sec \n\n");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CloudSim.resumeSimulation();
    }

    public DatacenterBroker getBroker() {
        return broker;
    }
};