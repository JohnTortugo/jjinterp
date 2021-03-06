package com.jinterp.spec.constantpool;

public class InvokeDynamic extends ConstantPoolEntry {
    private short bootstrap_method_attr_index;
    private short name_and_type_index;

    public InvokeDynamic(short bootstrap_method_attr_index, short name_and_type_index) {
        this.bootstrap_method_attr_index = bootstrap_method_attr_index;
        this.name_and_type_index = name_and_type_index;
    }
}
