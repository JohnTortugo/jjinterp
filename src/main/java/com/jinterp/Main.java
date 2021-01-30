package com.jinterp;

public class Main {
    public static void main(String[] args) {
        Loader l = new Loader("/wf/jjinterp/resources");

        ClassDefinition cd = l.load("EmptyClass");

        cd.print();
    }
}
