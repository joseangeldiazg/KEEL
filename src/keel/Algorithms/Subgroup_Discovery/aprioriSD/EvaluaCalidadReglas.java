/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. Sánchez (luciano@uniovi.es)
    J. Alcalá-Fdez (jalcala@decsai.ugr.es)
    S. García (sglopez@ujaen.es)
    A. Fernández (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

package keel.Algorithms.Subgroup_Discovery.aprioriSD;

/**
 * <p> Get the statistical data from the algorithm. In particular, it evaluates the quality of the rules generated.
 * @author Alberto Fernández Hilario
 * @version 1.1
 * @since JDK1.2
 * </p>
 */
public class EvaluaCalidadReglas {

    private double porcAciertoTr;
    private double porcAciertoTst;
    private ConjReglas reglas;
    private int nClases;
    private int nDatos;
    private int contClases[];
    private int tam;
    private double ant;
    private double cob;
    private double rel;

    private ConjDatos train;
    private ConjDatos test;

    private String[] valorNombreClases;

    /**
     * <p>
     * Calculates the final statisticals for a set of rules and a set of data
     * </p>
     * @param conjreg Set of rules(complex) final
     * @param conjTrn Set of train data
     * @param conjTst Set of test data
     * @param muestPorClaseTrain int[] Number of examples of each class in train set
     * @param muestPorClaseTest int[] Number of examples of each class in train test
     * @param valorNombreClases String[] Labels for each class
     */
    public EvaluaCalidadReglas(ConjReglas conjreg, ConjDatos conjTrn,
                               ConjDatos conjTst, int[] muestPorClaseTrain,
                               int[] muestPorClaseTest, String [] valorNombreClases) {
        reglas = conjreg; //conjreg.copiaConjReglas();
        nDatos = conjTrn.size();
        train = conjTrn.copiaConjDatos();
        test = conjTst.copiaConjDatos();
        this.valorNombreClases =  valorNombreClases;
        if(reglas.size() > 0){
        nClases = reglas.getRegla(0).getDistribucion().length;

        // Calculos en Entrenamiento
        calculaIndices(train, muestPorClaseTrain, 0);
        System.out.print("\n\nIndices en Train: ");
        System.out.print("\n\n Tamaño reglas: " + tam +
                         "\nNº Atributos por regla medio: " + ant +
                         "\nCobertura: " +
                         cob);
//        System.out.print("\n\t Confidence: " + conf + "  ComplMed: " + complmed +
//                         "  Compl: " + compl);
        System.out.print("\nRelevancia: " + rel);
        //System.out.print("\nAcierto: " + porcAciertoTr);

//		Calculos en test
        calculaIndices(test, muestPorClaseTest, 1);
        System.out.print("\n\nIndices en Test:");
        System.out.print("\n\n Tamaño reglas: " + tam +
                         "\nNº Atributos por regla medio: " + ant +
                         "\nCobertura: " +
                         cob);
        /*System.out.print("\n\t Confidence: " + conf +
                         "  Complejidad Media (support): " + complmed +
                         "  Complejidad (support completo): " + compl);
         */
        System.out.println("\nRelevancia: " + rel);
        //System.out.print("\nAcierto: " + porcAciertoTst);
    	}

    }

    /**
     * <p>
     * Prints on a string the statistical(for test)
     * </p>
     * @return the statisticals
     */
    public String printString() {
	    String cad = "#### Final Results (on test) ####\n";	    
	    if (reglas.size() > 0){
	        cad += "Avg. Rule length: " + tam + "\n";
	        cad += "Avg. Number of attributes by rule: " + ant + "\n";
	        cad += "Avg. Coverage; " + cob + "\n";
	        cad += "Avg. Significance; " + rel + "\n";
	        cad += "Accuracy Training: " + porcAciertoTr + "\n"; ;
	        cad += "Accuracy Test: " + porcAciertoTst;
        }else{
	        cad += "No rule found! Please decrease the confidence threshold \n";
	        cad += "Avg. Rule length: 0\n";
	        cad += "Avg. Number of attributes by rule: 0\n";
	        cad += "Avg. Coverage; 0\n";
	        cad += "Avg. Significance; 0\n";
	        cad += "Accuracy Training: 0\n"; ;
	        cad += "Accuracy Test: 0";	        
        }

        return cad;
    }

    /**
     * <p>
     * Calculates all the statistical results, especially percent mathes
     * </p>
     * @param datos Set of data(train or test)
     * @param muestPorClase int[] Number of examples for each class in the data set
     * @param code Train or Test
     */
    private void calculaIndices(ConjDatos datos, int[] muestPorClase, int code) {
        int i, j;
        nDatos = datos.size();

        // contamos el numero de mustras por clase
        contClases = new int[nClases];
        for (i = 0; i < nClases; i++) {
            contClases[i] = muestPorClase[i];
        }

        tam = reglas.size(); // calculamos Tam

        // calculamos nº atributos por regla medio
        for (i = 0, ant = 0; i < reglas.size(); i++) {
            ant += reglas.getRegla(i).size();
        }

        ant = (double) ant / tam; //Nº de atributos por regla medio

        // calculamos la distrib
        double muestCubiertas = 0; //nº ejemplos cubiertos por las reglas
        int[][] instCubiertas = new int[tam][nClases];

        for (i = 0; i < reglas.size(); i++) {
            for (j = 0; j < nClases; j++) {
                instCubiertas[i][j] = 0;
            }
        }
        muestCubiertas = 0;
        for (i = 0; i < reglas.size(); i++) {
            for (j = 0; j < nDatos; j++) {
                Muestra m = datos.getDato(j);
                if (reglas.getRegla(i).cubre(m)) {
                    muestCubiertas++;
                    //System.out.println("\nRegla y ejemplo:");
                    //reglas.getRegla(i).print();
                    //m.print();
                    instCubiertas[i][m.getClase()]++;
                }
            }
        }
        //System.err.println("Muestras cubiertas -> "+muestCubiertas);
        //System.err.println("Total datos -> "+nDatos);
        //cob = (double) muestCubiertas / (nDatos * tam * tam); //COV = 1/nR·SUM[Cov(Ri)] -- Cov(Ri) = n(Condi)/N //
        cob = (double)muestCubiertas / (tam * nDatos);
        //Cobertura -> porcentaje de ejemplos cubiertos por cada regla / nº de reglas

        // Calculamos la relevancia (significance)
        double sigParcial = 0;
        double[] pCondi = new double[reglas.size()]; //Factor normalizador -> coverage
        for (i = 0; i < reglas.size(); i++) {
            pCondi[i] = 0;
            for (j = 0; j < nClases; j++) {
                pCondi[i] += instCubiertas[i][j];
            }
            pCondi[i] *= (double) 1.0 / nDatos;
        }
        rel = 0;
        for (i = 0; i < reglas.size(); i++) {
            sigParcial = 0;
            for (j = 0; j < nClases; j++) {
                double logaritmo = (double) instCubiertas[i][j] /
                                   (contClases[j] * pCondi[i]);
                if ((logaritmo != 0) && (!Double.isNaN(logaritmo)) &&
                    (!Double.isInfinite(logaritmo))) {
                    logaritmo = Math.log(logaritmo);
                    logaritmo *= (double) instCubiertas[i][j];
                    sigParcial += logaritmo;
                }
            }
            rel += sigParcial * 2;
        }
        rel /= (double) reglas.size();

    }


    /**
     * Updates the dataset distribution.
     * @param train given dataset.
     */
    public void ajustaDistribucion(ConjDatos train) {
        int cl;
        for (int i = 0; i < train.size(); i++) { //Para cada ej.
            boolean seguir = true;
            cl = train.getDato(i).getClase();
            for (int j = 0; j < reglas.size() && seguir; j++) { //busco la regla que lo cubre
                if (reglas.getRegla(j).cubre(train.getDato(i))) {
                    reglas.getRegla(j).incremDistribClase(cl);
                }
            }
        }
    }

         /**
     * <p>
     * Generates a string with the out-put lists
     * </p>
     * @param datos Set of data witch to compare the set of rules 
     * @param conj flag to indicates if the set is for training or test (0,1).
     * @return A list of pairs: 'original class;calculated class;'
     */
    public String salida(ConjDatos datos, int conj) {
        String cadena = new String("");
        int voto[] = new int[nClases];
        int clases[] = new int[nClases];
        int distribucion[], max;
        int cl, cl2, clasePorDefecto = 0, aciertos = 0;
        if (reglas.size() > 0){
        for (int i = 0; i < datos.size(); i++) {
            clases[datos.getDato(i).getClase()]++;
        }
        for (int i = 0, clase = -1; i < nClases; i++) {
            if (clases[i] > clase) {
                clasePorDefecto = i;
                clase = clases[i];
            }
        }
        for (int i = 0; i < datos.size(); i++) { // Para el conjunto completo de datos
            cl2 = datos.getDato(i).getClase();
            for (int j = 0; j < nClases; j++) { //Inicializo voto a 0
                voto[j] = 0;
            }
            for (int j = 0; j < reglas.size(); j++) { // vemos que reglas verifican a la muestra
                if (reglas.getRegla(j).cubre(datos.getDato(i))) {
                    distribucion = reglas.getRegla(j).getDistribucion();
                    for (int k = 0; k < nClases; k++) {
                        voto[k] += distribucion[k];
                    }
                }
            }
            max = 0;
            cl = 0;
            for (int j = 0; j < nClases; j++) { //Obtengo la clase que me da mis reglas
                if (voto[j] > max) {
                    max = voto[j];
                    cl = j;
                }
            }
            if (max == 0) { //No se ha activado ninguna regla
                cl = clasePorDefecto;
            }
            cadena += new String(valorNombreClases[cl2] + " " + valorNombreClases[cl] + "\n");
            if (cl == cl2) {
                aciertos++;
            }
        }
        if (conj == 0) {
            porcAciertoTr = (double) aciertos / datos.size();
        } else {
            porcAciertoTst = (double) aciertos / datos.size();
        }
		}else{
			for (int i = 0; i < datos.size(); i++) { // Para el conjunto completo de datos			
				cadena += new String(valorNombreClases[datos.getDato(i).getClase()] + " ?\n");
			}
		}
        return cadena;
    }
    
}

