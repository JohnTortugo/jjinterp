package com.jinterp;

public class Main {
    public static void main(String[] args) {
        Loader jloader = new Loader("/wf/jjinterp/resources");
        Interpreter jinterp = new Interpreter(jloader);

        jinterp.run("EmptyClass");
    }
}
