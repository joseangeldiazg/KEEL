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




package keel.Algorithms.Genetic_Rule_Learning.XCS;
import  keel.Algorithms.Genetic_Rule_Learning.XCS.KeelParser.Config;
import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * <p>It contains a char value, that represents the value of the alelle.
 * @author Written by Albert Orriols (La Salle, Ramón Llull University - Barcelona) 28/03/2004
 * @author Modified by Xavi Solé (La Salle, Ramón Llull University - Barcelona) 03/12/2008
 * @version 1.1
 * @since JDK1.2
 * </p>
 */
public class TernaryRep implements Attribute{
/**
 * <p>
 * It contains a char value, that represents the value of the alelle.
 * </p>
 * <p>
 */
	
	
  ///////////////////////////////////////
  // attributes


/**
 * <p>
 * Represents the value that takes this allele of the classifier. 
 * </p>
 */
    private char pos; 

   ///////////////////////////////////////
   // associations

/**
 * <p>
 * A reference to a ternary mutation operator
 * </p>
 */
    private TernaryMutation ternaryMutation; 


  ///////////////////////////////////////
  // operations





/**
 * <p>
 * It is the default constructor of the class. 
 * value.
 * </p>
 */
    public  TernaryRep() {        
	if (Config.typeOfMutation.toLowerCase().equals("niched")){
		ternaryMutation = new TNichedMutation();
	}
	else{
		ternaryMutation = new TFreeMutation();
	}
	
    } // end TernaryRep        



/**
 * <p>
 * It's the  constructor of the class value from the environmental value.
 * </p>
 * @param env is the environmental value for this attribute.
 */
    public  TernaryRep(double env) {        
	
	//Initialitation of the char value.
	if (Config.rand() < Config.pDontCare)
      	pos = Config.dontCareSymbol;
      else{
      	if (env == -1.)
      		pos = Config.charVector[(int)(Config.rand()*(double)Config.charVector.length)];
      	else
      		pos = (char)env;	
      }
	
	// A new mutate object is declared
	if (Config.typeOfMutation.toLowerCase().equals("niched"))  ternaryMutation = new TNichedMutation();
	else		ternaryMutation = new TFreeMutation();
	
    } // end TernaryRep        





/**
 * <p>
 * It is the constructor of the class. It initializes the char 
 * according to the char value given as a parameter.
 * </p>
 * @param value is the value that takes the allele.
 */
    public  TernaryRep(char value) {        
        pos = value;
        
	if (Config.typeOfMutation.toLowerCase().equals("niched")){
		ternaryMutation = new TNichedMutation();
	}
	else{
		ternaryMutation = new TFreeMutation();
	}
	
    } // end TernaryRep        


/**
 * <p>
 * It is the constructor of the class. It creates a copy
 * of the TernaryRep given as a parameter.
 * </p>
 * @param tr is the ternary representation that has to be cloned. 
 */
    public  TernaryRep(Attribute tr) {        
        pos = ((TernaryRep)tr).pos;
        if (Config.typeOfMutation.toLowerCase().equals("niched")){
		ternaryMutation = new TNichedMutation();
	}
	else{
		ternaryMutation = new TFreeMutation();
	}
    } // end TernaryRep        




/**
 * <p>
 * Sets the value of the allele.
 * </p>
 * <p>
 * @param value contains the value that has to be set. 
 * </p>
 * <p>
 * @param value2 is needed to implement the Attribute interface. In this case is not
 * used.
 * </p>
 */
    public void setAllele(double value, double value2) {        
	if ((char)value == (char) -1. )
		pos = Config.dontCareSymbol;
	else
	       	pos = (char)value;
    } // end setAllele  




/**
 * <p>
 * Sets the value of the allele.
 * </p>
 * <p>
 * @param tr contains the value that has to be set. 
 * </p>
 * 
 */
    public void setAllele(Attribute tr) {        
        pos = ((TernaryRep)tr).pos;
    } // end setAllele  






/**
 * <p>
 * Returns the value of the alelle
 * </p>
 * <p>
 * 
 * @return a double with the value of the allele. 
 * </p>
 */
    public double getAllele() {        
        return (double)pos;
    } // end getAllele        

