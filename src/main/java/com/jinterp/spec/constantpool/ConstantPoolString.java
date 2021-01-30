package com.jinterp.spec.constantpool;

public class ConstantPoolString extends ConstantPoolEntry {
    private short constantPoolIndex;
    private String value;

    public ConstantPoolString(short _constant_pool_index) {
        this.constantPoolIndex = _constant_pool_index;
    }

	public short getConstantPoolIndex() {
		return constantPoolIndex;
	}

	public void setConstantPoolIndex(short constantPoolIndex) {
		this.constantPoolIndex = constantPoolIndex;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
