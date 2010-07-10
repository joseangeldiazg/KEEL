//
//  AllKNN.java
//
//  Salvador Garc�a L�pez
//
//  Created by Salvador Garc�a L�pez 18-7-2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package keel.Algorithms.Instance_Selection.AllKNN;

import keel.Algorithms.Preprocess.Basic.*;
import org.core.*;
import java.util.StringTokenizer;



public class AllKNN extends Metodo {

 /*Own parameters of the algorithm*/
  private int k;


  public AllKNN (String ficheroScript) {
    super (ficheroScript);
  }
  
  public void ejecutar () {

    int i, j, l;
    int nClases;
    int claseObt;
    boolean marcas[];
    int nSel = 0;
    double conjS[][];
    double conjR[][];
    int conjN[][];
    boolean conjM[][];
    int clasesS[];

    long tiempo = System.currentTimeMillis();

    /*Inicialization of instance flag vector*/
    marcas = new boolean[datosTrain.length];
    for (i=0; i<datosTrain.length; i++)
      marcas[i] = true;
    nSel = datosTrain.length;

    /*Getting the number of different classes*/
    nClases = 0;
    for (i=0; i<clasesTrain.length; i++)
      if (clasesTrain[i] > nClases)
        nClases = clasesTrain[i];
    nClases++;

    /*Algorithm body. For each instance of T, we search the conformity of mayority nearest
     neighborhood. If it is negative, the instance is erased. The neighborhood has size from 1 to k*/
    for (i=0; i<datosTrain.length; i++) {
      /*Apply KNN to the instance*/
      for (j=1; j<=k && marcas[i]; j++) {
    	claseObt = KNN.evaluacionKNN2(j, datosTrain, realTrain, nominalTrain, nulosTrain, clasesTrain, datosTrain[i], realTrain[i], nominalTrain[i], nulosTrain[i], nClases, distanceEu);
        if (claseObt != clasesTrain[i]) {
          marcas[i] = false;
          nSel--;
        }
      }
    }

    /*Construction of S set from the flagged vector*/
    conjS = new double[nSel][datosTrain[0].length];
    conjR = new double[nSel][datosTrain[0].length];
    conjN = new int[nSel][datosTrain[0].length];
    conjM = new boolean[nSel][datosTrain[0].length];
    clasesS = new int[nSel];
    for (i=0, l=0; i<datosTrain.length; i++) {
      if (marcas[i]) { //the instance must be copied to the solution
        for (j=0; j<datosTrain[0].length; j++) {
          conjS[l][j] = datosTrain[i][j];
          conjR[l][j] = realTrain[i][j];
          conjN[l][j] = nominalTrain[i][j];
          conjM[l][j] = nulosTrain[i][j];
        }
        clasesS[l] = clasesTrain[i];
        l++;
      }
    }

    System.out.println("AllKNN "+ relation + " " + (double)(System.currentTimeMillis()-tiempo)/1000.0 + "s");

    
                // COn conjS me vale.
                 int trainRealClass[][];
                 int trainPrediction[][];
                
                 trainRealClass = new int[conjS.length][1];
		 trainPrediction = new int[conjS.length][1];	
                
                 //Working on training
                 for ( i=0; i<conjS.length; i++) {
                     trainRealClass[i][0] = clasesS[i];
                     trainPrediction[i][0] = KNN.evaluate(conjS[i],datosTrain, nClases, clasesTrain, this.k);
                 }
                 
                 KNN.writeOutput(ficheroSalida[0], trainRealClass, trainPrediction,  entradas, salida, relation);
                 
                 
                //Working on test
		int realClass[][] = new int[datosTest.length][1];
		int prediction[][] = new int[datosTest.length][1];	
		
		//Check  time		
				
		for (i=0; i<realClass.length; i++) {
			realClass[i][0] = clasesTest[i];
			prediction[i][0]= KNN.evaluate(datosTest[i],conjS, nClases, clasesS, this.k);
		}
                
                KNN.writeOutput(ficheroSalida[1], realClass, prediction,  entradas, salida, relation);

    
  }

  public void leerConfiguracion (String ficheroScript) {

    String fichero, linea, token;
    StringTokenizer lineasFichero, tokens;
    byte line[];
    int i, j;

    ficheroSalida = new String[2];

    fichero = Fichero.leeFichero (ficheroScript);
    lineasFichero = new StringTokenizer (fichero,"\n\r");

    lineasFichero.nextToken();
    linea = lineasFichero.nextToken();

    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    token = tokens.nextToken();

    /*Getting the names of the training and test files*/
    line = token.getBytes();
    for (i=0; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroTraining = new String (line,i,j-i);
    for (i=j+1; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroTest = new String (line,i,j-i);

    /*Getting the path and base name of the results files*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    token = tokens.nextToken();

    /*Getting the names of output files*/
    line = token.getBytes();
    for (i=0; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[0] = new String (line,i,j-i);
    for (i=j+1; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[1] = new String (line,i,j-i);
    
    /*Getting the number of neighbors*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    k = Integer.parseInt(tokens.nextToken().substring(1));
  
    /*Getting the type of distance function*/
    distanceEu = true;    
  }
}