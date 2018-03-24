package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;


import java.util.Optional;

public final class BreakpointSpecification extends FullyQualifiedClassNameSpecification {

    public final Integer lineNumber;
    //ime varijable ako je prazna vraća sve iz te linije?  todo u strateđi?

    public BreakpointSpecification(Integer lineNumber, String fullyQualifiedClassName) {
        super(Optional.of(fullyQualifiedClassName).get());
        this.lineNumber = Optional.of(lineNumber).get();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BreakpointSpecification that = (BreakpointSpecification) o;

        if (!lineNumber.equals(that.lineNumber)) return false;
        return fullyQualifiedClassName.equals(that.fullyQualifiedClassName);
    }

    @Override
    public int hashCode() {
        int result = lineNumber.hashCode();
        result = 31 * result + fullyQualifiedClassName.hashCode();
        return result;
    }

}
