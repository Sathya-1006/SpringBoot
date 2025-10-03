package com.example.Hello.world.service;


import com.example.Hello.world.models.Todo;
import com.example.Hello.world.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

//parent - TodoService is a bean type.
@Service
public class TodoService {
    // for TodoRepository- bean concept is applied so autowiring is possible.So that @autowired annotation is used
    @Autowired //dependency injection
    private TodoRepository todoRepository; //creating instance for todo repository


    //CREATE
    public Todo createTodo(Todo todo){
        return todoRepository.save(todo);

    }


    //READ
    public Todo getTodoById(Long id){
        return todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
    }
    public Page<Todo> getAllTodosPages(int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    }


    //findAll() - retrieves all the available todos created/stored
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    //UPDATE
    public Todo updateTodo(Todo todo){
        //if todo available - updates, if not available-creates new
        return todoRepository.save(todo);
    }

    //DELETE
    public void deleteTodoById(Long id){
        todoRepository.delete(getTodoById(id));
    }

    public void deleteTodo(Todo todo){
        todoRepository.delete(todo);
    }




}
