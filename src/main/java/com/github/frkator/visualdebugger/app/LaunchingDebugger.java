package com.github.frkator.visualdebugger.app;


import com.github.frkator.dfd.examples.Debugee;
import com.github.frkator.visualdebugger.jdi.VirtualMachineManagerFacade;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.BreakpointSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Arrays;

public class LaunchingDebugger {

    private final static Logger LOG = LoggerFactory.getLogger(LaunchingDebugger.class);

    public static void main(String[] args) throws Exception {


        VirtualMachineManagerFacade virtualMachineManagerFacade
                = new VirtualMachineManagerFacade(
                        Debugee.class.getCanonicalName(),
                        Arrays.<BreakpointSpecification>asList(
                            new BreakpointSpecification(52,Debugee.class.getCanonicalName()),
                            new BreakpointSpecification(54,Debugee.class.getCanonicalName())
                        )

        );
        //LOG.entering();
        LOG.info("starting");
        virtualMachineManagerFacade.start();

    }





}
