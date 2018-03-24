package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;

import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;

import java.lang.reflect.Field;

/**
 * com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.SourceCodeSemanticLocationSerializer#a
 * com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification.SourceCodeSemanticLocationSerializer#parse(java.lang.Class)
 */
public class SourceCodeSemanticLocationSerializer {

    /**
     * Returns FQCN for given referenceType
     * @param referenceType
     * @return
     */
    public static String serialize(ReferenceType referenceType) {
        return referenceType.name();
    }

    /**
     * returns FQCN for given klazz
     * @param klazz
     * @return
     */
    public static String serialize(Class klazz) {
        return klazz.getCanonicalName();
    }

    public static String serialize(Method method) {
        return "";
    }

    public static String serialize(Field field) {
        return String.format(
                "%s#%s",
                serialize(field.getDeclaringClass()),
                field.getName()
        );
    }

}