    /**
     * Returns the attribute of this allele
     * @return  the attribute of this allele
     */
    public Attribute getAttributeAllele(){
	return this;
    }


/**
 * <p>
 * It returns the generality of the allele.
 * </p>
 *
 * <p>
 * @return a double with the generality of this allele. 
 * </p>
 */
    public double getGenerality() {        
        if (pos == Config.dontCareSymbol) return 1.;
        else return 0.;
    } // end getGenerality



/**
 * <p>
 * Changes the allele if it is a don't care symbol and the random number generated is less than Pspecify.
 * </p>
 * <p>
 * @param env is the environment.
 * </p>
 */
    public void makeSpecify (double env){
    		if (pos == Config.dontCareSymbol){
    			if (Config.rand() <= Config.Pspecify ){
    				if ((char)env == (char)-1.)
         				pos = Config.charVector[(int)(Config.rand() * (double)Config.charVector.length)];
    				else
    					pos = (char)env;
    			}
    		}
    }



/**
 * <p>
 * Mutates the character.
 * </p>
 *
 * <p>
 * @param currentState is needed to implement the Attribute interface
 * </p>
 */
    public void mutate(double currentState) {        
	
	pos = ternaryMutation.mutate(pos, (char)currentState);
   	
    } // end mute        



/**
 * <p>
 * Returns true if the allele matches with the environment.
 * </p>
 * <p>
 * @param env is the value of the environment.
 * </p>
 * <p>
 * @return a boolean indicating if the allele matches with the environmental value.
 * </p>
 */
   public boolean match (double env){
   	if (pos == (char)env || pos == Config.dontCareSymbol || (char)env == (char)(-1)) return true;
   	return false;
  } // end match


/**
 * <p>
 * Returns true if the allele is subsumed by the ternary representation given as a parameter.
 * </p>
 * @param tr is the ternary representation of a classifier.
 * @return a boolean indicating if the allele of the classifier is subsumed
 */
   public boolean subsumes(Attribute tr){
   	if (pos == ((TernaryRep)tr).pos || pos == Config.dontCareSymbol) return true;
   	return false;
  } // end subusmes



/**
 * <p>
 * Returns true if the allele is equal to the allele given as a parameter.
 * </p>
 * <p>
 * @param tr is the ternary representation of a classifier.
 * </p>
 * <p>
 * @return a boolean indicating if the two alleles are equal.
 * </p>
 */
   public boolean equals (Attribute tr){
   	return pos == ((TernaryRep)tr).pos;
  } // end equals


/**
 * <p>
 * Returns if the character of the representation
 * is a don't care symbol
 * </p>
 * <p>
 * @return a double:  1 if the character is a don't care simbol
 * and 0 otherwise
 * </p>
 */
    public double isDontCareSymbol(){
   	if (pos == Config.dontCareSymbol) {	
		return 1.;
	}
	return 0.;
    
    } // end isDontCareSymbol



/**
 * <p>
 * Indicates if the classifier is more general than 
 * the classifier passed as a parameter. 
 * </p>
 * <p>
 * @param t is the ternary representation of the more 
 * specific classifier. 
 * </p>
 * <p>
 * @return a boolean indicating if it's more general.
 * </p>
 */
    public boolean isMoreGeneral(Attribute t){
    	if (pos == ((TernaryRep)t).pos || pos == Config.dontCareSymbol) return true;
    	return false;
    }

/**
  * <p>
  * Prints the allele.
  * </p>
  */
    public void print(){
    	System.out.print (pos);	
    }



/**
  * <p>
  * Prints the allele.
  * </p>
  * <p>
  * @param out is the PrintWriter where the allele has to be printed
  * </p>
  */
    public void print(PrintWriter out){
    	out.print (pos);	
    }




// THE NEXT FUNCTIONS ARE NOT IMPLEMENTED

   public double getUpperAllele(){
	return (double)pos;
   }
   
   public double getLowerAllele(){
	return (double)pos;	
   }

   public void verifyInterval(){}

   /**
     *  Prints the classifier representation on the PrintWriter object given.
     * @param fout PrintWriter object given to write on.
     * @param conv covariance matrix
     */
   public void printNotNorm(PrintWriter fout, Vector conv){}
   
   /**
     * Prints the classifier representation not normalized on the PrintWriter object given.
     * @param fout PrintWriter object given to write on.
     * @param lo lower value in the interval.
     */
   public void printNotNorm(PrintWriter fout, int lo){}
   
   /**
     * Prints the classifier representation not normalized on the PrintWriter object given.
     * @param fout PrintWriter object given to write on.
     * @param lo lower value in the interval.
     * @param up upper value in the interval.
     */
   public void printNotNorm(PrintWriter fout, double lo, double up){}
	

   //public void crossAllele(Attribute at1, Attribute at2){}
   
   //public double setAllele(int i, double lowerValue, double UpperValue){return 0.0;}
   
   


} // end TernaryRep




