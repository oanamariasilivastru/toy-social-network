package com.example.demo11.domain;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {
    public ID id;

    public Entity(ID id) {
        this.id = id;
    }

    public Entity() {
    }

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Entity)) {
            return false;
        } else {
            Entity<?> entity = (Entity)o;
            return this.getId().equals(entity.getId());
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.getId()});
    }

    public String toString() {
        return "Prietenie intre id-urile: " + String.valueOf(this.id);
    }


}
