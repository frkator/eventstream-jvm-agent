package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.sun.jdi.event.VMStartEvent;

public class VmStartEventProcessor extends JdiEventProcessor<VMStartEvent,JdiEventConfiguration> {
    protected VmStartEventProcessor(JdiEventConfiguration configuration, ProcessorContext processorContext, JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext, JdiEventCategory.VM_CONNECT);
    }

    @Override
    protected void process(VMStartEvent event) {

    }

    @Override
    protected String serializeSpecifier(VMStartEvent event) {
        return JdiEventCategory.VM_CONNECT.name();
    }
}
