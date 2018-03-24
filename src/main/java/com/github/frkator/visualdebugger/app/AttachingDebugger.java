/*
https://mvmn.wordpress.com/2012/10/20/debugging-remove-jvm-from-groovy-shell/
http://blog.takipi.com/how-to-write-your-own-java-scala-debugger/
 */
package com.github.frkator.visualdebugger.app;

import com.github.frkator.visualdebugger.jdi.wrapper.Common;
import com.sun.jdi.*;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author linski
 */
public class AttachingDebugger {

    public static void main(String[] args) throws Exception {
        VirtualMachine virtualMachine = attachToVirtualMachineViaDtSocket("localhost","9999");




        ThreadReference mainThreadReference = Common.getThreadReference("main",virtualMachine);

        //List<ReferenceType> debugeeRefernceTypes = virtualMachine.classesByName("com.github.frkator.dfd.examples.Debugee");
        //virtualMachine.eventRequestManager().createBreakpointRequest(Location)



        if (mainThreadReference.isSuspended() || mainThreadReference.isAtBreakpoint()) {
            mainThreadReference.resume();
        }
        //Common.registerClassPrepareRequest(virtualMachine,"com.github.frkator.dfd.examples.Debugee");
        //Common.eventLoop(virtualMachine);

/*
        AtomicInteger counter = new AtomicInteger(1);
        mainThreadReference
                .frames()
                .stream()
                .forEach( frame ->
                        System.out.printf("%d %s %n",counter.getAndIncrement(), frame )
                );
                */
    }




    private static VirtualMachine attachToVirtualMachineViaDtSocket(String host,String port) throws IllegalConnectorArgumentsException, IOException {
        AttachingConnector dtSocketAttachingConnector
                = Bootstrap
                .virtualMachineManager()
                .attachingConnectors()
                .stream()
                .filter(it -> "dt_socket".equalsIgnoreCase(it.transport().name()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.size() < 1) {
                                throw new IllegalStateException(
                                        "debugee JVM not ran in debug mode " //todo
                                );
                            }
                            else if (list.size() < 1) {
                                throw new IllegalStateException(
                                        "debugee JVM multiple attaching connectors not allowed" //todo
                                );
                            }
                            return list.get(0);
                        }
                ));


        Connector.Argument hostnameArg= dtSocketAttachingConnector.defaultArguments().get("hostname");
        Connector.Argument portArg= dtSocketAttachingConnector.defaultArguments().get("port");
        Connector.Argument timeoutArg= dtSocketAttachingConnector.defaultArguments().get("timeout");

        hostnameArg.setValue(host);
        portArg.setValue(port);

        VirtualMachine virtualMachine = dtSocketAttachingConnector.attach(
                new LinkedHashMap<String, Connector.Argument>(){{
                    put("hostname",hostnameArg);
                    put("port",portArg);
                    put("timeout",timeoutArg);
                }}

        );
        return virtualMachine;
    }

}
