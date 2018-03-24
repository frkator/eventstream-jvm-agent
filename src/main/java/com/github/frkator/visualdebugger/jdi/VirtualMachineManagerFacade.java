package com.github.frkator.visualdebugger.jdi;

import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.BreakpointSpecification;
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class VirtualMachineManagerFacade {

    private final Logger logger = LoggerFactory.getLogger(VirtualMachineManagerFacade.class);

    private VirtualMachine virtualMachine;
    private Optional<String> arguments;
    private final DebugAgent debugAgent;
    private final String mainClassFQCN;
    private final List<ClientEventListener> clientEventListeners = new ArrayList<>();// todo concurrent analysis if adding remove

    public VirtualMachineManagerFacade(String mainClassFqcn, List<BreakpointSpecification> breakpointSpecifications) {
        this.debugAgent = new DebugAgent(Collections.EMPTY_SET,breakpointSpecifications,Collections.EMPTY_LIST);
        arguments = Optional.empty();
        mainClassFQCN = mainClassFqcn;
    }

    public VirtualMachineManagerFacade(String mainClassFqcn,DebugAgent debugAgent) {
        this.debugAgent = debugAgent;
        arguments = Optional.empty();
        mainClassFQCN = mainClassFqcn;
    }

    public void addClientEventListener(ClientEventListener clientEventListener) {
        clientEventListeners.add(clientEventListener);
    }

    public void start() throws Exception {
        launch();
        initAndResume();
    }


    protected void launch() throws VMStartException, IllegalConnectorArgumentsException, IOException {
        virtualMachine = launchVirtualMachine();
    }

    protected void initAndResume() throws Exception {
        debugAgent.registerListeners(Collections.emptySet(),virtualMachine,clientEventListeners);
        virtualMachine.resume();
        Process process = virtualMachine.process();
        final InputStream errorStream = process.getErrorStream();
        final InputStream inputStream = process.getInputStream();
        startInputStreamConsumer(errorStream,true);
        startInputStreamConsumer(inputStream,false);
        eventLoop(virtualMachine);
    }


    private void startInputStreamConsumer(InputStream inputStream, final boolean isError) {

        new Thread() {
            public void run() {
                byte[] buffer = new byte[16384];
                for (;;) {
                    try {
                        int count = inputStream.read(buffer);
                        if (count < 0)
                            return; // EOF
                        final String payload = new String(buffer, 0, count);
                        clientEventListeners.forEach(l -> {
                                    if (isError) {
                                        l.onErrorStreamEvent(payload);
                                    }
                                    else {
                                        l.onStandardStreamEvent(payload);
                                    }
                                }
                        );
                        if (clientEventListeners.isEmpty()) {
                            logger.debug(payload);
                        }
                    } catch (IOException e) {}
                }
            }
        } .start();
    }

    private VirtualMachine launchVirtualMachine() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        com.sun.jdi.VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
        LaunchingConnector defConnector = vmm.defaultConnector();
        Transport transport = defConnector.transport();
        List<LaunchingConnector> list = vmm.launchingConnectors();
        for (LaunchingConnector conn: list) {
            logger.debug(conn.name());
        }
        Map<String, Connector.Argument> arguments = defConnector.defaultArguments();
        for (Map.Entry<String, Connector.Argument> entry : arguments.entrySet()) {
            logger.debug("{} {}",entry.getKey(),entry.getValue());
        }
        arguments
            .get("main")
            .setValue(
                    this.arguments
                        .orElse(
                            String.format(
                                    "-classpath %s %s",
                                    System.getProperty("java.class.path"),
                                    mainClassFQCN
                            )
                        )
            );


        return defConnector.launch(arguments);
    }

    private void eventLoop(VirtualMachine virtualMachine) throws Exception {
        EventSet eventSet;
        while(true) {
            eventSet = virtualMachine.eventQueue().remove();
            logger.debug("{}", eventSet.toString());
            EventIterator eventIterator = eventSet.eventIterator();
            eventIterator.forEachRemaining((Event event) -> {
                debugAgent.onEventReceivedFromVirtualMachine(event);
            });

            eventSet.resume();
        }
    }

    protected VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }
}
