package com.github.frkator.test.specification

import com.github.frkator.dfd.examples.Debugee
import com.github.frkator.test.JvmGateway
import com.github.frkator.test.Assert
import com.github.frkator.test.VirtualMachineInstrumentationResult
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.MethodSpecification
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

class MethodEventSpec extends Specification {

    @Shared double timeoutSeconds = 60*3
    @Shared String mainClassFqcn = Debugee.class.getCanonicalName()

    @Shared BlockingVariable<VirtualMachineInstrumentationResult> state

    def createVmAndWatchForMethodEvents(List<MethodEventSpec> methodSpecs) {
        JvmGateway.createAndRunVirtualMachine(
                mainClassFqcn,
                new HashSet<ClassSpecification>(),
                [],
                methodSpecs
        )
    }

    def cleanup() {
        state = null
    }

    def "method events"(){
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def instrumentation = createVmAndWatchForMethodEvents(
                    [
                            new MethodSpecification(mainClassFqcn,null,true),
                            new MethodSpecification(mainClassFqcn,null,false)
                    ]
            )
            state.set(
                    new VirtualMachineInstrumentationResult(
                            instrumentation.exception,
                            instrumentation.standardOutputBuffer.toString(),
                            instrumentation.errorOutputBuffer.toString(),
                            instrumentation.eventOutput
                    )
            )
        expect: "that it is done with no error"
            Assert.noError(state)

        and: "startup was notified"
            Assert.validVmStartup(state)
/*
        and: "test class ${mainClassFqcn} loaded exactly once - prefix notation is supported"
        Assert.classIsLoadedExactlyOnce(state,mainClassFqcn)
        Assert.classIsLoadedExactlyOnce(state,mainClassFqcn,mainClassPrefixNotation)

        and: "test class ${depClassFqcn} loaded exactly once - infix notation is not supported"
        Assert.classIsLoadedExactlyOnce(state,depClassFqcn)
        Assert.classIsNotLoaded(state,depClassFqcn,depClassInfixNotation)
        Assert.classIsLoadedExactlyOnce(state,depClassFqcn,depClassFqcn)

        and: "test class ${dpDepClassFqcn} loaded exactly once - postfix notation is supported"
        Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn)
        Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn,depClassFqcnPostfixNotation)

        and: "${JdiEventCategory.CLASS_LOAD.name()} notification count is 3"
        state.get().eventOutput.findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }.size() == 3
*/
        and: "only recorded events are registered"
            Assert.onlySingletonEventsAnd(state, [JdiEventCategory.METHOD_ENTRY, JdiEventCategory.METHOD_EXIT])

        and: "shutdown was notified"
            Assert.validVmShutdown(state)
    }
}
