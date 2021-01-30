package com.jinterp.spec;

import java.util.List;

import com.jinterp.spec.constantpool.ConstantPoolClass;
import com.jinterp.spec.constantpool.ConstantPoolEntry;
import com.jinterp.spec.constantpool.ConstantPoolString;
import com.jinterp.spec.constantpool.ConstantPoolUtf8;

public class ClassDefinition {
	private short miv;
	private short mav;
	private short accessFlags;
	private short thisClass;
	private short parentClass;
	private List<ConstantPoolEntry> constantPool;
	private List<Short> interfaces;
	private List<Field> fields;
	private List<Method> methods;
	private List<Attribute> attributes;

	public ClassDefinition(short miv, short mav, short access_flags, short this_class, short parent_class,
			List<ConstantPoolEntry> constantpool, List<Short> interfaces, List<Field> fields, List<Method> methods,
			List<Attribute> attributes) {
		this.miv = miv;
		this.mav = mav;
		this.accessFlags = access_flags;
		this.thisClass = this_class;
		this.parentClass = parent_class;
		this.constantPool = constantpool;
		this.interfaces = interfaces;
		this.fields = fields;
		this.methods = methods;
		this.attributes = attributes;
		
		for (ConstantPoolEntry cpEntry : this.getConstantPool()) {
			if (cpEntry instanceof ConstantPoolClass) {
				ConstantPoolClass entry = (ConstantPoolClass) cpEntry;
				ConstantPoolUtf8 nameEntry = (ConstantPoolUtf8) this.getConstantPool().get(entry.getConstantPoolIndex());
				entry.setName(nameEntry.getValue());
			}
			else if (cpEntry instanceof ConstantPoolString) {
				ConstantPoolString entry = (ConstantPoolString) cpEntry;
				ConstantPoolUtf8 nameEntry = (ConstantPoolUtf8) this.getConstantPool().get(entry.getConstantPoolIndex());
				entry.setValue(nameEntry.getValue());
			}
		}
		
		for (Field field : this.getFields()) {
			ConstantPoolUtf8 nameEntry = (ConstantPoolUtf8) this.getConstantPool().get(field.getNameIndex());
			field.setName(nameEntry.getValue());

			ConstantPoolUtf8 descriptorEntry = (ConstantPoolUtf8) this.getConstantPool().get(field.getDescriptorIndex());
			field.setDescriptor(descriptorEntry.getValue());
		}
		
		for (Method method : this.getMethods()) {
			ConstantPoolUtf8 nameEntry = (ConstantPoolUtf8) this.getConstantPool().get(method.getNameIndex());
			method.setName(nameEntry.getValue());

			ConstantPoolUtf8 descriptorEntry = (ConstantPoolUtf8) this.getConstantPool().get(method.getDescriptorIndex());
			method.setDescriptor(descriptorEntry.getValue());
		}
	}
   
	public void print() {
		ConstantPoolClass classNameRef = (ConstantPoolClass) this.constantPool.get(this.getThisClass());
		ConstantPoolClass parentClassNameRef = (ConstantPoolClass) this.constantPool.get(this.getParentClass());
		
		System.out.format("File format version: %d.%d\n", this.getMav(), this.getMiv());
		System.out.format("Class name is: %s\n", classNameRef.getName());
		System.out.format("Parent class name is: %s\n", parentClassNameRef != null ? parentClassNameRef.getName() : "n/a");
		
		System.out.format("This class implements %d interfaces: ", this.getInterfaces().size());
		for (Short interfaceIdx : this.getInterfaces()) {
			System.out.format("%d, ", interfaceIdx);
		}
		System.out.println();
		
		System.out.format("This class has %d attributes: ", this.getAttributes().size());
		for (Attribute attribute : this.getAttributes()) {
			System.out.println(attribute);
		}
		System.out.println();
		
		System.out.format("This class has %d fields: ", this.getFields().size());
		for (Field field : this.getFields()) {
			System.out.format("%s %s\n", field.getName(), field.getDescriptor());
		}
		System.out.println();
		
		System.out.format("This class has %d methods: \n", this.getMethods().size());
		for (Method method : this.getMethods()) {
			System.out.format("  Method name: %s\n", method.getName());
			System.out.format("  Descriptor: %s\n", method.getDescriptor());
			System.out.format("  Attributes: \n");
			if (method.getAttributes().size() == 0) {
				System.out.format("  nonen");
			}
			for (Attribute attribute : method.getAttributes()) {
				String attributeStr = attribute.toString(this.constantPool);
				for (String line : attributeStr.split("\n")) {					
					System.out.println("    " + line);
				}
			}
			System.out.println("  -----------------------------");
		}
		System.out.println();
	}

	public short getMiv() {
		return miv;
	}

	public void setMiv(short miv) {
		this.miv = miv;
	}

	public short getMav() {
		return mav;
	}

	public void setMav(short mav) {
		this.mav = mav;
	}

	public short getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(short accessFlags) {
		this.accessFlags = accessFlags;
	}

	public short getThisClass() {
		return thisClass;
	}

	public void setThisClass(short thisClass) {
		this.thisClass = thisClass;
	}

	public short getParentClass() {
		return parentClass;
	}

	public void setParentClass(short parentClass) {
		this.parentClass = parentClass;
	}

	public List<ConstantPoolEntry> getConstantPool() {
		return constantPool;
	}

	public void setConstantPool(List<ConstantPoolEntry> constantPool) {
		this.constantPool = constantPool;
	}

	public List<Short> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<Short> interfaces) {
		this.interfaces = interfaces;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
}
