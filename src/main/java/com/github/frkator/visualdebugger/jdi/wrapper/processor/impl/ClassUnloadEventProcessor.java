package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.ClassUnloadConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.ClassUnloadEvent;

public class ClassUnloadEventProcessor extends JdiEventProcessor<ClassUnloadEvent,ClassUnloadConfiguration> {

    protected ClassUnloadEventProcessor(ClassUnloadConfiguration configuration,ProcessorContext context,JdiEventCategory jdiEventCategory) {
        super(configuration,context,jdiEventCategory);
    }

    @Override
    protected void process(ClassUnloadEvent event) {

    }

    @Override
    protected String serializeSpecifier(ClassUnloadEvent event) {
        return event.className();
    }
}
