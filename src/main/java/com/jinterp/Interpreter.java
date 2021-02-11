package com.jinterp;

import com.jinterp.spec.ClassDefinition;
import com.jinterp.spec.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

public class Interpreter {
    private String[] primordialClasses = new String[] {
        "java/lang/Object"
    };

    

    private Loader loader;

    private Map<String, ClassDefinition> loadedClasses = new HashMap<>();

    private Stack<StackFrame> executionStack = new Stack<>();

    public Interpreter(Loader _loader) {
        this.loader = _loader;

        for (String primordialClassName : this.primordialClasses) {
            this.load(primordialClassName);
        }
    }

    private void load(String className) {
        Queue<String> namesOfClassesPendingLoad = new LinkedList<>();

        namesOfClassesPendingLoad.add(className);

        while (!namesOfClassesPendingLoad.isEmpty()) {
            String nameOfNextClass = namesOfClassesPendingLoad.remove();

            if (this.loadedClasses.containsKey(nameOfNextClass)) {
                continue;
            }

            ClassDefinition newClass = this.loader.load(nameOfNextClass);

            if (newClass == null) {
                System.err.println("Could not find class '" + nameOfNextClass + "' on classpath.");
                System.exit(1);
            }

            Method mainMethod = newClass.getMethod("main");
            if (mainMethod != null) {
                this.executionStack.push(new StackFrame(newClass, mainMethod));
            }

            Method clInitMethod = newClass.getMethod("<clinit>");
            if (clInitMethod != null) {
                this.executionStack.push(new StackFrame(newClass, clInitMethod));
            }

            this.loadedClasses.put(nameOfNextClass, newClass);

            namesOfClassesPendingLoad.add(newClass.getParentClassName());

            for (String interfaceName : newClass.getInterfacesNames()) {
                namesOfClassesPendingLoad.add(interfaceName);
            }
        }
    }

    public boolean run(String mainClass) {
        this.load(mainClass);

        while (!this.executionStack.empty()) {
            StackFrame sf = this.executionStack.pop();

            System.out.println("Popped stackframe for method " + sf.getMethod().getName());
        }

        return true;
    }
}
