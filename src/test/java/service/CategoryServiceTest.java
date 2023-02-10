package service;

import banger.Application;
import banger.dto.CategoryDTO;
import banger.model.Category;
import banger.repository.CarRepository;
import banger.repository.CategoryRepository;
import banger.repository.UserRepository;
import banger.service.CategoryService;
import banger.service.transformer.Transformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = Application.class)
public class CategoryServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    Transformer transformer;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void whenCreateCategory_shouldReturnCategory(){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("közép");
        Category category = new Category();
        category.setName("közép");

        when(transformer.transform(ArgumentMatchers.any(CategoryDTO.class))).thenReturn(category);
        when(categoryRepository.save(ArgumentMatchers.any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.create(categoryDTO);


        assertEquals(category.getName(), createdCategory.getName());
        verify(transformer).transform(categoryDTO);
        verify(categoryRepository).save(category);
    }

    @Test
    public void whenCreateCategory_shouldReturnNull_ifNameDoesNotExist(){
        CategoryDTO categoryDTO = new CategoryDTO();

        when(categoryRepository.existsByName(categoryDTO.getName())).thenReturn(true);

        Category response = categoryService.create(categoryDTO);

        assertNull(response);
    }

    @Test
    public void whenGivenId_shouldReturnCategory_ifFound(){
        Category category = new Category();
        category.setId("abc123");
        category.setName("közép");
        Optional<Category> oCat = Optional.of(category);

        when(categoryRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oCat);

        Category createdCategory = categoryService.find("abc123");


        assertEquals(createdCategory, category);

        verify(categoryRepository).findById(category.getId());
    }

    @Test
    public void whenGivenId_shouldReturnNull_ifNotFound(){
        Optional<Category> empty = Optional.empty();
        Category category = null;

        when(categoryRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Category createdCategory = categoryService.find("FGB876");


        assertEquals(createdCategory, category);

        verify(categoryRepository).findById("FGB876");
    }

    @Test
    public void shouldReturnAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        categories.add(new Category());

        given(categoryRepository.findAll()).willReturn(categories);

        List<Category> expected = categoryService.findAll();

        assertEquals(expected, categories);
        verify(categoryRepository).findAll();
    }



    @Test
    public void whenGivenId_shouldDeleteCategory_ifFound() {
        Category category = new Category();
        category.setName("drága");

        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        categoryService.deleteById(category.getId());

        verify(categoryRepository).deleteById(category.getId());
        verify(categoryRepository).existsById(category.getId());
    }

    @Test
    public void should_throw_null_when_category_doesnt_exist_inDelete(){

        when(categoryRepository.existsById(ArgumentMatchers.any(String.class))).thenReturn(false);

        String deleteCar = categoryService.deleteById("FGB876");

        assertEquals(deleteCar, null);

        verify(categoryRepository).existsById("FGB876");
    }

    @Test
    public void whenUpdateCategory_shouldUpdateCategory_ifFound(){
        Category category = new Category();
        CategoryDTO categoryDTO = new CategoryDTO();
        category.setId("asd");
        category.setName("drága");
        categoryDTO.setName("közép");
        Category transformedCategoryDTO = new Category();
        transformedCategoryDTO.setName("közép");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(ArgumentMatchers.any(Category.class))).thenReturn(transformedCategoryDTO);
        when(transformer.updateCategory(category, categoryDTO)).thenCallRealMethod();

        Category createdCategory = categoryService.update(categoryDTO, category.getId());

        assertEquals(transformedCategoryDTO.getName(), createdCategory.getName());
        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(category);
    }


    @Test
    public void should_throw_null_when_site_doesnt_exist_inUpdate(){
        Optional<Category> empty = Optional.empty();

        when(categoryRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Category updatedCategory = categoryService.update(new CategoryDTO(),"FGB876");


        assertEquals(null, updatedCategory);

        verify(categoryRepository).findById("FGB876");
    }
}
