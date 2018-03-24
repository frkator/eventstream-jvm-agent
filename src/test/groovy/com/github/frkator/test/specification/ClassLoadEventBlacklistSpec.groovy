package com.github.frkator.test.specification

import com.github.frkator.dfd.examples.Debugee
import com.github.frkator.test.Assert
import com.github.frkator.test.StateFilter
import com.github.frkator.test.VirtualMachineInstrumentationResult
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import spock.lang.Shared
import spock.util.concurrent.BlockingVariable;

class ClassLoadEventBlacklistSpec extends AbstractClassLoadEventSpec {

    @Shared int dependetClassesOfDebugeeCount = 10

    def "blacklist by class name, single request is supported" () {
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def instrumentation = createVmAndWatchForClasses(
                    [
                            new ClassSpecification("*",mainClassFqcn)
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

        and: "test class ${mainClassFqcn} is not loaded"
            Assert.classIsNotLoaded(state,mainClassFqcn)

        and: "test class ${depClassFqcn} is loaded exactly once"
            Assert.classIsLoadedExactlyOnce(state,depClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,depClassFqcn,"*")

        and: "test class ${dpDepClassFqcn} is loaded exactly once"
            Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn,"*")

        and: "${JdiEventCategory.CLASS_LOAD.name()} notification count is more than ${Debugee.class.getSimpleName()} dependents"
            state.get().eventOutput.findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }.size() > dependetClassesOfDebugeeCount

        and: "only registered events are recorded"
            Assert.onlyStandardEventsAnd(state,JdiEventCategory.CLASS_LOAD)

        and: "shutdown was notified"
            Assert.validVmShutdown(state)

    }

    def "blacklist by package name, single request is supported" () {
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def excludedPackage = "java.lang*"
            def instrumentation = createVmAndWatchForClasses(
                    [
                            new ClassSpecification("*",excludedPackage)
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

        and: "test class ${mainClassFqcn} is loaded exactly once"
            Assert.classIsLoadedExactlyOnce(state,mainClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,mainClassFqcn,"*")

        and: "test class ${depClassFqcn} is loaded exactly once"
            Assert.classIsLoadedExactlyOnce(state,depClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,depClassFqcn,"*")

        and: "test class ${dpDepClassFqcn} is loaded exactly once"
            Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn)
            Assert.classIsLoadedExactlyOnce(state,dpDepClassFqcn,"*")

        and: "classes from ${excludedPackage} are excluded"
            StateFilter.loadedClassesByMatchesNameAndDefinitionByContains(state,excludedPackage,excludedPackage).isEmpty()

        and: "${JdiEventCategory.CLASS_LOAD.name()} notification count is more than ${Debugee.class.getSimpleName()} dependents"
            state.get().eventOutput.findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }.size() > dependetClassesOfDebugeeCount

        and: "only registered events are recorded"
            Assert.onlyStandardEventsAnd(state,JdiEventCategory.CLASS_LOAD)

        and: "shutdown was notified"
            Assert.validVmShutdown(state)

    }

    def "blacklist by class name, 3 requests is not supported" () {
        setup: "when virtual machine is ran"
            state = new BlockingVariable<>(timeoutSeconds)
            def instrumentation = createVmAndWatchForClasses(
                    [
                            new ClassSpecification("*",null),
                            new ClassSpecification(null,mainClassFqcn),
                            new ClassSpecification(null,depClassFqcn),
                            new ClassSpecification(null,dpDepClassFqcn)
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

        and: "test class ${mainClassFqcn} is loaded"
            StateFilter.loadedClassesByEquals(state,mainClassFqcn).size() == 3

        and: "test class ${depClassFqcn} is loaded"
            StateFilter.loadedClassesByEquals(state,depClassFqcn).size() == 3

        and: "test class ${dpDepClassFqcn} is loaded"
            StateFilter.loadedClassesByEquals(state,dpDepClassFqcn).size() == 3

        and: "${JdiEventCategory.CLASS_LOAD.name()} notification count is more than ${Debugee.class.getSimpleName()} dependents"
            state.get().eventOutput.findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }.size() > 10

        and: "only registered events are recorded"
            Assert.onlyStandardEventsAnd(state,JdiEventCategory.CLASS_LOAD)

        and: "shutdown was notified"
            Assert.validVmShutdown(state)

    }
}
