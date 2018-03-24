package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.sun.jdi.event.VMDisconnectEvent;

public class VmDisconnectEventProcessor extends JdiEventProcessor<VMDisconnectEvent,JdiEventConfiguration> {

    protected VmDisconnectEventProcessor(JdiEventConfiguration configuration, ProcessorContext processorContext, JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext, JdiEventCategory.VM_DISCONNECT);
    }

    @Override
    protected void process(VMDisconnectEvent event) {

    }

    @Override
    protected String serializeSpecifier(VMDisconnectEvent event) {
        return JdiEventCategory.VM_DISCONNECT.name();
    }
}
