package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;

import java.util.Objects;

public final class MethodSpecification extends ClassSpecification {

    public final boolean entry;

    public MethodSpecification(String inclusionPattern,String exclusionPattern, boolean entry) {
        super(inclusionPattern, exclusionPattern);
        this.entry = entry;
    }

    public boolean isEntry() {
        return entry;
    }

    public boolean isExit() {
        return !isEntry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MethodSpecification that = (MethodSpecification) o;
        return entry == that.entry &&
                Objects.equals(inclusionPattern, that.inclusionPattern) &&
                Objects.equals(exclusionPattern, that.exclusionPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entry, inclusionPattern, exclusionPattern);
    }
}
