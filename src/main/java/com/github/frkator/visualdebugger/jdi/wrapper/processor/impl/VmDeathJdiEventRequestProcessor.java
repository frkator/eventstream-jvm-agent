package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.sun.jdi.event.VMDeathEvent;

public class VmDeathJdiEventRequestProcessor extends JdiEventRequestProcessor<VMDeathEvent, JdiEventConfiguration> {

    protected VmDeathJdiEventRequestProcessor(JdiEventConfiguration configuration, ProcessorContext processorContext, JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext, jdiEventCategory);
    }

    @Override
    public JdiEventProcessor<VMDeathEvent, JdiEventConfiguration> newEventProcessor() {
        return new VmDeathEventProcessor(configuration,context,JdiEventCategory.VM_DEATH);
    }
}
