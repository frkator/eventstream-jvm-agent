package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;

import java.util.Objects;

public class ClassSpecification {

    public final String inclusionPattern;
    public final String exclusionPattern;

    public ClassSpecification(String inclusionPattern, String exclusionPattern) {
        this.inclusionPattern = inclusionPattern;
        this.exclusionPattern = exclusionPattern;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassSpecification that = (ClassSpecification) o;
        return Objects.equals(inclusionPattern, that.inclusionPattern) &&
                Objects.equals(exclusionPattern, that.exclusionPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inclusionPattern, exclusionPattern);
    }
}
