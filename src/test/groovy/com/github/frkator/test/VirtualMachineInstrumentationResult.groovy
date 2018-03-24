package com.github.frkator.test

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent

final class VirtualMachineInstrumentationResult {

    public final Exception exception
    public final String standardOutput
    public final String errorOutput
    public final List<AbstractClientEvent> eventOutput;

    VirtualMachineInstrumentationResult(Exception exception, String standardOutput, String errorOutput, List<AbstractClientEvent> eventOutput) {
        this.exception = exception
        this.standardOutput = standardOutput
        this.errorOutput = errorOutput
        this.eventOutput = Collections.unmodifiableList(eventOutput)
    }

}