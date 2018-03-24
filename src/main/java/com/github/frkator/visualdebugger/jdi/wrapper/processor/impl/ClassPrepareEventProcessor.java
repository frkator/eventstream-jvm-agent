package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.ClassPrepareConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.SourceCodeSemanticLocationSerializer;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.event.ClassPrepareEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClassPrepareEventProcessor extends JdiEventProcessor<ClassPrepareEvent,ClassPrepareConfiguration> {

    private final Logger logger = LoggerFactory.getLogger(ClassPrepareEventProcessor.class);

    protected ClassPrepareEventProcessor(ClassPrepareConfiguration configuration, ProcessorContext context,JdiEventCategory jdiEventCategory) {
        super(configuration,context,jdiEventCategory);
    }

    @Override
    public void process(ClassPrepareEvent event) {
        ReferenceType refType = event.referenceType();
        logger.debug("loaded class: {}",refType.name());
        context.getJdiEventListener().onClassPrepareEvent(refType,context.getVirtualMachine());

    }

    @Override
    protected String serializeSpecifier(ClassPrepareEvent classPrepareEvent) {
        return SourceCodeSemanticLocationSerializer.serialize(classPrepareEvent.referenceType());
    }





}
