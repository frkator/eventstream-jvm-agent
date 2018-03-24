package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.request.ClassUnloadRequest;

public class ClassUnloadConfiguration extends JdiEventConfiguration {

    public final String classFilterPattern;
    public final String sourceNameFilterPattern;

    ClassUnloadConfiguration(String classFilterPattern, String sourceNameFilterPattern) {
        this.classFilterPattern = classFilterPattern;
        this.sourceNameFilterPattern = sourceNameFilterPattern;
    }


    @Override
    public void configure(ProcessorContext context) {
        ClassUnloadRequest classUnloadRequest = context.getVirtualMachine().eventRequestManager().createClassUnloadRequest();
        if (classFilterPattern != null) {
            classUnloadRequest.addClassFilter(classFilterPattern);
        }
        classUnloadRequest.putProperty(JdiEventConfiguration.class.getSimpleName(),toString(getClass().getSimpleName(),classFilterPattern,null));
        classUnloadRequest.setEnabled(true);
    }

}
