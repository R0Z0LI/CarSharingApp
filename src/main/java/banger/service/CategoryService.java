package banger.service;

import banger.dto.CategoryDTO;
import banger.model.Category;
import banger.repository.CategoryRepository;
import banger.service.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private Transformer transformer;

    private static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public List<Category> findAll() {
        logger.info("find all categories");
        List<Category> categories = repository.findAll();
        logger.info("found: " + categories);
        return categories;
    }

    public Category find(String categoryID){
        logger.info("finding category by id: " + categoryID);
        Optional<Category> oCat = repository.findById(categoryID);
        if(oCat.isEmpty()) {
            logger.error("category does not exist");
            return null;
        }
        Category category = oCat.get();
        logger.info("Category found: " + categoryID);
        return category;
    }

    public String deleteById(String id) {
        logger.info("deleting by id: " + id);
        if(!repository.existsById(id)){
            logger.error("category does not exist");
            return null;
        }
        repository.deleteById(id);
        logger.info("successful deletion");
        return "Sikeres kategória törlés!";
    }

    public Category create(CategoryDTO c) {
        logger.info("creating category");
        if(repository.existsByName(c.getName())){
            logger.error("category already exists with name: "+c.getName());
            return null;
        }
        Category category = transformer.transform(c);
        logger.debug("CategoryDTO transformed to Category");
        repository.save(category);
        logger.info("category creation successful");
        return category;
    }

    public Category update(CategoryDTO c, String categoryID) {
        logger.info("update category: " + categoryID);
        Optional<Category> oCat = repository.findById(categoryID);
        if(oCat.isEmpty()) {
            logger.error("category does not exist");
            return null;
        }
        logger.info("found category to update");
        Category category = oCat.get();
        transformer.updateCategory(category, c);
        logger.debug("Category updated from CategoryDTO");
        repository.save(category);
        logger.info("updated successfully");
        return category;
    }
}
