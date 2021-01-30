package com.jinterp.spec.bytecodes;

import java.util.List;

import com.jinterp.spec.constantpool.ConstantPoolEntry;

public abstract class BytecodeInstruction {
    public String toString(List<ConstantPoolEntry> constantPool) { return toString(); }
}
