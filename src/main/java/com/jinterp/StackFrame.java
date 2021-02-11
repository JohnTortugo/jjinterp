package com.jinterp;

import java.util.Stack;

import com.jinterp.spec.ClassDefinition;
import com.jinterp.spec.CodeAttribute;
import com.jinterp.spec.Method;

public class StackFrame {
    private ClassDefinition klass;
    private Method method;
    private CodeAttribute code;
    private Stack<Long> operands;
    private long[] locals;
    private int ip;

    public StackFrame(ClassDefinition _holderKlass, Method _targetMethod) {
        this.klass = _holderKlass;
        this.method = _targetMethod;
        this.code = _targetMethod.getCode();
        this.ip = 0;
        this.operands = new Stack<Long>();
        this.locals = new long[this.code.getMaxLocals()];
    }

    public ClassDefinition getKlass() {
        return klass;
    }

    public void setKlass(ClassDefinition klass) {
        this.klass = klass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public CodeAttribute getCode() {
        return code;
    }

    public void setCode(CodeAttribute code) {
        this.code = code;
    }

    public Stack<Long> getOperands() {
        return operands;
    }

    public void setOperands(Stack<Long> operands) {
        this.operands = operands;
    }

    public long[] getLocals() {
        return locals;
    }

    public void setLocals(long[] locals) {
        this.locals = locals;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }
}
