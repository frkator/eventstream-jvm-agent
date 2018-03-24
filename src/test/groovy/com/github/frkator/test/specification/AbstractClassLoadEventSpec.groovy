package com.github.frkator.test.specification

import com.github.frkator.dfd.examples.Debugee
import com.github.frkator.dfd.examples.DependecyClass
import com.github.frkator.dfd.other.DifferentPackageDepClass
import com.github.frkator.test.JvmGateway
import com.github.frkator.test.VirtualMachineInstrumentationResult
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

abstract class AbstractClassLoadEventSpec extends Specification {

    @Shared double timeoutSeconds = 60*3
    @Shared String mainClassFqcn = Debugee.class.getCanonicalName()
    @Shared String depClassFqcn = DependecyClass.class.getCanonicalName()
    @Shared String dpDepClassFqcn = DifferentPackageDepClass.class.getCanonicalName()

    @Shared BlockingVariable<VirtualMachineInstrumentationResult> state

    def createVmAndWatchForClasses(List<ClassSpecification> classSpecs) {
        JvmGateway.createAndRunVirtualMachine(
                mainClassFqcn,
                new HashSet<ClassSpecification>(classSpecs),
                [],
                []
        )
    }

    def cleanup() {
        state = null
    }
}
