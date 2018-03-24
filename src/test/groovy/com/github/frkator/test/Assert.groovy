package com.github.frkator.test;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory
import spock.util.concurrent.BlockingVariable

class Assert {

    static void noError(BlockingVariable<VirtualMachineInstrumentationResult> state) {
        assert state.get().exception == null
        assert state.get().errorOutput.isEmpty()
    }

    static void validVmStartup(BlockingVariable<VirtualMachineInstrumentationResult> state) {
        assert state
                .get()
                .eventOutput
                .findAll { e -> e.jdiEventCategory == JdiEventCategory.VM_CONNECT }
                .size() == 1
    }

    static void validVmShutdown(BlockingVariable<VirtualMachineInstrumentationResult> state) {
        assert state
                .get()
                .eventOutput
                .findAll { e -> e.jdiEventCategory == JdiEventCategory.VM_DISCONNECT }
                .size() == 1
        assert state
                .get()
                .eventOutput
                .findAll { e -> e.jdiEventCategory == JdiEventCategory.VM_DEATH }
                .size() == 2
    }

    static void classIsLoadedExactlyOnce(BlockingVariable<VirtualMachineInstrumentationResult> state, String specifier) {
        assert StateFilter
                .loadedClassesByEquals(
                    state,
                    specifier
                )
                .size() == 1
    }

    static void classIsNotLoaded(BlockingVariable<VirtualMachineInstrumentationResult> state, String specifier) {
        assert StateFilter
                .loadedClassesByEquals(
                    state,
                    specifier
                )
                .isEmpty()
    }

    static void classIsLoadedExactlyOnce(BlockingVariable<VirtualMachineInstrumentationResult> state, String actualSpecifier, String userSpecifier) {
        assert StateFilter
                .loadedClassesByEqualsAndDefinitionByContains(
                    state,
                    actualSpecifier,
                    userSpecifier
                )
                .size() == 1
    }

    static void classIsNotLoaded(BlockingVariable<VirtualMachineInstrumentationResult> state, String actualSpecifier, String userSpecifier) {
        assert StateFilter
                .loadedClassesByEqualsAndDefinitionByContains(
                    state,
                    actualSpecifier,
                    userSpecifier
                )
                .isEmpty()
    }

    static void onlyStandardEventsAnd(BlockingVariable<VirtualMachineInstrumentationResult> state, JdiEventCategory category) {
        onlySingletonEventsAnd(state,[category])
    }

    static void onlySingletonEventsAnd(BlockingVariable<VirtualMachineInstrumentationResult> state, List<JdiEventCategory> categories) {
         assert StateFilter
                    .onlyEventsThatAreNot(
                        state,
                        [
                                JdiEventCategory.VM_CONNECT,
                                JdiEventCategory.VM_DISCONNECT,
                                JdiEventCategory.VM_DEATH
                        ] + categories
                    )
                    .isEmpty()
    }



}
