package com.github.frkator.visualdebugger.jdi.wrapper;

import com.sun.jdi.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Common {

    public static ThreadReference getThreadReference(String threadName, VirtualMachine virtualMachine) {
        return virtualMachine.allThreads().stream().filter( it -> it.name().equals(threadName)).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() < 1) {
                        throw new IllegalStateException(
                                "debugee JVM not ran in debug mode " //todo
                        );
                    }
                    else if (list.size() < 1) {
                        throw new IllegalStateException(
                                "debugee JVM attaching multiple connectors not allowed" //todo
                        );
                    }
                    return list.get(0);
                }
        ));
    }




    public static void printLocalVariablesAndFields(StackFrame frame) {
        System.out.println("local variables dump start");
        List<LocalVariable> variables = null;
        try {
            variables = frame.visibleVariables();
        } catch (AbsentInformationException e) {}
        if (variables != null) {
            System.out.println(frame.toString());
            for (LocalVariable variable: variables) {
                System.out.println("The value of variable " + variable.name() + " is " + frame.getValue(variable));
            }
        }
        System.out.println("local variables dump end");
        System.out.println("fields dump start");
        List<Field> fields = null;
            fields = frame.location().declaringType().allFields();
        if (fields != null) {
            for (Field field: fields) {
                try {
                    System.out.println("The value of field " + field.name() + " is " + frame.location().declaringType().getValue(field));
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("fields dump end");
    }



    public static void dumpReferenceType(ReferenceType referenceType,VirtualMachine virtualMachine) throws AbsentInformationException, IncompatibleThreadStateException {
        final Optional<ThreadReference> mainOptional = virtualMachine.allThreads().stream().filter(t -> t.name().equalsIgnoreCase("main")).findFirst();
        printLocalVariablesAndFields(mainOptional.get().frames().stream().findFirst().get());
        referenceType.allLineLocations().stream().forEach(l->System.out.println(l.declaringType().name()+" "+l.lineNumber()+" "+l.method().name()));
    }

    public static void dumpLocation(Location location) {
        System.out.printf(
                "%s|%s|%n%s%n",
                "location dump start",
                ToStringBuilder.reflectionToString(location, ToStringStyle.MULTI_LINE_STYLE),
                "location dump end"
        );
    }
}
