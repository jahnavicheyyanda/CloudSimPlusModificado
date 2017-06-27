package org.cloudbus.cloudsim.simulador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterSimple;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;

public class Simulador {
	private static List<Cloudlet> cloudletList; //Lista de Cloudlett
	private static List<Cloudlet> cloudletListaux; //Lista de Cloudlett
    private static List<Vm> vmList; //Lista de maquinas virtuais
    private static List<Host> hostlist;
    private static DatacenterBroker broker;
	private static boolean stop = false;
	private static VM v;
	private static int comando;
	private static Scanner entrada;
	private static DC dc;
	private static DatacenterSimple datacenter;
	private static int IndiceVm, IndiceCl;
	private static int GB = 1000;
	private static int TB = 1000000;
	
	
	public static void IniciarSimulacao(){
		new Thread() {
			
			@Override
			public void run() {
				while(FinalizarSimulacao()){
					
					if(cloudletList.size() == 0){//Cloudlet esperando
						//Não
						SolicitarCarga();
					}else{
					comando = 999;	
					entrada = new Scanner (System.in);
					System.out.print("Criar VMs automaticamente? (S/N): ");
						String cm = entrada.nextLine();
						if(cm.equals("S")) comando =2;
						else if(cm.equals("N")) comando =1;
							
					}
				
					if(comando == 1){//Criar vms manuais
						entrada = new Scanner (System.in);
					    System.out.print("Quantidade de VMs: ");
						CriarVm(entrada.nextInt());}
					else if(comando==2)//Criar vms automatica
						CriarVmAuto2();
					
					ProcessarCloudlets();
					DestruirVMs();
					
					System.out.print("Sair? (S/N): ");
					entrada = new Scanner (System.in);
					String cm = entrada.nextLine();
					if(cm.equals("S")) stop = true;	
					
					
				}
		
			}
		}.start();
	}
	public static void ProcessarCloudlets(){
		if(cloudletList.size()!=0 && vmList.size()!=0){
			//datacenter = dc.CreateDatacenter();
			
			broker.submitCloudletList(cloudletList);
			broker.submitVmList(vmList);
			CloudSim.startSimulation2();//Iniciando simulação
			
		}
	}
	
	public static void SolicitarCarga(){
		//datacenter = dc.CreateDatacenter();
		broker = new DatacenterBrokerSimple("Broker");
		
		  int n =300;
		  CL c = new CL(broker.getId(),IndiceCl);
	      //c.add(2,4000, 3,300,300);//especifica para qual VM o cloudlet sera enviado
	      c.getCarga(n);
	      cloudletList = c.getList();
	      Log.printLine(CloudSim.clock()+": Carga de Trabalho com "+cloudletList.size()+" Cloudlets");
	     // Comandos();
	}
	
	public static boolean FinalizarSimulacao(){
		if(stop){
		CloudSim.stopSimulation();
		
		Log.printLine("Simulação encerrada!");
		MostrarResultados();
		CloudSim.finishSimulation();

		CalcularElasticidade();
		}
		return !stop;
	}
	
	public static void Comandos(){
		new Thread() {
			
			@Override
			public void run() {
				while(true){
					Scanner entrada = new Scanner (System.in);
					System.out.print("-> ");
					String cm = entrada.nextLine();
					if(cm.equals("sair")){
						stop = true;
						break;
					}else
						if(cm.equals("Criar vm")){
							comando = 1;
						break;
						}
					
					if(comando ==999)
						break;
					
				}
			}
		}.start();
	}
	
	
	public static void CriarVm(int num){
		v= new VM(broker.getId(),IndiceVm);
		comando = 0;
		
		for(int i=0;i<num;i++){
			Scanner entrada = new Scanner (System.in);
			Log.printLine("\n\nMemoria(GB): ");
			int memoria = entrada.nextInt()*GB;
			entrada = new Scanner (System.in);
			Log.printLine("Armazenamento(MB): ");
			long length = entrada.nextInt();
			entrada = new Scanner (System.in);
		for(Host h:hostlist){
			Log.printLine(h.getId()+" - "+h.getNomeHost());
		}
			System.out.print("Digite o numero correspondente ao host: ");
			int idhost = entrada.nextInt();
			Host h = hostlist.get(idhost);
			Vm vm =v.add(h, memoria,length);
			Log.printLine(CloudSim.clock()+": Host selecionado "+h.getNomeHost()+"\n\n");
		}
		vmList = v.getList();
		//Comandos();
	}
	
/*
 * Criando vms automaticamente tecnica de replicação de vms / memoria 1GB e Disco 50GB total de Vms 56Vms
 */
	public static void CriarVmAuto(){
		comando =0;
		v= new VM(broker.getId(),IndiceVm);
		
		int alternar = 0;
		
		for(int i=0;i<cloudletList.size() ;i++){
			
			Host h = hostlist.get(alternar);
			v.add(h, 1*GB,50*GB);
			
			alternar++;
			if(alternar==3)
				alternar =0;
			
			if(i>47 && alternar ==1)
					alternar =2;
			
			if(i==55)
				break;
			 
		}
		vmList = v.getList();
	}
	
