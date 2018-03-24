package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventConfiguration;
import com.sun.jdi.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class BreakpointConfiguration extends JdiEventConfiguration {

    public final String filter;
    public final int lineNumber;
    public final ReferenceType referenceType;
    public final ObjectReference objectReference;
    public final ThreadReference threadReference;

    public BreakpointConfiguration(String filter, int lineNumber, ReferenceType referenceType, ObjectReference objectReference, ThreadReference threadReference) {
        this.filter = filter;
        this.lineNumber = lineNumber;
        this.referenceType = referenceType;
        this.objectReference = objectReference;
        this.threadReference = threadReference;
        if (referenceType == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void configure(ProcessorContext context) {
        List listOfLocations = null;
        try {
            listOfLocations = referenceType.locationsOfLine(lineNumber);
        } catch (AbsentInformationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Location loc = (Location)listOfLocations.get(0);
        listOfLocations.stream().forEach(l-> ReflectionToStringBuilder.toString(l, ToStringStyle.MULTI_LINE_STYLE));
        EventRequestManager erm = context.getVirtualMachine().eventRequestManager();
        BreakpointRequest breakpointRequest = erm.createBreakpointRequest(loc);
        breakpointRequest.putProperty(JdiEventConfiguration.class.getSimpleName(),toString(referenceType.name(),lineNumber));
        breakpointRequest.setEnabled(true);
    }

}
