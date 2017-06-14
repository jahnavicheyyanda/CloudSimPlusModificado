/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;


import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;

/**
 * Classe Simulador de Nuvem
 * @author Anselmo
 */
public class SimuladorDeNuvem {

    private static List<Cloudlet> cloudletList; //Lista de Cloudlett
    private static List<Vm> vmList; //Lista de maquinas virtuais
    private static VM v;
    private static CL cl;
    private static Host Murici;
    private static Host Cajueiro;
    private static Host Amendoeira;
    private static Host Angelim;
    private static Host Mangabeira;
    private static Host Pitombeira;
    private static Host Jequitiba;
    private static Host Sapucaia;
    private static Host Cedro;
    private static DatacenterBroker broker;
    private static DatacenterBroker broker2 ;
    
     public static void main(String[] args) {
    	
        Log.printFormattedLine("Iniciando simulação!");
        //int Nuser = 1; // N° usuarios da nuvem
        //Calendar calendar = Calendar.getInstance(); //Pegando data e hora atual
        //boolean trace_flag = false; // trace events
        //CloudSim.init(Nuser, calendar);//Inicializando Bibliotecas do CloudSim 
        CloudSim.init();
        
        //****************************************************************************
        int GB = 1000;
        int TB = 1000000;
        //Criando dataCenter
        DC dc= new DC("ELAN");
        
        
       //          CreateHost(  nome,   ram, storage);
       Murici = dc.CreateHost("Murici", 3*GB, 500*GB);
       Cajueiro = dc.CreateHost("Cajueiro", 16*GB, 1*TB);
       Amendoeira = dc.CreateHost("Amendoeira", 8*GB, 1*TB);
       Angelim = dc.CreateHost("Angelim", 32*GB, 1*TB);
       Mangabeira = dc.CreateHost("Mangabeira", 16*GB, 1*TB);
       Pitombeira = dc.CreateHost("Pitombeira", 8*GB, 4*TB);
       Jequitiba = dc.CreateHost("Jequitiba", 48*GB, 1*TB);
       Sapucaia = dc.CreateHost("Sapucaia", 8*GB, 500*GB);
       Cedro = dc.CreateHost("Cedro", 8*GB, 1*TB);
       dc.CreateDatacenter();
       
        //****************************************************************************
       
        /*
        * Criando Broker
        * DatacenterBroker modela o agente que age como o mediador entre o usuário e o
        * provedor da nuvem, é através dele que são enviados os Cloudlets para serem executados
        * e também a alocação das máquinas virtuais.
        */
        
        broker =  new DatacenterBrokerSimple("Broker");
        int brokerId = broker.getId(); // Para o primeiro Broker criado o id = 3
        //****************************************************************************
        /*
         * VM (máquina virtual), que é gerenciada e alocada em uma
         * máquina física (Host)
         */
        v = new VM(brokerId);
        //v.CreateVMs(50);
        //v.add(5000, 250,1);
        v.add(Murici,2*GB,500,2);
        v.add(Murici,2*GB,100,1);
        
        vmList = v.getList();
        broker.submitVmList(vmList);
        //--------------------------------------------------------------------------------

        //*****************************************************************************
        
        /*
         * A classe CL (Cloudlet) modela as aplicações que irão ser executadas na nuvem. Possui
         * atributos necessários para a sua execução como tamanho e quantidade de núcleos
         * necessários.
         */
        
              cl = new CL(brokerId);
              //cl.CreateCoudLets(10);
//            cl.add(4000, 1,300,10);
//              cl.add(0,4000, 10,300,300);//especifica para qual VM o cloudlet sera enviado
//              cl.add(1,4000, 3,300,300);//especifica para qual VM o cloudlet sera enviado
//              
//              cloudletList = cl.getList();
//              broker.submitCloudletList(cloudletList);
              CargaDeTrabalho carga1 = new CargaDeTrabalho(100);
              carga1.gerar(broker);
          
      
//------------------------------------------------------- 
//Responsavel por pausar o sistema e retorna no momento "m" da simulação
//Se m = 0 sistema não é pausado      
        long m = 0;
        ThreadMonitor monitor = new ThreadMonitor(m);
        monitor.start();
//-------------------------------------------------------
        CloudSim.startSimulation();//Iniciando simulação
//      NW nw=new NW();
//      nw.ActiveNetWorkTopology(datacenter, broker);
        
        CloudSim.runStop();

        //vmList = broker.getVmsCreatedList();
        printRes();
        
        CloudSim.finishSimulation();
        
     }
     
     /**
      * Método responsável por criar vms e/ou cloudlets depois que o sistema voltar de uma pausa
      */
     public static void SistemaPosPausado(){
    	 broker2 =  new DatacenterBrokerSimple("Broker2");
    	 
         //Create VMs and Cloudlets and send them to broker
         //creating 5 vms
         VM cvm = new VM(broker2.getId(),v.getIndice());
         cvm.add(Pitombeira,1000, 1000, 1);
         List<Vm> vmlist =cvm.getList();
         broker2.submitVmList(vmlist);
         
        
         CL c = new CL(broker2.getId(),cl.getIndice());
         c.add(2,4000, 3,300,300);//especifica para qual VM o cloudlet sera enviado
         List<Cloudlet> cloudletList = c.getList();
         broker2.submitCloudletList(cloudletList);
     }
     
     /**
      * Método responsável por imprimir o resultado
      */
     public static void printRes(){
    	 List<Cloudlet> newList;
    	 newList = broker.getCloudletsFinishedList();
    	 if(broker2 !=null)
    	 newList.addAll(broker2.getCloudletsFinishedList());
         CloudletsTableBuilderHelper.print(new TextTableBuilder(), newList);
     }

}


class ThreadMonitor extends Thread {
    private long PauseClock =0;
    
    public ThreadMonitor(long PauseClock) {
		this.PauseClock = PauseClock;
	}
    
    @Override
    public void run() {
        CloudSim.pauseSimulation(PauseClock);

        while (true) {
            if (CloudSim.isPaused()) break;
           
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        SimuladorDeNuvem.SistemaPosPausado();        
        
        Log.printLine(CloudSim.clock() + ": Continuando simulação...");
        CloudSim.resumeSimulation();
    }


};