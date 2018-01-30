/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.SouthAfrica.Masking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import com.grid_tools.products.datamasker.IMaskFunction;

public class SouthAfricanIDMask implements IMaskFunction {

    /*
    * A South African ID number is a 13-digit number which is defined by the following format: YYMMDDSSSSCAZ.
    * 
    * The first 6 digits (YYMMDD) are based on your date of birth. 20 February 1992 is displayed as 920220.
    *
    * The next 4 digits (SSSS) are used to define your gender.  
    * Females are assigned numbers in the range 0000-4999 and males from 5000-9999.
    * 
    * The next digit (C) shows if you're an SA citizen status with 0 denoting that you were born a SA citizen
    * and 1 denoting that you're a permanent resident.
    *
    * The next digit (A) was used until the late 1980 to Indicate Race, Old ID numbers have been re-issued to correct this.
    * Defaulted to 8.
    *
    * The last digit (Z) is a checksum digit – used to check that the number sequence is 
    * accurate using a set formula called the Luhn algorithm.
     */

 /*
    * 
    * 
    * 
     */
    @Override
    public Object mask(Object... args) {
        StringBuilder maskedValue = new StringBuilder();
        Calendar calendar = Calendar.getInstance();

        // ARGS - 0 - orginal Value
        // ARGS - 1 - Date Of Birth
        // ARGS - 2 - Nationality
        // ARGS - 3 - Gender
        // Assuming original value is a String
        // Parts of original value may be a driving factor in obtaining the masked value
        String originalValue = (String) args[0];

        // Assume date of birth format as yy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Date of Birth comes from parm 1
        Date dob = null;

        try {
            dob = sdf.parse((String) args[1]);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        calendar.setTime(dob);

        Integer dd = calendar.get(Calendar.DATE);
        Integer mm = calendar.get(Calendar.MONTH) + 1;
        Integer yyyy = calendar.get(Calendar.YEAR);

        String MM = ("00" + mm.toString());

        MM = MM.substring(MM.length() - 2);
        String yy = yyyy.toString().substring(2);

        // Form the DDMMYY part of the ID.  Assume that string manipulations are done.
        maskedValue.append(yy).append(MM).append(dd);

        // Gender comes from parm 3 Allowed Options, Male | Female | M | F 
        String gender = (String) args[3];
        String SSSS = getSSSS(gender);
        maskedValue.append(SSSS);

        // Citizenship comes from parm 2 Allowed Options, SA Citizen | Permanent Resident | S | P 
        String nat = getCitizenshipDigit((String) args[2]);
        maskedValue.append(nat);

        Integer checksum = generateCheckDigit(maskedValue.toString());
        maskedValue.append(checksum);

        // System.out.println("Is ID Valid>>>>>" + verifyIDNumber(maskedValue.toString()));
        return maskedValue.toString();
    }

    /*
    * 
    * 
    * 
     */
    private String getCitizenshipDigit(String nationality) {
        String outString = "00";

        //Permanent Resident
        if ("P".equalsIgnoreCase(nationality.substring(0, 1))) {
            outString = "18";

            // SA Citizen
        } else if ("S".equalsIgnoreCase(nationality.substring(0, 1))) {
            outString = "08";
        }

        return outString;
    }

    /*
    * 
    * 
    * 
     */
    private static String getSSSS(String gender) {
        // Assume proprietary code to generate SSSS based on gender
        Integer out = 0;
        String outString = "0000";

        if ("M".equalsIgnoreCase(gender.substring(0, 1))) {
            out = ThreadLocalRandom.current().nextInt(5000, 9999);

        } else if ("F".equalsIgnoreCase(gender.substring(0, 1))) {
            out = ThreadLocalRandom.current().nextInt(0000, 4999);
        }

        outString = outString.concat(Integer.toString(out));

        return outString.substring(outString.length() - 4);
    }

    /*
    * 
    * 
    * 
     */
    private static Boolean verifyIDNumber(String identities) {
        char[] idchars = identities.toCharArray();
        int sum = 0;
        
        // loop over each digit, except the check-digit
        for (int i = 0; i < idchars.length - 1; i++) {
            int digit = Character.getNumericValue(idchars[i]);
            if ((i % 2) == 0) {
                sum += digit;
            } else {
                sum += digit < 5 ? digit * 2 : digit * 2 - 9;
            }
        }
        
        int checkdigit = Character.getNumericValue(idchars[idchars.length - 1]);
        int compdigit = (sum * 9) % 10;

        return checkdigit == compdigit;
    }

    /*
    * 
    * 
    * 
     */
    private static Integer generateCheckDigit(String identities) {
        char[] chars = identities.toCharArray();
        int sum = 0;
        
        // loop over each digit
        for (int i = 0; i < chars.length; i++) {
            int digit = Character.getNumericValue(chars[i]);
            if ((i % 2) == 0) {
                sum += digit;
            } else {
                sum += digit < 5 ? digit * 2 : digit * 2 - 9;
            }
        }
        int compdigit = (sum * 9) % 10;

        return compdigit;
    }
}
