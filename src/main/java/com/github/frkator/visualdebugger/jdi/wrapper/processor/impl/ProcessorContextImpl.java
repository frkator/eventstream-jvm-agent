package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventListener;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.Settings;
import com.sun.jdi.VirtualMachine;

public class ProcessorContextImpl implements ProcessorContext {

    private final VirtualMachine virtualMachine;
    private final JdiEventListener jdiEventListener;
    private final Settings settings;

    protected ProcessorContextImpl(VirtualMachine virtualMachine, JdiEventListener jdiEventListener,Settings settings) {
        this.virtualMachine = virtualMachine;
        this.jdiEventListener = jdiEventListener;
        this.settings = settings;
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    @Override
    public JdiEventListener getJdiEventListener() {
        return jdiEventListener;
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

}
