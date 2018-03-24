package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;

public final class ClientEvent extends AbstractClientEvent {

    public ClientEvent(JdiEventCategory jdiEventCategory, String actualSpecifier, String userDefinedSpecifier) {
        super(jdiEventCategory, actualSpecifier, userDefinedSpecifier);
    }
}
