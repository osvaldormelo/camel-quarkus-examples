package redhat.com.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Product {
    private String code;
    private String description;
    private Double value;

    public Product() {
		super();		
    }
    public Product(String code,String description,Double value){
        this();
        this.setCode(code);
        this.setDescription(description);
        this.setValue(value);
    }  
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Double getValue() {
        return value;
    }

    

    
}
