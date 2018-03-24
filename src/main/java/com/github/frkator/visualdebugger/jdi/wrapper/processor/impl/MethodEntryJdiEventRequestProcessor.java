package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.MethodEntryConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.MethodEntryRequest;

public class MethodEntryJdiEventRequestProcessor extends JdiEventRequestProcessor<MethodEntryEvent,MethodEntryConfiguration> {

    protected MethodEntryJdiEventRequestProcessor(MethodEntryConfiguration configuration, ProcessorContext processorContext) {
        super(configuration, processorContext, JdiEventCategory.METHOD_ENTRY);
    }

    @Override
    public JdiEventProcessor<MethodEntryEvent, MethodEntryConfiguration> newEventProcessor() {
        return new MethodEntryEventProcessor(configuration,context,jdiEventCategory);
    }
}
