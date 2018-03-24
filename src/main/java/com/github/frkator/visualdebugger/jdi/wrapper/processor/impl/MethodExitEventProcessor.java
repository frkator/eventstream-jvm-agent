package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.MethodExitConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.SourceCodeSemanticLocationSerializer;
import com.sun.jdi.event.MethodExitEvent;

public class MethodExitEventProcessor extends JdiEventProcessor<MethodExitEvent,MethodExitConfiguration> {

    protected MethodExitEventProcessor(MethodExitConfiguration configuration, ProcessorContext processorContext,JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext,jdiEventCategory);
    }

    @Override
    protected void process(MethodExitEvent event) {

    }

    @Override
    protected String serializeSpecifier(MethodExitEvent event) {
        return SourceCodeSemanticLocationSerializer.serialize(event.method());
    }
}
