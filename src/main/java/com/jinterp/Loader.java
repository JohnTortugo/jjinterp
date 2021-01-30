package com.jinterp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;
import com.jinterp.spec.*;
import com.jinterp.spec.bytecodes.*;
import com.jinterp.spec.constantpool.*;

public class Loader {
    private String classpath;

    public Loader(String _classpath) {
        this.classpath = _classpath.endsWith("/") ? _classpath : _classpath + File.separator;
    }    

    public ClassDefinition load(String className) {
        try {
            byte[] bytes = Files.toByteArray(new File(this.classpath + className + ".class"));
            ByteBuffer wrapped = ByteBuffer.wrap(bytes);

            int magic = wrapped.getInt();
            short miv = wrapped.getShort();
            short mav = wrapped.getShort();

            List<ConstantPoolEntry> constantpool = fetchConstantPool(wrapped);

            short access_flags = wrapped.getShort();
            short this_class = wrapped.getShort();
            short parent_class = wrapped.getShort();

            List<Short> interfaces = fetchInterfaces(wrapped);
            List<Field> fields = fetchFields(wrapped, constantpool);
            List<Method> methods = fetchMethods(wrapped, constantpool);
            List<Attribute> attributes = fetchAttributes(wrapped, constantpool);

            return new ClassDefinition(
                    miv,
                    mav,
                    access_flags,
                    this_class,
                    parent_class,
                    constantpool,
                    interfaces,
                    fields,
                    methods,
                    attributes
                );
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Method> fetchMethods(ByteBuffer wrapped, List<ConstantPoolEntry> constantpool) {
        int numMethods = wrapped.getShort();
        List<Method> entries = new ArrayList<Method>(numMethods);

        for (int i=0; i<numMethods; i++) {
            short access_flags = wrapped.getShort();
            short name_index = wrapped.getShort();
            short descriptor_index = wrapped.getShort();
            List<Attribute> attributes = fetchAttributes(wrapped, constantpool);

            entries.add( new Method(access_flags, name_index, descriptor_index, attributes) );
        }

        return entries;
    }

    private List<Field> fetchFields(ByteBuffer wrapped, List<ConstantPoolEntry> constantpool) {
        int numFields = wrapped.getShort();
        List<Field> entries = new ArrayList<Field>(numFields);

        for (int i=0; i<numFields; i++) {
            short access_flags = wrapped.getShort();
            short name_index = wrapped.getShort();
            short descriptor_index = wrapped.getShort();
            List<Attribute> attributes = fetchAttributes(wrapped, constantpool);

            entries.add( new Field(access_flags, name_index, descriptor_index, attributes) );
        }

        return entries;
    }

    private List<Attribute> fetchAttributes(ByteBuffer wrapped, List<ConstantPoolEntry> constantpool) {
        int numAttributes = wrapped.getShort();
        List<Attribute> entries = new ArrayList<Attribute>(numAttributes);

        for (int i=0; i<numAttributes; i++) {
            short attribute_name_index = wrapped.getShort();
            int attribute_length = wrapped.getInt();
            byte[] info = new byte[attribute_length];

            for (int j=0; j<attribute_length; j++) {
                info[j] = wrapped.get();
            }

            entries.add( build_attribute(attribute_name_index, info, constantpool) );
        }

        return entries;
    }

    private List<Short> fetchInterfaces(ByteBuffer wrapped) {
        int numInterfaces = wrapped.getShort();
        List<Short> entries = new ArrayList<Short>(numInterfaces);

        for (int i=0; i<numInterfaces; i++) {
            entries.add( wrapped.getShort() );
        }

        return entries;
    }

    private List<ConstantPoolEntry> fetchConstantPool(ByteBuffer wrapped) {
        int cpSize = wrapped.getShort();
        List<ConstantPoolEntry> entries = new ArrayList<ConstantPoolEntry>(cpSize+1);

        // This is just a padding. This entry should never be used
        entries.add(null);

        for (int i=1; i<cpSize; i++) {
            int tag = wrapped.get();

            switch(tag) {
                case 1: entries.add( fetchUtf8(wrapped) ); break;
                case 3: entries.add( fetchInteger(wrapped) ); break;
                case 4: entries.add( fetchFloat(wrapped) ); break;
                case 7: entries.add( fetchClass(wrapped) ); break;
                case 8: entries.add( fetchString(wrapped) ); break;
                case 9: entries.add( fetchFieldReference(wrapped)); break;
                case 10: entries.add( fetchMethodRef(wrapped) ); break;
                case 11: entries.add( fetchInterfaceMethodReference(wrapped)); break;
                case 12: entries.add( fetchNameAndType(wrapped) ); break;
                case 15: entries.add( fetchMethodHandle(wrapped) ); break;
                case 17: entries.add( fetchDynamic(wrapped) ); break;
                case 18: entries.add( fetchInvokeDynamic(wrapped) ); break;
                default: {
                    System.out.println("This should never happen! tag -> " + tag);
                    System.exit(1);
                }
            }
        }

        return entries;
    }

    private ConstantPoolEntry fetchInvokeDynamic(ByteBuffer wrapped) {
		return new InvokeDynamic(wrapped.getShort(), wrapped.getShort());
	}

	private ConstantPoolEntry fetchDynamic(ByteBuffer wrapped) {
        return new Dynamic(wrapped.getShort(), wrapped.getShort());
    }

    private ConstantPoolEntry fetchMethodHandle(ByteBuffer wrapped) {
		return new MethodHandle(wrapped.get(), wrapped.getShort());
	}

	private ConstantPoolEntry fetchFieldReference(ByteBuffer wrapped) {
        return new FieldReference(wrapped.getShort(), wrapped.getShort());
    }

    private ConstantPoolEntry fetchInterfaceMethodReference(ByteBuffer wrapped) {
        return new InterfaceMethodReference(wrapped.getShort(), wrapped.getShort());
    }

    private ConstantPoolEntry fetchNameAndType(ByteBuffer wrapped) {
		return new NameAndType(wrapped.getShort(), wrapped.getShort());
    }

	private ConstantPoolEntry fetchMethodRef(ByteBuffer wrapped) {
        return new MethodReference(wrapped.getShort(), wrapped.getShort());
	}

    private ConstantPoolClass fetchClass(ByteBuffer wrapped) {
		return new ConstantPoolClass(wrapped.getShort());
    }
    
    private ConstantPoolString fetchString(ByteBuffer wrapped) {
		return new ConstantPoolString(wrapped.getShort());
    }
    
    private ConstantPoolInteger fetchInteger(ByteBuffer wrapped) {
		return new ConstantPoolInteger(wrapped.getInt());
    }
    
    private ConstantPoolFloat fetchFloat(ByteBuffer wrapped) {
		return new ConstantPoolFloat(wrapped.getFloat());
	}
    
	private ConstantPoolUtf8 fetchUtf8(ByteBuffer wrapped) {
        int length = wrapped.getShort();
        byte[] bytes = new byte[length];
        for (int i=0; i<length; i++)
            bytes[i] = wrapped.get();
        return new ConstantPoolUtf8( new String(bytes, StandardCharsets.UTF_8) );
    }

    private Attribute build_attribute(short attribute_name_index, byte[] info, List<ConstantPoolEntry> constantpool) {
        ByteBuffer wrapped = ByteBuffer.wrap(info);
        String name = ((ConstantPoolUtf8) constantpool.get(attribute_name_index)).getValue();

        if (name.equalsIgnoreCase("code")) {
            short max_stack = wrapped.getShort();
            short max_locals = wrapped.getShort();
            int code_length = wrapped.getInt();
            byte[] bytes = new byte[code_length];
            
            for (int i=0; i<code_length; i++) {
                bytes[i] = wrapped.get();
            }

            short exception_table_length = wrapped.getShort();
            List<ExceptionTableEntry> exception_table = new ArrayList<ExceptionTableEntry>(exception_table_length);
            for (int i=0; i<exception_table_length; i++) {
                short start_pc = wrapped.getShort();
                short end_pc = wrapped.getShort();
                short handler_pc = wrapped.getShort();
                short catch_type = wrapped.getShort();

                exception_table.add( new ExceptionTableEntry(start_pc, end_pc, handler_pc, catch_type) );
            }

            List<Attribute> attributes = fetchAttributes(wrapped, constantpool);

            return new CodeAttribute(max_stack, max_locals, parse_bytecode(bytes), exception_table, attributes);
        }
        else if (name.equalsIgnoreCase("SourceFile")) {
            short sourcefile_index = wrapped.getShort();

        	return new SourceFileAttribute(sourcefile_index);
        }
        else if (name.equalsIgnoreCase("LineNumberTable")) {
            return new LineNumberTableAttribute();
        }
        else if (name.equalsIgnoreCase("Signature")) {
            return new SignatureAttribute();
        }
        else if (name.equalsIgnoreCase("StackMapTable")) {
            return new StackMapTableAttribute();
        }
        else if (name.equalsIgnoreCase("Exceptions")) {
            return new ExceptionsAttribute();
        }
        else {
            System.out.println("Didn't find code for handling attribute: " + name);
        }
        /*
        else if name == "InnerClasses" {
            let number_of_classes = wrapped.getShort();
            let mut classes = Vec::with_capacity(number_of_classes as usize);

            for _ in 0..number_of_classes {
                let inner_class_idx = wrapped.getShort();
                let inner_class_info = constant_pool[inner_class_idx as usize].class();

                let outer_class_idx = wrapped.getShort();
                let outer_class_info =  if outer_class_idx != 0 
                                            { 
                                                let outer_class_info_class_idx = constant_pool[outer_class_idx as usize].class();
                                                Some( outer_class_info_class_idx )
                                            }
                                        else
                                            { None };

                let inner_name_idx = wrapped.getShort();
                let inner_name =    if inner_name_idx != 0 
                                        { 
                                            Some( constant_pool[inner_name_idx as usize].utf8() )
                                        }
                                    else
                                        { None };

                classes.push(
                    InnerClasses_attribute {
                        inner_class_info : inner_class_info.to_string(),
                        outer_class_info : outer_class_info,
                        inner_name : inner_name,
                        inner_class_access_flags : wrapped.getShort()
                    }
                );
            }

            inner_classes = Some(classes);
        }
        else if name == "BootstrapMethods" {
            let number_of_bootstrap_methods = wrapped.getShort();
            let mut bs_methods = Vec::with_capacity(number_of_bootstrap_methods as usize);

            for _ in 0..number_of_bootstrap_methods {
                let bootstrap_method_ref = wrapped.getShort();
                let num_bootstrap_arguments = wrapped.getShort();
                let mut bootstrap_arguments = Vec::with_capacity(num_bootstrap_arguments as usize);

                for _ in 0..num_bootstrap_arguments {
                    bootstrap_arguments.push( wrapped.getShort() );
                }

                bs_methods.push(
                    BootstrapMethods_attribute {
                        bootstrap_method_ref,
                        bootstrap_arguments,
                    }
                );
            }

            bootstrap_methods = Some(bs_methods);
        }
        */
        return null;
    }

    private List<BytecodeInstruction> parse_bytecode(byte[] bytes) {
        ByteBuffer wrapped = ByteBuffer.wrap(bytes);
        List<BytecodeInstruction> bytecodes = new ArrayList<BytecodeInstruction>();

        while (wrapped.hasRemaining()) {
            int opcode = wrapped.get() & 0xFF;

            switch (opcode) {
                case   2: bytecodes.add( new Iconstm1() ); break;
                case   3: bytecodes.add( new Iconst0() ); break;
                case   4: bytecodes.add( new Iconst1() ); break;
                case   5: bytecodes.add( new Iconst2() ); break;
                case   6: bytecodes.add( new Iconst3() ); break;
                case   7: bytecodes.add( new Iconst4() ); break;
                case   8: bytecodes.add( new Iconst5() ); break;
                case   9: bytecodes.add( new Lconst0() ); break;
                case  10: bytecodes.add( new Lconst1() ); break;
                case  11: bytecodes.add( new Fconst0() ); break;
                case  12: bytecodes.add( new Fconst1() ); break;
                case  13: bytecodes.add( new Fconst2() ); break;
                case  16: bytecodes.add( new Bipush(wrapped.get()) ); break;
                case  17: bytecodes.add( new Sipush(wrapped.getShort()) ); break;
                case  18: bytecodes.add( new Ldc(wrapped.get()) ); break;
                case  21: bytecodes.add( new Iload(wrapped.get()) ); break;
                case  26: bytecodes.add( new Iload0() ); break;
                case  27: bytecodes.add( new Iload1() ); break;
                case  28: bytecodes.add( new Iload2() ); break;
                case  29: bytecodes.add( new Iload3() ); break;
                case  30: bytecodes.add( new Lload0() ); break;
                case  31: bytecodes.add( new Lload1() ); break;
                case  32: bytecodes.add( new Lload2() ); break;
                case  33: bytecodes.add( new Lload3() ); break;
                case  42: bytecodes.add( new Aload0() ); break;
                case  43: bytecodes.add( new Aload1() ); break;
                case  44: bytecodes.add( new Aload2() ); break;
                case  45: bytecodes.add( new Aload3() ); break;
                case  54: bytecodes.add( new Istore(wrapped.get()) ); break;
                case  59: bytecodes.add( new Istore0() ); break;
                case  60: bytecodes.add( new Istore1() ); break;
                case  61: bytecodes.add( new Istore2() ); break;
                case  62: bytecodes.add( new Istore3() ); break;
                case  63: bytecodes.add( new Lstore0() ); break;
                case  64: bytecodes.add( new Lstore1() ); break;
                case  65: bytecodes.add( new Lstore2() ); break;
                case  66: bytecodes.add( new Lstore3() ); break;
                case  75: bytecodes.add( new Astore0() ); break;
                case  76: bytecodes.add( new Astore1() ); break;
                case  77: bytecodes.add( new Astore2() ); break;
                case  78: bytecodes.add( new Astore3() ); break;
                case  83: bytecodes.add( new Aastore() ); break;
                case  89: bytecodes.add( new Dup() ); break;
                case  96: bytecodes.add( new Iadd() ); break;
                case  97: bytecodes.add( new Ladd() ); break;
                case 132: bytecodes.add( new Iinc(wrapped.get(), wrapped.get()) ); break;
                case 148: bytecodes.add( new Lcmp() ); break;
                case 153: bytecodes.add( new Ifeq(wrapped.getShort()) ); break;
                case 154: bytecodes.add( new Ifne(wrapped.getShort()) ); break;
                case 155: bytecodes.add( new Iflt(wrapped.getShort()) ); break;
                case 156: bytecodes.add( new Ifge(wrapped.getShort()) ); break;
                case 157: bytecodes.add( new Ifgt(wrapped.getShort()) ); break;
                case 158: bytecodes.add( new Ifle(wrapped.getShort()) ); break;
                case 159: bytecodes.add( new IfIcmpeq(wrapped.getShort()) ); break;
                case 160: bytecodes.add( new IfIcmpne(wrapped.getShort()) ); break;
                case 161: bytecodes.add( new IfIcmplt(wrapped.getShort()) ); break;
                case 162: bytecodes.add( new IfIcmpge(wrapped.getShort()) ); break;
                case 163: bytecodes.add( new IfIcmpgt(wrapped.getShort()) ); break;
                case 164: bytecodes.add( new IfIcmple(wrapped.getShort()) ); break;
                case 165: bytecodes.add( new IfAcmpeq(wrapped.getShort())); break;
                case 166: bytecodes.add( new IfAcmpne(wrapped.getShort())); break;
                case 167: bytecodes.add( new Goto(wrapped.getShort()) ); break;
                case 172: bytecodes.add( new Ireturn() ); break;
                case 176: bytecodes.add( new Areturn() ); break;
                case 177: bytecodes.add( new Return() ); break;
                case 178: bytecodes.add( new Getstatic(wrapped.getShort()) ); break;
                case 179: bytecodes.add( new Putstatic(wrapped.getShort()) ); break;
                case 182: bytecodes.add( new Invokevirtual(wrapped.getShort()) ); break;
                case 183: bytecodes.add( new Invokespecial(wrapped.getShort()) ); break;
                case 184: bytecodes.add( new Invokestatic(wrapped.getShort()) ); break;
                case 186: bytecodes.add( new Invokedynamic(wrapped.getShort(), wrapped.getShort()) ); break;
                case 187: bytecodes.add( new New(wrapped.getShort()) ); break;
                case 189: bytecodes.add( new Anewarray(wrapped.getShort()) ); break;
                case 191: bytecodes.add( new Athrow() ); break;
                default: {
                    System.err.println("Unrecognized opcode " + opcode);
                    System.exit(1);
                }
            };
        }

        return bytecodes;
    }
}
