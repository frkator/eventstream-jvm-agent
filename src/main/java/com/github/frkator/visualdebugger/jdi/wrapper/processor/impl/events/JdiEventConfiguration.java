package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events;

import com.sun.jdi.request.EventRequest;

public abstract class JdiEventConfiguration implements Configurable {

    public String toString(String configurationName,String inclusionExpression, String exclusionExpresion){
        return String.format("%s{incl=%s,excl=%s}",
                                configurationName.replaceFirst("Configuration",""),
                                inclusionExpression,
                                exclusionExpresion
        );
    }

    public String toString(String fqcn, int lineNumber){
        return String.format("%s:%s",
                fqcn,
                lineNumber
        );
    }

}
