package com.jinterp.constantpool;

public class InterfaceMethodReference extends ConstantPoolEntry {
    private short constantpool_class_index;
    private short constantpool_name_and_type_index;

    public InterfaceMethodReference(short constantpool_class_index, short constantpool_name_and_type_index) {
        this.constantpool_class_index = constantpool_class_index;
        this.constantpool_name_and_type_index = constantpool_name_and_type_index;
    } 
}
