package com.example.demo11.domain;



//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Objects;

public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 getLeft() {
        return this.e1;
    }

    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    public E2 getRight() {
        return this.e2;
    }

    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    public String toString() {
        String var10000 = String.valueOf(this.e1);
        return var10000 + "," + String.valueOf(this.e2);
    }

    public boolean equals(Object obj) {
        return this.e1.equals(((Tuple)obj).e1) && this.e2.equals(((Tuple)obj).e2);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.e1, this.e2});
    }
}
