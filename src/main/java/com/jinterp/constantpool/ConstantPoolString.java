package com.jinterp.constantpool;

public class ConstantPoolString extends ConstantPoolEntry {
    private short contant_pool_index;

    public ConstantPoolString(short _constant_pool_index) {
        this.contant_pool_index = _constant_pool_index;
    }
}
