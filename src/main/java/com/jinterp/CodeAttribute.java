package com.jinterp;

import java.util.List;

import com.jinterp.bytecodes.BytecodeInstruction;

public class CodeAttribute extends Attribute {

	public CodeAttribute(short max_stack, short max_locals, List<BytecodeInstruction> parse_bytecode,
			List<ExceptionTableEntry> exception_table, List<Attribute> attributes) {
	}
}
