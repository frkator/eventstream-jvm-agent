package com.github.frkator.visualdebugger.jdi.wrapper.processor;

public abstract class AbstractClientEvent {

    public final JdiEventCategory jdiEventCategory;
    public final String actualSpecifier;
    public final String userDefinedSpecifier;

    private AbstractClientEvent(){
        throw new IllegalStateException();
    }

    public AbstractClientEvent(JdiEventCategory jdiEventCategory, String actualSpecifier, String userDefinedSpecifier) {
        this.jdiEventCategory = jdiEventCategory;
        this.actualSpecifier = actualSpecifier;
        this.userDefinedSpecifier = userDefinedSpecifier;
    }

    @Override
    public String toString() {
        return "AbstractClientEvent{" +
                "jdiEventCategory=" + jdiEventCategory.name() +
                ", actualSpecifier='" + actualSpecifier + '\'' +
                ", userDefinedSpecifier='" + userDefinedSpecifier + '\'' +
                '}';
    }
}
