package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.sun.jdi.event.VMDeathEvent;

public class VmDeathEventProcessor extends JdiEventProcessor<VMDeathEvent,JdiEventConfiguration>{

    protected VmDeathEventProcessor(JdiEventConfiguration configuration, ProcessorContext processorContext, JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext, JdiEventCategory.VM_DEATH);
    }

    @Override
    protected void process(VMDeathEvent event) {

    }

    @Override
    protected String serializeSpecifier(VMDeathEvent event) {
        return jdiEventCategory.name();
    }
}
