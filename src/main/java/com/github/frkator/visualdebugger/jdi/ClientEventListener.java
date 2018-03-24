package com.github.frkator.visualdebugger.jdi;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientDataEvent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientErrorEvent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientEvent;

/**
 * output
 */
public interface ClientEventListener {

    void onStandardStreamEvent(String payload);
    void onErrorStreamEvent(String payload);
    void onClientEvent(ClientEvent clientEvent);
    void onClientDataEvent(ClientDataEvent clientDataEvent);
    void onClientErrorEvent(ClientErrorEvent clientErrorEvent);

}
