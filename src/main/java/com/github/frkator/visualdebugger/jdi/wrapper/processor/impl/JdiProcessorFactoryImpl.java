package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.DebugAgent;
import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventConfigurationFactory;
import com.github.frkator.visualdebugger.jdi.wrapper.JdiProcessorFactory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.*;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.Settings;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

public class JdiProcessorFactoryImpl implements JdiProcessorFactory {

    private final JdiEventConfigurationFactory jdiEventConfigurationFactory;

    public JdiProcessorFactoryImpl(JdiEventConfigurationFactory jdiEventConfigurationFactory) {
        this.jdiEventConfigurationFactory = jdiEventConfigurationFactory;
    }

    @Override
    public ClassPrepareJdiEventRequestProcessor createClassPrepareJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classExclusionFilterPattern, ReferenceType classFilter, String classFilterPattern, String sourceNameFilterPattern) {
        ClassPrepareConfiguration classPrepareConfiguration = jdiEventConfigurationFactory.createClassPrepareConfiguration(classExclusionFilterPattern, classFilter, classFilterPattern, sourceNameFilterPattern);
        return new ClassPrepareJdiEventRequestProcessor(classPrepareConfiguration, new ProcessorContextImpl(virtualMachine, debugAgent, settings));
    }

    @Override
    public ClassUnloadJdiEventRequestProcessor createClassUnloadConfiguration(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classExclusionFilterPattern, String classFilterPattern) {
        ClassUnloadConfiguration classUnloadConfiguration = jdiEventConfigurationFactory.createClassUnloadConfiguration(classExclusionFilterPattern, classFilterPattern);
        return new ClassUnloadJdiEventRequestProcessor(classUnloadConfiguration, new ProcessorContextImpl(virtualMachine, debugAgent, settings));
    }


    @Override
    public BreakpointJdiEventRequestProcessor createBreakpointJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String fqcn, Integer lineNumber, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference) {
        BreakpointConfiguration breakpointConfiguration = jdiEventConfigurationFactory.createBreakpointConfiguration(fqcn,lineNumber, referenceType, instanceReference, threadReference);
        return new BreakpointJdiEventRequestProcessor(breakpointConfiguration, new ProcessorContextImpl(virtualMachine, debugAgent, settings));
    }

    @Override
    public MethodEntryJdiEventRequestProcessor createMethodEntryJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classFilter​, String classExclusionFilter​, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference) {
        MethodEntryConfiguration methodEntryConfiguration = jdiEventConfigurationFactory.createMethodEntryConfiguration(classExclusionFilter​, classFilter​, referenceType, instanceReference, threadReference);
        return new MethodEntryJdiEventRequestProcessor(methodEntryConfiguration, new ProcessorContextImpl(virtualMachine,debugAgent,settings));
    }

    @Override
    public MethodExitJdiEventRequestProcessor createMethodExitJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classFilter​, String classExclusionFilter​, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference) {
        MethodExitConfiguration methodExitConfiguration = jdiEventConfigurationFactory.createMethodExitConfiguration(classExclusionFilter​, classFilter​, referenceType, instanceReference, threadReference);
        return new MethodExitJdiEventRequestProcessor(methodExitConfiguration, new ProcessorContextImpl(virtualMachine,debugAgent,settings));
    }

    @Override
    public VmDeathJdiEventRequestProcessor createVmDeathJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings) {
        VmDeathConfiguration vmDeathConfiguration = jdiEventConfigurationFactory.createVmDeathConfiguration();
        return new VmDeathJdiEventRequestProcessor(vmDeathConfiguration,new ProcessorContextImpl(virtualMachine,debugAgent, settings), JdiEventCategory.VM_DEATH);
    }

    @Override
    public VmStartEventProcessor createVmStartEventProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings) {
        return new VmStartEventProcessor(null,new ProcessorContextImpl(virtualMachine,debugAgent, settings), JdiEventCategory.VM_CONNECT);
    }

    @Override
    public VmDisconnectEventProcessor createVmDisconnectEventProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings) {
        return new VmDisconnectEventProcessor(null,new ProcessorContextImpl(virtualMachine,debugAgent, settings), JdiEventCategory.VM_DISCONNECT);
    }

}
