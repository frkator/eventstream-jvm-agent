package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl;

import com.github.frkator.visualdebugger.jdi.wrapper.Common;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ValueIntrospector;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.BreakpointConfiguration;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ProcessorContext;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiDataEventProcessor;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.BreakpointEvent;

import java.util.List;
import java.util.Map;

public class BreakpointEventProcessor extends JdiDataEventProcessor<BreakpointEvent,BreakpointConfiguration> {

    protected BreakpointEventProcessor(BreakpointConfiguration configuration, ProcessorContext context,JdiEventCategory jdiEventCategory) {
        super(configuration,context,jdiEventCategory);
    }

    @Override
    protected List<ValueIntrospector.CategorizedValue> getEventData(BreakpointEvent event) {

        return null;
    }

    @Override
    protected String serializeSpecifier(BreakpointEvent event) {
        return null;
    }

    // todo http://wayne-adams.blogspot.ie/2011/11/breakpoint-processing-in-jdi.html
    @Override
    protected void process(BreakpointEvent event) {
        BreakpointEvent breakEvent = (BreakpointEvent)event;
        List<ThreadReference> threads = context.getVirtualMachine().allThreads();
        for (ThreadReference thread: threads) {
            if (thread.isSuspended()) {
                try {
                    System.out.println(thread.name());
                    List<StackFrame> frames = thread.frames();
                    if (frames.size() > 0) {
                        StackFrame frame = frames.get(0);
                        String breakpointId = String.format("%s:%d",((BreakpointEvent) event).location().declaringType().name(),((BreakpointEvent) event).location().lineNumber());
                        System.out.println("breakpoint start "+breakpointId);
                        Common.printLocalVariablesAndFields(frame);
                        // Common.dumpLocation(breakEvent.location());
                        System.out.println("breakpoint end "+breakpointId);
                    }

                } catch (IncompatibleThreadStateException e) {
                    System.out.println("Incompatible thread state");
                }

            }
        }
    }
}
