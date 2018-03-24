package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.BreakpointConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.BreakpointEvent;

public class BreakpointJdiEventRequestProcessor extends JdiEventRequestProcessor<BreakpointEvent,BreakpointConfiguration> {

    protected BreakpointJdiEventRequestProcessor(BreakpointConfiguration jdiEventConfiguration,ProcessorContext context) {
        super(jdiEventConfiguration,context, JdiEventCategory.BREAKPOINT);
    }

    @Override
    public BreakpointEventProcessor newEventProcessor() {
        return new BreakpointEventProcessor(configuration,context,jdiEventCategory);
    }
}
