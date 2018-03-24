package com.github.frkator.test.specification

import com.github.frkator.dfd.GreedyInfiniteLoop
import com.github.frkator.test.JvmGateway
import com.github.frkator.test.VirtualMachineInstrumentationResult
import com.github.frkator.visualdebugger.jdi.wrapper.processor.ValueIntrospector
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import com.sun.jdi.VirtualMachine
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

class ValueIntrospectorSpec extends Specification {

    @Shared double timeoutSeconds = 60*3
    @Shared VirtualMachine virtualMachine
    @Shared String mainClassFqcn = GreedyInfiniteLoop.class.getCanonicalName()

    def setupSpec() {
        def state = new BlockingVariable<>(timeoutSeconds)
        def instrumentation = JvmGateway
                                    .createAndRunVirtualMachine(
                                        mainClassFqcn,
                                        new HashSet<ClassSpecification>(),
                                        [],
                                        [],
                                        true
                                    )
        virtualMachine = instrumentation.virtualMachineManagerFacade.virtualMachine
    }

    def clearSpec() {
        virtualMachine.exit(0)
    }

    def "all the primitive types are resolved to their java.lang counterparts without loss of information"(expectedResult,isPrimitive) {
        setup: "given the |${expectedResult}| of type |${expectedResult.class.name}|"
            def calculator = { input ->
                ValueIntrospector.categorize(virtualMachine.mirrorOf(input))
            }
            def actualResult = calculator(expectedResult)
            if (expectedResult.class.name == BigDecimal.class.name) {
                expectedResult = expectedResult.doubleValue()
            }

        expect: "type is resolved as |${actualResult.class.name}|"
            actualResult.primitive == isPrimitive
            actualResult.value.class.name == expectedResult.class.name

        and: "value is resalueolved as |${actualResult}|"
            actualResult.value == expectedResult

        where:
            expectedResult | isPrimitive
            (short)2 | true
            (int)2 | true
            (long)2 | true
            (float)2 | true
            (double)2 | true
            (boolean)false | true
            (char)65 | true
            3.30e23 | true
            2.0 | true
            2f | true
            2d | true
            2L | true
            (byte) 1 | true
            0xFA | true
            0b0010_0101 | true
    }
}
