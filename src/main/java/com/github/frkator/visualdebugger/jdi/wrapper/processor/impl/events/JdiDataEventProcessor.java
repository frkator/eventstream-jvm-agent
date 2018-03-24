package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ValueIntrospector;
import com.sun.jdi.event.Event;

import java.util.List;
import java.util.Map;

public abstract class JdiDataEventProcessor<E extends Event, C extends JdiEventConfiguration> extends JdiEventProcessor<E,C> {

    protected JdiDataEventProcessor(C configuration, ProcessorContext processorContext,JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext,jdiEventCategory);
    }

    protected abstract List<ValueIntrospector.CategorizedValue> getEventData(E event);

    @Override
    protected void notifyClientEventListeners(String actualSpecifier,String userDefinedSpecifier, E event) {
        final List<ValueIntrospector.CategorizedValue> eventData = getEventData((E) event);
        final ClientDataEvent clientDataEvent = new ClientDataEvent(
                                                        jdiEventCategory,
                                                        actualSpecifier,
                                                        userDefinedSpecifier,
                                                        eventData
        );
        clientEventListeners
                .stream()
                .forEach(
                        clientEventListener ->
                                clientEventListener
                                        .onClientDataEvent(
                                                clientDataEvent
                                        )
                );
    }
}
