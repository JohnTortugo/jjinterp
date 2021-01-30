package com.jinterp.spec;

import java.util.List;

import com.jinterp.spec.constantpool.ConstantPoolEntry;

public abstract class Attribute {

    public abstract String toString(List<ConstantPoolEntry> constantPool);
}
