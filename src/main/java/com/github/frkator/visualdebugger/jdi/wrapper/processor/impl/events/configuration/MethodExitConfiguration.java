package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.MethodExitRequest;

public class MethodExitConfiguration extends JdiEventConfiguration {

    public final String classExclusionFilter​;
    public final String classFilter​;
    private ReferenceType classFilterReferenceType​;
    private ObjectReference instanceFilter​;
    private ThreadReference threadFilter​;

    MethodExitConfiguration(String classExclusionFilter​, String classFilter​) {
        this.classExclusionFilter​ = classExclusionFilter​;
        this.classFilter​ = classFilter​;
    }


    @Override
    public void configure(ProcessorContext context) {
        MethodExitRequest methodExitRequest = context.getVirtualMachine().eventRequestManager().createMethodExitRequest();
        if (classFilter​ != null) {
            methodExitRequest.addClassFilter(classFilter​);
        }
        if (classExclusionFilter​ != null) {
            methodExitRequest.addClassExclusionFilter(classExclusionFilter​);
        }
        methodExitRequest.putProperty(JdiEventConfiguration.class.getSimpleName(),toString(getClass().getSimpleName(),classFilter​,classExclusionFilter​));
        methodExitRequest.setEnabled(true);
    }

}
