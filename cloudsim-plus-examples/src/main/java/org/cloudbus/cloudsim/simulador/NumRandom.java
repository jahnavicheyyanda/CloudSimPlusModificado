/*
 * Classe criada para gerar números aleatorios entre 0 e n 
 */
package org.cloudbus.cloudsim.simulador;

import java.util.Random;

/**
 *
 * @author thejott
 */
public class NumRandom {
    
      public int rdm(int n){
        Random r = new Random();
         return r.nextInt(n);
     }
}
