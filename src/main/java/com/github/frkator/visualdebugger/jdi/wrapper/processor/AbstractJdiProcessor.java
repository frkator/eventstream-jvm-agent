package com.github.frkator.visualdebugger.jdi.wrapper.processor;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;

public abstract class AbstractJdiProcessor<C extends JdiEventConfiguration> {

    public final ProcessorContext context;
    public final C configuration;
    public final JdiEventCategory jdiEventCategory;

    protected AbstractJdiProcessor(C configuration, ProcessorContext context, JdiEventCategory jdiEventCategory) {
        this.context = context;
        this.configuration = configuration;
        this.jdiEventCategory = jdiEventCategory;
    }

}
