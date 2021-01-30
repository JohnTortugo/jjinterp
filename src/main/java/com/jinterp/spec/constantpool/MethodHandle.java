package com.jinterp.spec.constantpool;

public class MethodHandle extends ConstantPoolEntry {
    private byte reference_kind;
    private short reference_index;

    public MethodHandle(byte reference_kind, short reference_index) {
        this.reference_kind = reference_kind;
        this.reference_index = reference_index;
    }
}
