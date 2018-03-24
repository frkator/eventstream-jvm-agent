package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ValueIntrospector;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiDataEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.MethodEntryConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.SourceCodeSemanticLocationSerializer;
import com.sun.jdi.*;
import com.sun.jdi.event.MethodEntryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodEntryEventProcessor extends JdiDataEventProcessor<MethodEntryEvent,MethodEntryConfiguration> {

    protected MethodEntryEventProcessor(MethodEntryConfiguration configuration, ProcessorContext processorContext,JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext,jdiEventCategory);
    }

    @Override
    protected List<ValueIntrospector.CategorizedValue> getEventData(MethodEntryEvent event) {
        final List<ValueIntrospector.CategorizedValue> arguments = new ArrayList<>();
        try {
            List<LocalVariable> localVariables = event.method().arguments();
            for (LocalVariable localVariable : localVariables) {
                Value value = event.thread().frame(0).getValue(localVariable);
                arguments.add(ValueIntrospector.categorize(value));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return arguments;
    }

    @Override
    protected void process(MethodEntryEvent event) {

    }

    @Override
    protected String serializeSpecifier(MethodEntryEvent event) {
        return SourceCodeSemanticLocationSerializer.serialize(event.method());
    }


}
