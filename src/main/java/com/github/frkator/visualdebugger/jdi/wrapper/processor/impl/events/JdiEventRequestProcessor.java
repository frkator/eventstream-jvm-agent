package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractJdiProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.Event;

public abstract class JdiEventRequestProcessor<E extends Event, C extends JdiEventConfiguration> extends AbstractJdiProcessor<C> {

    protected JdiEventRequestProcessor(C configuration, ProcessorContext processorContext,JdiEventCategory jdiEventCategory) {
        super(configuration,processorContext, jdiEventCategory);
    }

    public void register() {
        configuration.configure(context);
    }

    public abstract JdiEventProcessor<E,C> newEventProcessor();

    public JdiEventCategory getEventCategory() {
        return jdiEventCategory;
    }

}