	//---------------------------------------------------------------------------------------------------------
	/*
	 * Criando vms automaticamente tecnica de replicação de vms / memoria 1GB e Disco 20GB total de Vms 96Vms
	 */
		public static void CriarVmAuto2(){
			comando =0;
			v= new VM(broker.getId(),IndiceVm);
			
			int alternar = 0;
			
			for(int i=0;i<cloudletList.size() ;i++){
				
				Host h = hostlist.get(alternar);
				v.add(h, 1*GB,20*GB);
				
				
				if(i<80){
				alternar++;
				if(alternar==3)
					alternar = 0;
				}else
					alternar = 2;
				
				if(i>47 && alternar ==1)
						alternar =2;
				
				
				
				if(i==95)
					break;
				 
			}
			vmList = v.getList();
		}
	//---------------------------------------------------------------------------------------------------------
	
	public static void DestruirVMs(){
		if(vmList.size()!=0){
		//for(Vm v:vmList)
		//Log.printLine(CloudSim.clock()+": Destruindo VM #"+v.getId());
		//	Log.printLine("\n\n"+broker.getCloudletsWaitingList()+"\n\n");	
		cloudletListaux.addAll(broker.getCloudletsFinishedList());	
		IndiceVm = vmList.get(vmList.size()-1).getId()+1;	
		vmList.clear();
		
		
		IndiceCl = cloudletList.get(cloudletList.size()-1).getId()+1;
		
		cloudletList.clear();

		//System.out.println("ID: "+datacenter.getId());
		//CloudSim.setDatacenterIdsList(datacenter.getId());
		//CloudSim.stopSimulation();
	}
			
	}
	
	public static void CalcularElasticidade(){
		
	}
	
	public static void MostrarResultados(){
		CloudletsTableBuilderHelper.print(new TextTableBuilder(), cloudletListaux);
	}
	
	/**
	 * Método responsavel pela criação dos Hosts
	 * @param dc possui as configurações do Datacenter
	 */
	public static void Hosts(DC dc){
		
        hostlist.add(dc.CreateHost("Angelim", 32*GB, 1*TB));
        hostlist.add(dc.CreateHost("Mangabeira", 16*GB, 1*TB));
        hostlist.add(dc.CreateHost("Jequitiba", 48*GB, 1*TB));

        for(Host h:hostlist)
        Log.printLine(CloudSim.clock()+": Host "+h.getNomeHost()+" iniciando...");
	}
	
	
	
	public static void main(String[] args) {
		
		IndiceCl = 0;
		IndiceVm = 0;
        comando = 0;
        hostlist = new ArrayList<Host>();
        vmList = new ArrayList<Vm>();
        cloudletList = new ArrayList<Cloudlet>();
        cloudletListaux = new ArrayList<Cloudlet>();
        CloudSim.init();
   
          //Criando dataCenter
          dc= new DC("ELAN");
          Log.printLine(CloudSim.clock()+":Datacenter "+dc.getNome()+" Ativo");
          Hosts(dc);
          datacenter = dc.CreateDatacenter();
  		  //broker =  new DatacenterBrokerSimple("Broker");
			
         
           
//        CL cl = new CL(broker.getId());
        
        IniciarSimulacao();
        //Comandos();
        
        
        
	}
}
