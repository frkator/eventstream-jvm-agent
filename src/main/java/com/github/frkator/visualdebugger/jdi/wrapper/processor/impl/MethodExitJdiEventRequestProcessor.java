package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.MethodExitConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitJdiEventRequestProcessor extends JdiEventRequestProcessor<MethodExitEvent,MethodExitConfiguration> {

    protected MethodExitJdiEventRequestProcessor(MethodExitConfiguration configuration, ProcessorContext processorContext) {
        super(configuration, processorContext, JdiEventCategory.METHOD_EXIT);
    }

    @Override
    public JdiEventProcessor<MethodExitEvent, MethodExitConfiguration> newEventProcessor() {
        return new MethodExitEventProcessor(configuration,context,jdiEventCategory);
    }
}
