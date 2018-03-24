package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;

public final class ClientErrorEvent extends AbstractClientEvent {

    public final Exception exception;

    public ClientErrorEvent(JdiEventCategory jdiEventCategory, String actualSpecifier, String userDefinedSpecifier, Exception exception) {
        super(jdiEventCategory, actualSpecifier, userDefinedSpecifier);
        this.exception = exception;
    }
}
