package com.jinterp.constantpool;

public class ConstantPoolUtf8 extends ConstantPoolEntry {
    private String value;

    public ConstantPoolUtf8(String _value) {
        this.value = _value;
	}

	public String getValue() {
        return this.value;
    }
}
