package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;

public class ClassPrepareConfiguration extends JdiEventConfiguration {

    public final String classFilterPattern;
    public final String sourceNameFilterPattern;
    public final String classExclusionFilterPattern;
    public final ReferenceType classFilter;

    ClassPrepareConfiguration(String classExclusionFilterPattern, ReferenceType classFilter, String classFilterPattern, String sourceNameFilterPattern) {
        this.classFilterPattern = classFilterPattern;
        this.sourceNameFilterPattern = sourceNameFilterPattern;
        this.classFilter = classFilter;
        this.classExclusionFilterPattern = classExclusionFilterPattern;
    }

    @Override
    public void configure(ProcessorContext context) {
        ClassPrepareRequest classPrepareRequest = context.getVirtualMachine().eventRequestManager().createClassPrepareRequest();
        if (classFilterPattern != null) {
            classPrepareRequest.addClassFilter(classFilterPattern);
        }
        if (classExclusionFilterPattern != null) {
            classPrepareRequest.addClassExclusionFilter(classExclusionFilterPattern);
        }
        classPrepareRequest.putProperty(JdiEventConfiguration.class.getSimpleName(),toString(getClass().getSimpleName(),classFilterPattern,classExclusionFilterPattern));
        classPrepareRequest.setEnabled(true);
    }

}
