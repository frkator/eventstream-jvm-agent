package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.ClassPrepareConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.ClassPrepareEvent;

public class ClassPrepareJdiEventRequestProcessor extends JdiEventRequestProcessor<ClassPrepareEvent,ClassPrepareConfiguration> {

    protected ClassPrepareJdiEventRequestProcessor(ClassPrepareConfiguration configuration, ProcessorContext context) {
        super(configuration,context, JdiEventCategory.CLASS_LOAD);
    }

    @Override
    public JdiEventProcessor<ClassPrepareEvent,ClassPrepareConfiguration> newEventProcessor() {
        return new ClassPrepareEventProcessor(configuration,context,jdiEventCategory);
    }

}
