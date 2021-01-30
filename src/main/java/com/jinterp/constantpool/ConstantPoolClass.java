package com.jinterp.constantpool;

public class ConstantPoolClass extends ConstantPoolEntry {
    private short constantPoolIndex;
    private String name;

    public ConstantPoolClass(short _constant_pool_index) {
        this.constantPoolIndex = _constant_pool_index;
    }

    public short getConstantPoolIndex() { return this.constantPoolIndex; }
    
    public void setName(String name) { this.name = name; }
    
    public String getName() { return this.name; }
    
	@Override
	public String toString() {
		return "ConstantPoolClass []";
	}
}
