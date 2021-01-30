package com.jinterp;

import java.util.List;

import com.jinterp.bytecodes.BytecodeInstruction;

public class CodeAttribute extends Attribute {
	private short maxStack;
	private short maxLocals;
	private List<BytecodeInstruction> instructions;
	private List<ExceptionTableEntry> exceptionTable;
	private List<Attribute> attributes;
	
	public CodeAttribute(short max_stack, short max_locals, List<BytecodeInstruction> parse_bytecode,
			List<ExceptionTableEntry> exception_table, List<Attribute> attributes) {
		this.maxLocals = max_locals;
		this.maxStack = max_stack;
		this.attributes = attributes;
		this.exceptionTable = exception_table;
		this.instructions = parse_bytecode;
	}

	public short getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(short maxStack) {
		this.maxStack = maxStack;
	}

	public short getMaxLocals() {
		return maxLocals;
	}

	public void setMaxLocals(short maxLocals) {
		this.maxLocals = maxLocals;
	}

	public List<BytecodeInstruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<BytecodeInstruction> instructions) {
		this.instructions = instructions;
	}

	public List<ExceptionTableEntry> getExceptionTable() {
		return exceptionTable;
	}

	public void setExceptionTable(List<ExceptionTableEntry> exceptionTable) {
		this.exceptionTable = exceptionTable;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Code: \n");
		buffer.append("  Max locals: " + this.maxLocals + "\n");
		buffer.append("  Max stack: " + this.maxStack + "\n");
		buffer.append("  Instructions: \n");
		
		for (BytecodeInstruction instruction : instructions) {
			buffer.append("    " + instruction + "\n");
		}
		
		return buffer.toString();
	}
}
