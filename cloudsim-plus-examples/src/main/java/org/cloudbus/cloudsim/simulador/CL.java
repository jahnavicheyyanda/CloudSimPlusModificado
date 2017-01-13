/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;

import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;

/**
 *
 * @author anselmo
 */
public class CL {
    private List<Cloudlet> list;
    private  UtilizationModel utilizationModel;
    private  long fileSize;
    private  long outputSize;
    private int userId;
    private int indice;
    
    public CL(int userId){
        list = new LinkedList<>();
        utilizationModel = new UtilizationModelFull();
        fileSize = 300;
        outputSize = 300;
        this.userId= userId;
        indice =0 ;
    }
    
    public void add(long length,int pesNumber){
    Cloudlet cloudlet = new CloudletSimple(indice, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    cloudlet.setUserId(userId);
    list.add(cloudlet);
    indice++;
    }
    
    public  List<Cloudlet> getList(){
        return list;
    }
    

}
