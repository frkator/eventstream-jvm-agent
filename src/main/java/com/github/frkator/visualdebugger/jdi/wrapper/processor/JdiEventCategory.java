package com.github.frkator.visualdebugger.jdi.wrapper.processor;

import com.sun.jdi.event.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public enum JdiEventCategory {

    CLASS_LOAD(parse(ClassPrepareEvent.class)),
    CLASS_UNLOAD(parse(ClassUnloadEvent.class)),
    METHOD_ENTRY(parse(MethodEntryEvent.class)),
    METHOD_EXIT(parse(MethodExitEvent.class)),
    BREAKPOINT(parse(BreakpointEvent.class)),
    VM_CONNECT(parse(VMStartEvent.class)),
    VM_DISCONNECT(parse(VMDisconnectEvent.class)),
    VM_DEATH(parse(VMDeathEvent.class));

    public final String category;

    JdiEventCategory(String eventCategory) {
        this.category = eventCategory;
    }

    public static String parse(Class<? extends Event> klazz) {
        Class targetClass = null;
        if(klazz.isInterface()) { //interface, subclass of com.sun.jdi.event.Event
            targetClass = klazz;
        }
        else { //inner class of com.sun.tools.jdi.EventSetImpl, subclass of com.sun.tools.jdi.EventSetImpl.EventImpl, implementor of above interface.
            targetClass = klazz.getInterfaces()[0];
        }
        return targetClass.getCanonicalName().replaceFirst("Event$", "");
    }

    public static JdiEventCategory of(Event event) {
        return of(event.getClass());
    }

    public static JdiEventCategory of(Class<? extends Event> klazz) {
        return index.get(parse(klazz));
    }

    public static final Map<String,JdiEventCategory> index = Collections
                                                                .unmodifiableMap(
                                                                        Arrays.asList(
                                                                                JdiEventCategory.values()
                                                                        )
                                                                        .stream()
                                                                        .collect(
                                                                                Collectors.toMap(
                                                                                        category -> category.category,
                                                                                        category -> category
                                                                                )
                                                                        )
                                                                );

}
