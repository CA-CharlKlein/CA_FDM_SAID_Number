/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.SouthAfrica.Masking;

/**
 *
 * @author charlklein
 */
public class SouthAFricanIDTests {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        SouthAfricanIDMask mask = new SouthAfricanIDMask();
        
        //String[] input = new String[4];
        //input[0] = "";       
        //input[1] = "1965-07-18";
        //input[2] = "S";
        //input[3] = "male";
        
        String[] input = new String[4];
        input[0] = "";       
        input[1] = "1987-08-30";
        input[2] = "P";
        input[3] = "male";

        //30889 5044 089
        
        System.out.println(mask.mask(input));
        
        
    }
    
}
