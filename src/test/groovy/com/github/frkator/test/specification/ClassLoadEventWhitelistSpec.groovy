package com.github.frkator.test.specification

import com.github.frkator.test.Assert
import com.github.frkator.test.VirtualMachineInstrumentationResult
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import spock.util.concurrent.BlockingVariable

class ClassLoadEventWhitelistSpec extends AbstractClassLoadEventSpec {

    def "whitelist by fully qualified class name, single"() {
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def instrumentation = createVmAndWatchForClasses([new ClassSpecification(mainClassFqcn,null)])
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

        and: "test class ${mainClassFqcn} loading is notified"
            Assert.classIsLoadedExactlyOnce(state,mainClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,mainClassFqcn,mainClassFqcn)

        and: "${JdiEventCategory.CLASS_LOAD.name()} notification count is 1"
            state.get().eventOutput.findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }.size() == 1

        and: "only registered events are recorded"
            Assert.onlyStandardEventsAnd(state,JdiEventCategory.CLASS_LOAD)

        and: "shutdown was notified"
            Assert.validVmShutdown(state)

    }

    def "whitelist by kleene operator: prefix, infix, suffix" () {
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def mainClassPrefixNotation = mainClassFqcn.replaceFirst("com","*")
            def depClassInfixNotation = depClassFqcn.replaceFirst("github","*")
            def depClassFqcnPostfixNotation = "com.github.frkator.dfd.othe*"
            def instrumentation = createVmAndWatchForClasses(
                    [
                            new ClassSpecification(mainClassPrefixNotation,null),
                            new ClassSpecification(depClassInfixNotation,null),
                            new ClassSpecification(depClassFqcn,null),
                            new ClassSpecification(depClassFqcnPostfixNotation,null)
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

        and: "only registered events are recorded"
            Assert.onlyStandardEventsAnd(state,JdiEventCategory.CLASS_LOAD)

        and: "shutdown was notified"
            Assert.validVmShutdown(state)

    }

}
