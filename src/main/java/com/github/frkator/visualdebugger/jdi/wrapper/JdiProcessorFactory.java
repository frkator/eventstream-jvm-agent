package com.github.frkator.visualdebugger.jdi.wrapper;

import com.github.frkator.visualdebugger.jdi.DebugAgent;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.*;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.Settings;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

public interface JdiProcessorFactory {

    ClassPrepareJdiEventRequestProcessor createClassPrepareJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classExclusionFilterPattern, ReferenceType classFilter, String classFilterPattern, String sourceNameFilterPattern);
    ClassUnloadJdiEventRequestProcessor createClassUnloadConfiguration(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classExclusionFilterPattern, String classFilterPattern);
    BreakpointJdiEventRequestProcessor createBreakpointJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String fqcn, Integer lineNumber, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference);
    MethodEntryJdiEventRequestProcessor createMethodEntryJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classFilter​, String classExclusionFilter​, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference);
    MethodExitJdiEventRequestProcessor createMethodExitJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings, String classFilter​, String classExclusionFilter​, ReferenceType referenceType, ObjectReference instanceReference, ThreadReference threadReference);
    VmDeathJdiEventRequestProcessor createVmDeathJdiEventRequestProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings);

    VmStartEventProcessor createVmStartEventProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings);
    VmDisconnectEventProcessor createVmDisconnectEventProcessor(VirtualMachine virtualMachine, DebugAgent debugAgent, Settings settings);

}
