package com.jinterp.spec.bytecodes;

import java.util.List;
import com.jinterp.spec.constantpool.ConstantPoolEntry;

public class Invokestatic extends BytecodeInstruction {
	private short index;

	public Invokestatic(short _index) {
		this.index = _index;
	}

	@Override
	public String toString(List<ConstantPoolEntry> constantPool) {
		return "invokestatic " + index + " \t #" + constantPool.get(index);
	}
}
