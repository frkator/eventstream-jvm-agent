/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.frkator.dfd.examples;

import com.github.frkator.dfd.other.DifferentPackageDepClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * cd /home/linski/NetBeansProjects/dfd; 
 * 
 * JAVA_HOME=/usr/lib/jvm/jdk1.8.0_66 
 * /usr/local/netbeans-8.2/java/maven/bin/mvn "-Dexec.args=-Xdebug -Xrunjdwp:transport=dt_socket,server=n,address=9999,suspend=y -classpath %classpath com.cartrawler.dfd.examples.Debugee" -Dexec.executable=/usr/lib/jvm/jdk1.8.0_66/bin/java -Dexec.classpathScope=runtime -Djpda.listen=true -Djpda.address=9999 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec
 * @author linski
 */
public class Debugee {

    protected DependecyClass dependecyClass = new DependecyClass();
    private DifferentPackageDepClass differentPackageDepClass = new DifferentPackageDepClass();
    
    private final Double field;
    
    public Debugee() {
        field = 42d;
    }
    
    protected final static Long staticFinalField = 2l;

    protected static String staticField;

    static {
        staticField = "1";
    }
    
    public static double calculate(int argVar) {
     int intVar = 1;
        String strVar = "7";
        List<String> listOfStrings = new ArrayList<String>();        
        Map<Integer,String> map = new LinkedHashMap<Integer, String>();
        
        listOfStrings.add(String.valueOf(1));
        listOfStrings.add(String.valueOf(2));
        
        map.put(1, strVar);
        map.put(2, String.valueOf(2));
        map.put(3, listOfStrings.get(0));
        
        double intermediatePureResult = Double.valueOf(intVar + listOfStrings.get(0));
                
        double intermediateNonPureResult = intermediatePureResult + new Debugee().field + Double.valueOf(staticField);

        intermediateNonPureResult += new Debugee().dependecyClass.hashCode() + new Debugee().differentPackageDepClass.hashCode() + map.size();

        return intermediateNonPureResult + staticFinalField + argVar;
    }
    
    public static void main(String[] args) {
       System.out.println(calculate(2));
    }
    
}
