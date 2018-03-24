package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;

public abstract class FullyQualifiedClassNameSpecification {

    public final String fullyQualifiedClassName;

    public FullyQualifiedClassNameSpecification(String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    public String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

}
