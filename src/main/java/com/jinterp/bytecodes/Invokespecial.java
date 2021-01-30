package com.jinterp.bytecodes;

public class Invokespecial extends BytecodeInstruction {
	private short constantPoolIndex;
	
	public Invokespecial(short constantPoolIndex) {
		this.constantPoolIndex = constantPoolIndex;
	}
	
	public short getConstantPoolIndex() { return this.constantPoolIndex; }

	@Override
	public String toString() {
		return "invokespecial #" + this.getConstantPoolIndex();
	}
}
