package com.jinterp;

import java.util.List;

public class Method {
	private short accessFlags;
	private short nameIndex;
	private String name;
	private short descriptorIndex;
	private String descriptor;
	private List<Attribute> attributes;
	
	public Method(short access_flags, short name_index, short descriptor_index, List<Attribute> attributes) {
		this.accessFlags = access_flags;
		this.nameIndex = name_index;
		this.descriptorIndex = descriptor_index;
		this.attributes = attributes;
	}

	public short getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(short accessFlags) {
		this.accessFlags = accessFlags;
	}

	public short getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(short nameIndex) {
		this.nameIndex = nameIndex;
	}

	public short getDescriptorIndex() {
		return descriptorIndex;
	}

	public void setDescriptorIndex(short descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
}
