package com.github.frkator.test

import com.github.frkator.visualdebugger.jdi.ClientEventListener
import com.github.frkator.visualdebugger.jdi.DebugAgent
import com.github.frkator.visualdebugger.jdi.VirtualMachineManagerFacade
import com.github.frkator.visualdebugger.jdi.wrapper.processor.AbstractClientEvent
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientDataEvent
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientErrorEvent
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.ClientEvent
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.BreakpointSpecification
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.MethodSpecification
import com.sun.jdi.VMDisconnectedException

class JvmGateway {

    static def createAndRunVirtualMachine(String mainClazzFqcn, Set<ClassSpecification> clazzRecordingSpecifications, List<BreakpointSpecification> breakpointSpecifications, List<MethodSpecification> methodRecordingSpecifications, exposeVirtualMachine = false) {
        Exception exception
        DebugAgent debugAgent;
        VirtualMachineManagerFacade virtualMachineManagerFacade;
        final StringBuffer standardOutputBuffer = new StringBuffer()
        final StringBuffer errorOutputBuffer = new StringBuffer()
        final List<AbstractClientEvent> eventOutput = new ArrayList<>()
        final ClientEventListener listener = new ClientEventListener() {
            @Override
            void onStandardStreamEvent(String payload) {
                standardOutputBuffer.append(payload)
            }

            @Override
            void onErrorStreamEvent(String payload) {
                errorOutputBuffer.append(payload)
            }

            @Override
            void onClientEvent(ClientEvent clientEvent) {
                eventOutput << clientEvent
            }

            @Override
            void onClientDataEvent(ClientDataEvent clientDataEvent) {
                eventOutput << clientDataEvent
            }

            @Override
            void onClientErrorEvent(ClientErrorEvent clientErrorEvent) {
                eventOutput << clientErrorEvent
            }
        }
        try {
            debugAgent = new DebugAgent(
                    clazzRecordingSpecifications,
                    breakpointSpecifications,
                    methodRecordingSpecifications
            )
            virtualMachineManagerFacade = exposeVirtualMachine ?
                                                        new TestableVirtualMachineManagerFacade(
                                                                        mainClazzFqcn,
                                                                        debugAgent
                                                        )
                                                        :
                                                        new VirtualMachineManagerFacade(
                                                                mainClazzFqcn,
                                                                debugAgent
                                                        )
            virtualMachineManagerFacade.addClientEventListener(listener)
            virtualMachineManagerFacade.start()
        }
        catch (VMDisconnectedException vmde) {
            //expected
        }
        catch (Exception exc) {
            exc.printStackTrace()
            exception = exc
        }
        [
                exception: exception,
                debugAgent: debugAgent,
                virtualMachineManagerFacade: virtualMachineManagerFacade,
                standardOutputBuffer: standardOutputBuffer,
                errorOutputBuffer: errorOutputBuffer,
                eventOutput: eventOutput
        ]
    }
}
