package com.jinterp;

import com.jinterp.spec.ClassDefinition;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private String[] primordialClasses = new String[] {
        "Object"
    };

    private Loader loader;
    private Map<String, ClassDefinition> loadedClasses = new HashMap<>();

    public Interpreter(Loader _loader) {
        this.loader = _loader;

        for (String primordial : this.primordialClasses) {
            this.loadedClasses.put(primordial, this.loader.load(primordial));
        }
    }

    public boolean run(String mainClass) {
        this.loadedClasses.put(mainClass, this.loader.load(mainClass));

        for (ClassDefinition cd : this.loadedClasses.values()) {
            cd.print();
        }

        return true;
    }
}
