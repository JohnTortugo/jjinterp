package com.jinterp;

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

	@Override
	public String toString() {
		return this.sourceFile;
	}
}
