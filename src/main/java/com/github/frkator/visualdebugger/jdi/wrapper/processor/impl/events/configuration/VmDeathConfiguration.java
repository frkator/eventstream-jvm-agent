package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.request.VMDeathRequest;

public class VmDeathConfiguration extends JdiEventConfiguration {

    VmDeathConfiguration(){}

    @Override
    public void configure(ProcessorContext context) {
        VMDeathRequest vmDeathRequest = context.getVirtualMachine().eventRequestManager().createVMDeathRequest();
        vmDeathRequest.setEnabled(true);
    }

}
