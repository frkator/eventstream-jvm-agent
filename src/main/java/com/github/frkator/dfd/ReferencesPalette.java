package com.github.frkator.dfd;

public class ReferencesPalette {

    String[] getStringArray() {
        return new String[0];
    }

    Integer[] getIntegerArray() {
        return new Integer[1];
    }

    Double[] getDoubleArray() {
        return new Double[]{1.2};
    }

    Long[] getLongArray() {
        return new Long[]{1L, 2L};
    }

    String getString() {
        return "asd";
    }

    Thread getThread() {
        return Thread.currentThread();
    }

    ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }

    ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    int[] getIntArray() {
        return new int[]{2};
    }

    public static void main(String[] args) {

    }
}
