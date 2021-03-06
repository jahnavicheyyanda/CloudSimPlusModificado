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
public class CL extends NumRandom{
    private List<Cloudlet> list;
    private  UtilizationModel utilizationModel;
    private int userId;
    private int indice;
    
    public CL(int userId){
        list = new LinkedList<>();
        utilizationModel = new UtilizationModelFull();
        this.userId= userId;
        indice =0 ;
    }
    public CL(int userId,int indice){
        list = new LinkedList<>();
        utilizationModel = new UtilizationModelFull();
        this.userId= userId;
        this.indice =indice;
    }
    
    public int getIndice(){
    	return indice;
    }
    
    public void add(long length,int pesNumber,int fileSize,int outputSize){
    Cloudlet cloudlet = new CloudletSimple(indice, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    cloudlet.setUserId(userId);
    list.add(cloudlet);
    indice++;
    }
    public void add(int idvm,long length,int pesNumber,int fileSize,int outputSize){
        Cloudlet cloudlet = new CloudletSimple(indice, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setUserId(userId);
        cloudlet.setVmId(idvm);
        list.add(cloudlet);
        indice++;
        }
    
    public void add(int idvm,long length,int fileSize,int outputSize){
    	int pesNumber =1;
        Cloudlet cloudlet = new CloudletSimple(indice, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setUserId(userId);
        cloudlet.setVmId(idvm);
        list.add(cloudlet);
        indice++;
        }
    
     public void CreateCoudLets(int n){
            long length[]={100,300,500,1000,1500,2000,5000,10000,20000,10000000};
            int pesNumber[]={1,2};
            int fileSizeOutputSize[]={100,200,300,500,1000,2000,5000,10000,20000,50000};
            for(int i=0;i<n;i++)
            add(length[rdm(10)],pesNumber[rdm(2)],fileSizeOutputSize[rdm(10)],fileSizeOutputSize[rdm(10)]);
            
        }
    
     public void getCarga(int n){
    	 CreateCoudLets(rdm(n)+1);
     }
     
    public  List<Cloudlet> getList(){
        return list;
    }
    

}
