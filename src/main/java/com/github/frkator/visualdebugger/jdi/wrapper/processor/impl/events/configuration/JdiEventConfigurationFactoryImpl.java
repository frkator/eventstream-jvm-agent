package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventConfigurationFactory;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;

public class JdiEventConfigurationFactoryImpl implements JdiEventConfigurationFactory {

    @Override
    public ClassPrepareConfiguration createClassPrepareConfiguration(String classExclusionFilterPattern, ReferenceType classFilter, String classFilterPattern, String sourceNameFilterPattern) {
        return new ClassPrepareConfiguration(classExclusionFilterPattern, classFilter, classFilterPattern, sourceNameFilterPattern);
    }

    @Override
    public ClassUnloadConfiguration createClassUnloadConfiguration(String sourceNameFilterPattern, String classFilterPattern) {
        return new ClassUnloadConfiguration(classFilterPattern, sourceNameFilterPattern);
    }

    @Override
    public BreakpointConfiguration createBreakpointConfiguration(String fqcn, Integer lineNumber, ReferenceType referenceType,ObjectReference instance, ThreadReference thread) {
        return new BreakpointConfiguration(fqcn,lineNumber,referenceType,instance,thread);
    }

    @Override
    public MethodEntryConfiguration createMethodEntryConfiguration(String classExclusionFilter​, String classFilter​, ReferenceType classFilterReferenceType​, ObjectReference instanceFilter​, ThreadReference threadFilter​) {
        return new MethodEntryConfiguration(classExclusionFilter​,classFilter​);
    }

    @Override
    public MethodExitConfiguration createMethodExitConfiguration(String classExclusionFilter​, String classFilter​, ReferenceType classFilterReferenceType​, ObjectReference instanceFilter​, ThreadReference threadFilter​) {
        return new MethodExitConfiguration(classExclusionFilter​,classFilter​);
    }

    @Override
    public VmDeathConfiguration createVmDeathConfiguration() {
        return new VmDeathConfiguration();
    }


}
