package banger.controller;


import banger.dto.CategoryDTO;
import banger.model.Category;
import banger.model.User;
import banger.repository.CategoryRepository;
import banger.service.CategoryService;
import banger.service.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService service;
    @Autowired
    private Transformer transformer;


    @GetMapping("{id}")
    public ResponseEntity<Category> find(@PathVariable String id){
        Category cat = service.find(id);
        if(cat == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(cat);
        }
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/admin/delete/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<String> deleteCategory(@PathVariable String id){
        String response = service.deleteById(id);
        if(response == null) return ResponseEntity.badRequest().body("Nincs ilyen kategória!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/add")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryDTO c){
        Category category = service.create(c);
        if(category == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(category);
    }

    @PutMapping("/update/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Category> update(@Valid @RequestBody CategoryDTO c, @PathVariable("id") String id){
        Category category = service.update(c, id);
        if(category == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(category);
    }
}
