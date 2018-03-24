package com.github.frkator.test

import com.github.frkator.visualdebugger.jdi.DebugAgent
import com.github.frkator.visualdebugger.jdi.VirtualMachineManagerFacade
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.BreakpointSpecification
import com.sun.jdi.VirtualMachine

class TestableVirtualMachineManagerFacade extends VirtualMachineManagerFacade {

    TestableVirtualMachineManagerFacade(String mainClassFqcn, List<BreakpointSpecification> breakpointSpecifications) {
        super(mainClassFqcn, breakpointSpecifications)
    }

    TestableVirtualMachineManagerFacade(String mainClassFqcn, DebugAgent debugAgent) {
        super(mainClassFqcn, debugAgent)
    }

    VirtualMachine virtualMachine(){
        super.getVirtualMachine().virtualMachine()
    }

    @Override
    void start() throws Exception {
        super.launch()
    }
}
