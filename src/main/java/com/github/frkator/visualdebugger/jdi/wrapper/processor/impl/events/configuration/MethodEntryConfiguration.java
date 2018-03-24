package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.MethodEntryRequest;

public class MethodEntryConfiguration extends JdiEventConfiguration {

    public final String classExclusionFilter​;
    public final String classFilter​;
    private ReferenceType classFilterReferenceType​;
    private ObjectReference instanceFilter​;
    private ThreadReference threadFilter​;

    MethodEntryConfiguration(String classExclusionFilter, String classFilter​) {
        this.classExclusionFilter​ = classExclusionFilter;
        this.classFilter​ = classFilter​;
    }

    @Override
    public void configure(ProcessorContext context) {
        MethodEntryRequest methodEntryRequest = context.getVirtualMachine().eventRequestManager().createMethodEntryRequest();
        if (classFilter​ != null) {
            methodEntryRequest.addClassFilter(classFilter​);
        }
        if (classExclusionFilter​ != null) {
            methodEntryRequest.addClassExclusionFilter(classExclusionFilter​);
        }
        methodEntryRequest.putProperty(JdiEventConfiguration.class.getSimpleName(),toString(getClass().getSimpleName(),classFilter​,classExclusionFilter​));
        methodEntryRequest.setEnabled(true);
    }
}
