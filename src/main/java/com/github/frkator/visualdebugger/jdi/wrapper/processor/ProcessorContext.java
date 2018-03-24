package com.github.frkator.visualdebugger.jdi.wrapper.processor;

import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventListener;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.Settings;
import com.sun.jdi.VirtualMachine;

public interface ProcessorContext {

    VirtualMachine getVirtualMachine();
    JdiEventListener getJdiEventListener();
    Settings getSettings();
}
