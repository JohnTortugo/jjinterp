package com.jinterp.spec;

import java.util.List;

public class Method {
	private short accessFlags;
	private short nameIndex;
	private String name;
	private short descriptorIndex;
	private String descriptor;
	private List<Attribute> attributes;
	private CodeAttribute code;
	
	public Method(short access_flags, short name_index, short descriptor_index, List<Attribute> attributes) {
		this.accessFlags = access_flags;
		this.nameIndex = name_index;
		this.descriptorIndex = descriptor_index;
		this.attributes = attributes;

		for (Attribute attr : attributes) {
			if (attr instanceof CodeAttribute) {
				this.code = (CodeAttribute) attr;
				break;
			}
		}

		assert this.code != null : "Everything must have a code?!";
	}

	public short getAccessFlags() {
		return accessFlags;
	}

	public short getNameIndex() {
		return nameIndex;
	}

	public short getDescriptorIndex() {
		return descriptorIndex;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}
	public String getDescriptor() {
		return descriptor;
	}

	public CodeAttribute getCode() {
		return code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
}
