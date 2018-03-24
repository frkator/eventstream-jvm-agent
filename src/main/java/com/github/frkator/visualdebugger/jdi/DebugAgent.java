package com.github.frkator.visualdebugger.jdi;

import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventConfigurationFactory;
import com.github.frkator.visualdebugger.jdi.wrapper.JdiProcessorFactory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.*;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.Settings;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.configuration.JdiEventConfigurationFactoryImpl;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.JdiEventCategory;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventRequestProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.JdiEventListener;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.JdiEventProcessor;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.BreakpointSpecification;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.ClassSpecification;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.FullyQualifiedClassNameSpecification;
import com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.MethodSpecification;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class DebugAgent implements JdiEventListener {
    
    private final Logger logger = LoggerFactory.getLogger(DebugAgent.class);

    private final Map<String,Set<BreakpointSpecification>> breakpointSpecificationsByFqcn;
    private final Set<ClassSpecification> clazzRecordingSpecifications;
    private final Set<MethodSpecification> methodRecordingSpecifications;

    private final LinkedHashMap<String, ReferenceType> loadedClassesIndex;
    private final Set<ReferenceType> loadedReferenceTypesIndex;
    private final JdiEventConfigurationFactory jdiEventConfigurationFactory;
    private final JdiProcessorFactory jdiProcessorFactory;
    private final Map<JdiEventCategory,JdiEventProcessor> eventProcessors;
    private final Settings settings;

    public DebugAgent(Set<ClassSpecification> clazzRecordingSpecifications,List<BreakpointSpecification> breakpointSpecifications, List<MethodSpecification> methodRecordingSpecifications) {
        this.methodRecordingSpecifications = new HashSet<>(methodRecordingSpecifications);
        this.clazzRecordingSpecifications = clazzRecordingSpecifications;
        this.breakpointSpecificationsByFqcn =  breakpointSpecifications
                                                        .stream()
                                                        .collect(
                                                            groupingBy(
                                                                    FullyQualifiedClassNameSpecification::getFullyQualifiedClassName,
                                                                    toSet()
                                                            )
                                                        );
        loadedClassesIndex = new LinkedHashMap<>();
        loadedReferenceTypesIndex = new HashSet<>();
        eventProcessors = new LinkedHashMap<>();
        settings = new Settings();
        jdiEventConfigurationFactory = new JdiEventConfigurationFactoryImpl();
        jdiProcessorFactory = new JdiProcessorFactoryImpl(jdiEventConfigurationFactory);
    }

    public void registerListeners(Set<String> excludes, VirtualMachine virtualMachine, List<ClientEventListener> clientEventListeners) {

        eventProcessors.put(
                JdiEventCategory.VM_CONNECT,
                jdiProcessorFactory.createVmStartEventProcessor(virtualMachine,this, settings)
        );
        eventProcessors.put(
                JdiEventCategory.VM_DISCONNECT,
                jdiProcessorFactory.createVmDisconnectEventProcessor(virtualMachine,this, settings)
        );
        VmDeathJdiEventRequestProcessor vmDeathJdiEventRequestProcessor = jdiProcessorFactory.createVmDeathJdiEventRequestProcessor(virtualMachine, this, settings);
        vmDeathJdiEventRequestProcessor.register();
        eventProcessors.put(
                vmDeathJdiEventRequestProcessor.getEventCategory(),
                vmDeathJdiEventRequestProcessor.newEventProcessor()
        );

        clazzRecordingSpecifications
                .stream()
                .forEach(classSpecification -> {
                        createAndRegisterClassRecordingProcessors(
                                virtualMachine,
                                classSpecification.inclusionPattern,
                                classSpecification.exclusionPattern
                        );
                    }
                );//todo excludes!!!rijesiti za method i bkpt, ako ne postoje classprep za iste dodati ih
        methodRecordingSpecifications
                .stream()
                .forEach( methodSpecification -> {
                            createAndRegisterMethodEventProcessors(
                                    virtualMachine,
                                    methodSpecification.inclusionPattern,
                                    methodSpecification.exclusionPattern,
                                    methodSpecification.entry
                            );
                        }
                );
        eventProcessors
                .values()
                .stream()
                .forEach(
                        eventProcessor -> eventProcessor.setClientEventListeners(clientEventListeners)
                );
    }

    private void createAndRegisterMethodEventProcessors(VirtualMachine virtualMachine, String inclusionPattern, String exclusionPattern, boolean entry) {
        logger.debug("register method {} with inclusion/exclusion: {}/{}",entry ? "entry" : "exit", inclusionPattern, exclusionPattern);
        List<JdiEventRequestProcessor> methodEventRequestProcessors = new ArrayList<>();
        if(entry) {
            methodEventRequestProcessors.add(
                jdiProcessorFactory.createMethodEntryJdiEventRequestProcessor(
                    virtualMachine,
                    this,
                    settings,
                    inclusionPattern,
                    exclusionPattern,
                    null,
                    null,
                    null
                )
            );
        }
        else {
            methodEventRequestProcessors.add(
                jdiProcessorFactory.createMethodExitJdiEventRequestProcessor(
                    virtualMachine,
                    this,
                    settings,
                    inclusionPattern,
                    exclusionPattern,
                    null,
                    null,
                    null
                )
            );
        }
        registerEventRequestsAndCreateEventProcessors(methodEventRequestProcessors);
    }

    private void createAndRegisterClassRecordingProcessors(VirtualMachine virtualMachine, String classInclusionPattern,String classExclusionPattern) {
        logger.debug("register class load and unload with inclusion/exclusion: {}/{}",classInclusionPattern,classExclusionPattern);
        ClassPrepareJdiEventRequestProcessor classPrepareJdiEventRequestProcessor = jdiProcessorFactory.createClassPrepareJdiEventRequestProcessor(virtualMachine, this, settings, classExclusionPattern, null,classInclusionPattern, null);
        ClassUnloadJdiEventRequestProcessor classUnloadEventRequestProcessor = jdiProcessorFactory.createClassUnloadConfiguration(virtualMachine, this, settings,classExclusionPattern, classInclusionPattern);

        List<JdiEventRequestProcessor> classEventRequestProcessors = new ArrayList<>(Arrays.asList(classPrepareJdiEventRequestProcessor,classUnloadEventRequestProcessor));

        registerEventRequestsAndCreateEventProcessors(classEventRequestProcessors);
    }

    private void registerEventRequestsAndCreateEventProcessors(List<JdiEventRequestProcessor> eventRequestProcessors) {
        eventRequestProcessors.stream().forEach( eventRequestProcessor -> {
                eventRequestProcessor.register();
                if (!eventProcessors.containsKey(eventRequestProcessor.getEventCategory())) {
                    eventProcessors.put(
                            eventRequestProcessor.getEventCategory(),
                            eventRequestProcessor.newEventProcessor()
                    );
                }
        });
    }

    @Override
    public void onClassPrepareEvent(ReferenceType referenceType, VirtualMachine virtualMachine) {
        if (breakpointSpecificationsByFqcn.containsKey(referenceType.name())) {
            List<JdiEventRequestProcessor> breakpointEventRequestProcessors = new ArrayList<>();
            for (BreakpointSpecification breakpointSpecification : breakpointSpecificationsByFqcn.get(referenceType.name())) {
                logger.debug("register breakpoint {}:{}",breakpointSpecification.fullyQualifiedClassName,breakpointSpecification.lineNumber);
                breakpointEventRequestProcessors.add(
                    jdiProcessorFactory.createBreakpointJdiEventRequestProcessor(
                        virtualMachine,
                        this,
                        settings,
                        referenceType.name(),
                        breakpointSpecification.lineNumber,
                        referenceType,
                        null,
                        null
                    )
                );
            }
            registerEventRequestsAndCreateEventProcessors(breakpointEventRequestProcessors);
        }
    }

    @Override
    public void onEventReceivedFromVirtualMachine(Event event) {
        logger.debug("{}", event.toString());
        try {
            final JdiEventCategory category = JdiEventCategory.of(event);
            if (eventProcessors.containsKey(category)) {
                eventProcessors.get(category).processEvent(event);
            }
            else {
                logger.debug("no processor registered");
            }
        }
        catch (Exception exception) {
            throw new IllegalStateException("dfd-to-JDI integration error",exception); //todo bacat excp iz listenera
        }
    }


}