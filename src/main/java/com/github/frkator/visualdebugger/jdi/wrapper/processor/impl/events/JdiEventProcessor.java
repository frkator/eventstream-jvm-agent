package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.ClientEventListener;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractJdiProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

import java.util.List;

public abstract class JdiEventProcessor<E extends Event,C extends JdiEventConfiguration> extends AbstractJdiProcessor<C> {

    protected List<ClientEventListener> clientEventListeners;// todo concurrent analysis if addign remove

    protected JdiEventProcessor(C configuration, ProcessorContext processorContext,JdiEventCategory jdiEventCategory) {
        super(configuration, processorContext, jdiEventCategory);
    }

    public void setClientEventListeners(List<ClientEventListener> clientEventListeners) {
        this.clientEventListeners = clientEventListeners;
    }

    protected abstract void process(E event);

    protected abstract String serializeSpecifier(E event);

    protected String getUserDefinedSpecifier(EventRequest request) {
        Object uds = request.getProperty(JdiEventConfiguration.class.getSimpleName());
        return uds == null ? "singleton event" : (String)uds;
    }

    public void processEvent(Event event) {
        String actualSpecifier = null;
        String userDefinedSpecifier = null;
        try {
            actualSpecifier = serializeSpecifier((E)event);
            userDefinedSpecifier = getUserDefinedSpecifier(event.request());
            process((E)event);
            notifyClientEventListeners(actualSpecifier,userDefinedSpecifier,(E)event);
        }
        catch (Exception exception) {
            final ClientErrorEvent clientErrorEvent = new ClientErrorEvent(
                                                                    jdiEventCategory,
                                                                    actualSpecifier,
                                                                    userDefinedSpecifier,
                                                                    exception
            );
            try {
                clientEventListeners
                        .stream()
                        .forEach(
                                clientEventListener ->
                                        clientEventListener
                                                .onClientErrorEvent(
                                                        clientErrorEvent
                                                )
                        );
            }
            catch (Exception e) {
                e.printStackTrace();//todo maknut ovo pod log
            }
        }
    }

    protected void notifyClientEventListeners(String actualSpecifier,String userDefinedSpecifier, E event) {
        final ClientEvent clientEvent = new ClientEvent(
                                                jdiEventCategory,
                                                actualSpecifier,
                                                userDefinedSpecifier
        );
        clientEventListeners
                .stream()
                .forEach(
                        clientEventListener ->
                                clientEventListener
                                        .onClientEvent(
                                                clientEvent
                                        )
                );
    }

}
