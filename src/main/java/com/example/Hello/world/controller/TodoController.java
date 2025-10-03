package com.example.Hello.world.controller;

import com.example.Hello.world.service.TodoService;
import com.example.Hello.world.models.Todo;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController

//RequestMapping- to group all the endpoints under 1 common endpoint
@RequestMapping("/todo")
//Logging
@Slf4j
@Data
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    private TodoService todoService; //without autowired, there is no instance for TodoService, to create it automatically, @autowired is used.

    //PathVariable-To receive input from the user & stores it in variable.
    //Documenting responses
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description = "Todo Retrieved Successfully"),
            @ApiResponse(responseCode = "404",description="Todo not found")
    })


    @GetMapping ("/{id}") //only returns 1 value
    ResponseEntity<Todo> getTodoById(@PathVariable long id){
        //If todo id found return created msg
        try{
            Todo createdTodo = todoService.getTodoById(id);
            return new ResponseEntity<>(createdTodo, HttpStatus.OK);
        }//if not found return error msg
        catch(RuntimeException exception){
            log.info("Todo not found");
            log.warn("");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //to return all the available todos as list
    @GetMapping
    ResponseEntity<List<Todo>>getTodos(){
        return new ResponseEntity<List<Todo>>(todoService.getTodos(),HttpStatus.OK);
    }

    @GetMapping("/page")
    ResponseEntity<Page<Todo>> getTodosPaged(@RequestParam int page,@RequestParam int size){
        return new ResponseEntity<>(todoService.getAllTodosPages(page,size), HttpStatus.OK);
    }

    @PostMapping("/add")
    ResponseEntity<Todo> addTodo(@RequestBody Todo todo){
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);

    }


    //PutMapping - update request
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable Long id, @RequestBody Todo todo) {
        try {
            // find existing
            Todo existingTodo = todoService.getTodoById(id);

            // update only fields that changed
            if (todo.getTitle() != null) existingTodo.setTitle(todo.getTitle());
            if (todo.getDescription() != null) existingTodo.setDescription(todo.getDescription());
            existingTodo.setIsComplete(todo.getIsComplete());

            Todo updatedTodo = todoService.updateTodo(existingTodo);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



    //DeleteMapping
    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable long id){
        todoService.deleteTodoById(id);
    }
}
