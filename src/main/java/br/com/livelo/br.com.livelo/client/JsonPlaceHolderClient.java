package br.com.livelo.br.com.livelo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "jplaceholder", url = "https://jsonplaceholder.typicode.com/")
public interface JsonPlaceHolderClient {

    @GetMapping(value = "/todos")
    List<Object> getTodos();

    @GetMapping(value = "/todos/{todoId}")
    Object getTodosById(@PathVariable("todoId") int todoId);

}
