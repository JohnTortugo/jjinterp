package com.jinterp.spec.constantpool;

public class NameAndType extends ConstantPoolEntry {
    private short name_index;
    private short descriptor_index;

    public NameAndType(short name_index, short descriptor_index) {
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }
}
