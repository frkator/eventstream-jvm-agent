package com.github.frkator.visualdebugger.jdi.wrapper.processor;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ValueIntrospector {

    public enum VALUE {
        NULL,VOID
    }

    private static final Logger log = LoggerFactory.getLogger(ValueIntrospector.class);

    /**
     * only primitives,
     * will break for Void
     * references should be visited ...
     * @param value
     * @return
     * @throws Exception
     */
    public static CategorizedValue categorize(Value value) throws Exception {
        log.trace("input value {}",value);
        ResolvedValue resolved = resolve(value);
        if (resolved.primitive) {
            return toCategorizedValue(resolved, null);
        }
        if (value instanceof ObjectReference) {
            return traverse((ObjectReference) value);
        }
        throw new IllegalArgumentException(value == null ? null : value.toString());
    }

    private static ResolvedValue resolve(Value input) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Object jdiValue = input.getClass().cast(input);
        final Object javaLangValue = jdiValue.getClass().getMethod("value").invoke(jdiValue);
        log.trace("resolved as primitive value {} of type {}",javaLangValue,javaLangValue.getClass().getCanonicalName());
        return new ResolvedValue(javaLangValue, input instanceof PrimitiveValue);
    }
    
    private static class RecursionState {
        
        final CategorizedValue parent;
        final Field field;

        private RecursionState(CategorizedValue parent, Field field) {
            this.parent = parent;
            this.field = field;
        }
    }

    private static CategorizedValue traverse(ObjectReference input) throws Exception {
        log.trace("start traversing object reference {}",input);
        Stack<RecursionState> recursionStack = new Stack<>();
        ResolvedValue resolved = resolve(input);;
        CategorizedValue root = toCategorizedValue(resolved, null);
        CategorizedValue previousCategorizedValue = root;
        ObjectReference currentReference = input;
        Value currentValue = null;
        int fieldsCount = 0, depthCount = 0; //todo depthCount off but not always
        if (!currentReference.referenceType().allFields().isEmpty()) {
            do {
                recursionStack.addAll(
                    prepareInstanceFieldsForRecursiveIteration(
                            currentReference,
                            resolved,
                            previousCategorizedValue
                    )
                );
                previousCategorizedValue = recursionStack.peek().parent;
                currentValue = currentReference.getValue(recursionStack.pop().field);
                resolved = resolve(currentValue);
                previousCategorizedValue.children.add(toCategorizedValue(resolved, previousCategorizedValue));
                fieldsCount++;
                if (currentValue instanceof ObjectReference) {
                    currentReference = (ObjectReference) currentValue;
                    log.trace("resolved object reference field {}",currentReference);
                    previousCategorizedValue = previousCategorizedValue.children.get(previousCategorizedValue.children.size());
                    depthCount++;
                } else if (!resolved.primitive) {
                    throw new IllegalArgumentException(input == null ? null : input.toString());
                }
            }
            while (!recursionStack.empty());
            if (depthCount == 0) depthCount++;
        }
        log.trace("done traversing object reference {}, total of {} fields with maximum depth {}",input,fieldsCount,depthCount);
        return root;
    }

    private static List<RecursionState> prepareInstanceFieldsForRecursiveIteration(ObjectReference currentReference,  ResolvedValue resolved, CategorizedValue previousCategorizedValue) {
        return currentReference
                .referenceType()
                .allFields()
                .stream()
                .map( field -> new RecursionState(
                                        toCategorizedValue(
                                                resolved,
                                                previousCategorizedValue
                                        ),
                                        field
                        )
                )
                .collect(Collectors.toList());
    }

    private static CategorizedValue toCategorizedValue(ResolvedValue resolved, CategorizedValue previousCategorizedValue) {
        return new CategorizedValue(
                resolved.value,
                resolved.primitive,
                previousCategorizedValue
        );
    }

    private static class ResolvedValue {
        final Object value;
        final boolean primitive;

        ResolvedValue(Object value, boolean primitive) {
            this.value = value;
            this.primitive = primitive;
        }
    }

    public static class CategorizedValue {

        public final StringBuilder id;

        public final Object value;
        public final boolean primitive;

        private final CategorizedValue parent;
        private final List<CategorizedValue> children;

        CategorizedValue(Object value, boolean primitive, CategorizedValue parent) {
            this.value = value;
            this.primitive = primitive;
            this.parent = parent;
            this.children = new ArrayList<>();
            this.id = new StringBuilder();
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

    }

}
