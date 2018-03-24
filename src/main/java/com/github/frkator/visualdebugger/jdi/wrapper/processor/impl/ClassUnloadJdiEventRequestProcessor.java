package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.ClassUnloadConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.ClassUnloadEvent;

public class ClassUnloadJdiEventRequestProcessor extends JdiEventRequestProcessor<ClassUnloadEvent,ClassUnloadConfiguration> {

    public ClassUnloadJdiEventRequestProcessor(ClassUnloadConfiguration configuration,ProcessorContext context) {
        super(configuration,context, JdiEventCategory.CLASS_UNLOAD);
    }

    @Override
    public JdiEventProcessor<ClassUnloadEvent,ClassUnloadConfiguration> newEventProcessor() {
        return new ClassUnloadEventProcessor(configuration,context,jdiEventCategory);
    }

}
