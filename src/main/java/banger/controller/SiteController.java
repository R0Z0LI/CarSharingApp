package banger.controller;

import banger.dto.SiteDTO;
import banger.model.Site;
import banger.model.User;
import banger.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {
    @Autowired
    private SiteService service;

    @GetMapping("/{id}")
    public ResponseEntity<Site> find(@PathVariable String id){
        Site site = service.find(id);
        if(site == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(site);
    }

    @GetMapping
    public ResponseEntity<List<Site>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/admin/add")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Site> create(@Valid @RequestBody SiteDTO s){
        Site site = service.create(s);
        if(site == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(site);
    }

    @GetMapping("/admin/delete/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<String> deleteSite(@PathVariable String id){
        String response = service.deleteById(id);
        if(response == null) return ResponseEntity.badRequest().body("Nincs ilyen telephely vagy az utolsót törölted volna!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/update/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Site> update(@PathVariable("id") String siteId, @Valid @RequestBody SiteDTO update){
        Site site = service.update(siteId, update);
        if(site == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(site);
    }
}
