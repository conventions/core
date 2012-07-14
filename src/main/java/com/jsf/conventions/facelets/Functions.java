/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.facelets;

import java.util.List;

/**
 *
 * @author rmpestano Nov 19, 2011 2:10:42 AM
 */
public class Functions {

    public static String concat(String form, String string1, String string2) {
        System.out.println("return:"+form.concat(":").concat(string1).concat(":").concat(string1).concat(string2));
        return ":"+form.concat(":").concat(string1).concat(string2);
    }
    
    public static Integer size(List list){
        return list != null ? list.size() : 0;
    } 

}
