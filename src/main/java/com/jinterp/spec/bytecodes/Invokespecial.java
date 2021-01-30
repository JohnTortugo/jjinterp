package com.jinterp.spec.bytecodes;

import java.util.List;
import com.jinterp.spec.constantpool.ConstantPoolEntry;

public class Invokespecial extends BytecodeInstruction {
	private short index;
	
	public Invokespecial(short _index) {
		this.index = _index;
	}
	
	public short getConstantPoolIndex() { return this.index; }

	@Override
	public String toString(List<ConstantPoolEntry> constantPool) {
		return "invokeespecial " + index + " \t #" + constantPool.get(index);
	}
}
