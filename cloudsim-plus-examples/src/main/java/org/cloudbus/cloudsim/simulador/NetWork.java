/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.network.NetworkTopology;

/**
 *
 * @author thejott
 */
public class NetWork {
    
    public  void ActiveNetWorkTopology(Datacenter datacenter, DatacenterBroker broker){
        //==========================================================
        //                  Configuração da REDE
        //==========================================================
        //NetworkTopology contém a informação para o comportamento da rede na simulaçã
        //Carrega o arquivo de topologia de rede
        
                    NetworkTopology.buildNetworkTopology("topology.brite");

                    // Mapeia entidades CloudSim para Brite entidades
                    
                    // O Datacenter corresponderá ao BRITE nó 0
                    int briteNode=0;
                    NetworkTopology.mapNode(datacenter.getId(),briteNode);

                    // O Broker corresponderá ao BRITE nó 3
                    briteNode=3;
                    NetworkTopology.mapNode(broker.getId(),briteNode);
                    
        //==========================================================
     }
}
