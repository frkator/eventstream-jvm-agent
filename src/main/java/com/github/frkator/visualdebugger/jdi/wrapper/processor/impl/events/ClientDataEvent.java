package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ValueIntrospector;

import java.util.*;

public final class ClientDataEvent extends AbstractClientEvent {

    public final List<ValueIntrospector.CategorizedValue> arguments;

    public ClientDataEvent(JdiEventCategory jdiEventCategory, String actualSpecifier, String userDefinedSpecifier, List<ValueIntrospector.CategorizedValue> arguments) {
        super(jdiEventCategory, actualSpecifier, userDefinedSpecifier);
        this.arguments = Collections.unmodifiableList(Optional.ofNullable(arguments).orElse(new ArrayList<>()));
    }
}
