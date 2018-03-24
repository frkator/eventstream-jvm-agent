package com.github.frkator.visualdebugger.jdi.wrapper;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.*;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;

public interface JdiEventConfigurationFactory {

    ClassPrepareConfiguration createClassPrepareConfiguration(String classExclusionFilterPattern, ReferenceType classFilter, String classFilterPattern, String sourceNameFilterPattern);
    ClassUnloadConfiguration createClassUnloadConfiguration(String classExclusionFilterPattern, String classFilterPattern);
    BreakpointConfiguration createBreakpointConfiguration(String fqcn, Integer lineNumber, ReferenceType referenceType, ObjectReference instance, ThreadReference thread);
    MethodEntryConfiguration createMethodEntryConfiguration(String classExclusionFilter​, String classFilter​, ReferenceType classFilterReferenceType​, ObjectReference instanceFilter​, ThreadReference threadFilter​);
    MethodExitConfiguration createMethodExitConfiguration(String classExclusionFilter​, String classFilter​, ReferenceType classFilterReferenceType​, ObjectReference instanceFilter​, ThreadReference threadFilter​);
    VmDeathConfiguration createVmDeathConfiguration();
}
