package com.jinterp.constantpool;

public class ConstantPoolClass extends ConstantPoolEntry {
    private short contant_pool_index;

    public ConstantPoolClass(short _constant_pool_index) {
        this.contant_pool_index = _constant_pool_index;
    }
}
