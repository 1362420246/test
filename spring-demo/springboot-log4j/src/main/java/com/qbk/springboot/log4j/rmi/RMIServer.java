package com.qbk.springboot.log4j.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * rmi server
 */
public class RMIServer {
    public static void main(String[] args) {
       try {
           LocateRegistry.createRegistry(1099);
           Registry registry = LocateRegistry.getRegistry();
           System.out.println("Create RMI registry on port 1099");

           Reference reference = new Reference(
                   "com.qbk.springboot.log4j.rmi.Event",
                   "com.qbk.springboot.log4j.rmi.Event",
                   null);
           ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
           registry.bind("event",referenceWrapper);
       }catch ( Exception e){
           e.printStackTrace();
       }
    }
}