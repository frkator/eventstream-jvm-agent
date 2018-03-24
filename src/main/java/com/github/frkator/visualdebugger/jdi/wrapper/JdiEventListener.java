package com.github.frkator.visualdebugger.jdi.wrapper;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;

/**
 * input
 */
public interface JdiEventListener {

    void onClassPrepareEvent(ReferenceType referenceType, VirtualMachine virtualMachine);
    void onEventReceivedFromVirtualMachine(Event event);
}
