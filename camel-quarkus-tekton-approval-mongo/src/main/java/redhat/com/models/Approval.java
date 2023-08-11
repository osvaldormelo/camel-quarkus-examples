package com.redhat.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Approval {
    private String id;
    private String mud;
    private String description;
    
    public Approval(String id, String mud, String description) {
        this.id = id;
        this.mud = mud;
        this.description = description;
    }
    public Approval() {
		super();		
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMud() {
        return mud;
    }
    public void setMud(String mud) {
        this.mud = mud;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    
    

    

    
}
