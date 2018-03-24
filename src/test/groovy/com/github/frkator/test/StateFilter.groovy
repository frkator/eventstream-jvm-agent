package com.github.frkator.test

import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory
import com.sun.jdi.event.Event
import spock.util.concurrent.BlockingVariable

class StateFilter {

    static List<AbstractClientEvent> onlyEventsThatAreNot(BlockingVariable<VirtualMachineInstrumentationResult> state, List<JdiEventCategory> categories) {
         state
            .get()
            .eventOutput
            .findAll { e -> categories.inject(true) { result, category -> result && e.jdiEventCategory != category } }
    }

    static List<AbstractClientEvent> loadedClassesByEqualsAndDefinitionByContains(BlockingVariable<VirtualMachineInstrumentationResult> state, String actualSpecifier, String userDefinedSpecifier) {
        state
            .get()
            .eventOutput
            .findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }
            .findAll { e -> e.actualSpecifier == actualSpecifier }
            .findAll { e -> e.userDefinedSpecifier.contains(userDefinedSpecifier) }
    }

    static List<AbstractClientEvent> loadedClassesByMatchesNameAndDefinitionByContains(BlockingVariable<VirtualMachineInstrumentationResult> state, String actualSpecifier, String userDefinedSpecifier) {
        state
            .get()
            .eventOutput
            .findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }
            .findAll { e -> e.actualSpecifier.matches(actualSpecifier) }
            .findAll { e -> e.userDefinedSpecifier.contains(userDefinedSpecifier) }
    }

    static List<AbstractClientEvent> loadedClassesByEquals(BlockingVariable<VirtualMachineInstrumentationResult> state, String specifier) {
        state
            .get()
            .eventOutput
            .findAll { e -> e.jdiEventCategory == JdiEventCategory.CLASS_LOAD }
            .findAll { e -> e.actualSpecifier == specifier }
    }


}
