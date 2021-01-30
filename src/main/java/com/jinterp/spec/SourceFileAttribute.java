package com.jinterp.spec;

import java.util.List;

import com.jinterp.spec.constantpool.ConstantPoolEntry;

public class SourceFileAttribute extends Attribute {
	private short constantPoolIndex;
	private String sourceFile;
	
	public SourceFileAttribute(short sourcefile_index) {
		this.constantPoolIndex = sourcefile_index;
	}

	public short getConstantPoolIndex() {
		return constantPoolIndex;
	}

	public void setConstantPoolIndex(short constantPoolIndex) {
		this.constantPoolIndex = constantPoolIndex;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

    public String toString(List<ConstantPoolEntry> constantPool) {
		return this.sourceFile;
    }
}
