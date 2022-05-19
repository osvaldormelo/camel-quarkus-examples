package com.redhat.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(serialization = true)
public class Key {
    private int id;
    private String key;
    private String value;

    public Key() {
		super();		
    }
    public Key(int id,String key,String value){
        this();
        this.setId(id);
        this.setKey(key);
        this.setValue(value);
    }  
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    

    
}
