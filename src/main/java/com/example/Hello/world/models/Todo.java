package com.example.Hello.world.models;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Entity //automatically - table will be created
@Data  //automatically writes getters and setters
public class Todo {

    @Id
    @GeneratedValue
    Long id;

   @NotNull
   @NotBlank
    String title;


    String description;
    Boolean isComplete;

    public void setId(Long id) {
        this.id = id;
    }
}
