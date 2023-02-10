package service;

import banger.Application;
import banger.dto.SiteDTO;
import banger.model.Car;
import banger.model.Site;
import banger.repository.SiteRepository;
import banger.service.SiteService;
import banger.service.UserService;
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
public class SiteServiceTest {
    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private SiteService siteService;

    @Mock
    Transformer transformer;

    @Mock
    private UserService userService;


    @Test
    public void whenCreateSite_shouldReturnResponseEntity(){
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setAddress("Pécs");
        Site site = new Site();
        site.setAddress("Pécs");

        when(transformer.transform(ArgumentMatchers.any(SiteDTO.class))).thenReturn(site);
        when(siteRepository.save(ArgumentMatchers.any(Site.class))).thenReturn(site);

        Site createdSite = siteService.create(siteDTO);


        assertEquals(site.getAddress(),createdSite.getAddress());
        verify(transformer).transform(siteDTO);
        verify(siteRepository).save(site);
    }

    @Test
    public void whenCreateSite_shouldReturnNull_ifSiteAlreadyExists(){
        Site site = new Site();
        SiteDTO siteDTO = new SiteDTO();
        when(siteRepository.existsByAddress(site.getAddress())).thenReturn(true);

        Site response = siteService.create(siteDTO);

        assertNull(response);
    }

    @Test
    public void whenCreateSite_shouldReturnNull_ifCarDoesNotExist(){
        Site site = new Site();
        SiteDTO siteDTO = new SiteDTO();

        when(siteRepository.existsByAddress(site.getAddress())).thenReturn(false);
        when(transformer.transform(siteDTO)).thenReturn(null);

        Site response = siteService.create(siteDTO);

        assertNull(response);
    }

    @Test
    public void whenGivenId_shouldReturnSiteResponseEntity_ifFound(){
        Site site = new Site();
        site.setId("abc123");
        site.setAddress("Budapest");
        Optional<Site> oSite = Optional.of(site);

        when(siteRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oSite);

        Site createdSite = siteService.find("abc123");


        assertEquals(site, createdSite);

        verify(siteRepository).findById(site.getId());
    }

    @Test
    public void whenGivenId_shouldReturnBadRequestIn_ifNotFound(){
        Optional<Site> empty = Optional.empty();
        Site site = null;

        when(siteRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Site createdSite = siteService.find("FGB876");


        assertEquals(site, createdSite);

        verify(siteRepository).findById("FGB876");
    }

    @Test
    public void shouldReturnAllSites() {
        List<Site> sites = new ArrayList<Site>();
        sites.add(new Site());

        given(siteRepository.findAll()).willReturn(sites);

        List<Site> expected = siteService.findAll();

        assertEquals(sites, expected);
        verify(siteRepository).findAll();
    }


    @Test
    public void whenGivenId_shouldDeleteSite_ifFound() {
        Site site = new Site();
        site.setAddress("Debrecen");
        site.setId("asd");
        site.setAvailableCars(new ArrayList<>(List.of(new Car(), new Car())));
        ArrayList sites = new ArrayList(List.of(site, new Site()));

        when(siteRepository.existsById(site.getId())).thenReturn(true);
        when(siteRepository.findAll()).thenReturn(sites);
        when(siteRepository.getById(ArgumentMatchers.any(String.class))).thenReturn(site);

        String response = siteService.deleteById(site.getId());

        assertEquals(response, "Sikeres telephely törlés!");

        verify(siteRepository).deleteById(site.getId());
        verify(siteRepository).existsById(site.getId());
    }

    @Test
    public void whenLastSite_shouldReturnNull_onDelete(){
        Site site = new Site();
        site.setAddress("Debrecen");
        site.setId("asd");
        List<Site> sites = new ArrayList<>(List.of(site));

        when(siteRepository.existsById(site.getId())).thenReturn(true);
        when(siteRepository.findAll()).thenReturn(sites);
        when(siteRepository.getById(ArgumentMatchers.any(String.class))).thenReturn(site);

        String response = siteService.deleteById(site.getId());

        assertNull(response);

        verify(siteRepository).existsById(site.getId());
    }

    @Test
    public void should_throw_badRequest_when_site_doesnt_exist_inDelete(){

        when(siteRepository.existsById(ArgumentMatchers.any(String.class))).thenReturn(false);

        String deleteCar = siteService.deleteById("FGB876");

        assertEquals(deleteCar, null);

        verify(siteRepository).existsById("FGB876");
    }

    //nem jó
    @Test
    public void whenUpdateSite_shouldUpdateSite_ifFound(){
        Site site = new Site();
        SiteDTO siteDTO = new SiteDTO();
        site.setId("asd");
        site.setAddress("Pécs");
        siteDTO.setAddress("Mohács");
        Site transformedSiteDTO = new Site();
        transformedSiteDTO.setAddress("Mohács");

        when(siteRepository.findById(site.getId())).thenReturn(Optional.of(site));
        when(siteRepository.save(ArgumentMatchers.any(Site.class))).thenReturn(transformedSiteDTO);
        when(transformer.updateSite(site, siteDTO)).thenReturn(transformedSiteDTO);


        Site updatedSite = siteService.update(site.getId(), siteDTO);

        assertEquals(transformedSiteDTO.getAddress(), updatedSite.getAddress());
        verify(siteRepository).save(transformedSiteDTO);
    }

    @Test
    public void shouldThrowNull_whenSiteDoesntExist_inUpdate(){

        SiteDTO siteDTO = new SiteDTO();
        when(siteRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());

        Site updatedSite = siteService.update("FGB876", siteDTO);

        assertNull(updatedSite);

        verify(siteRepository).findById("FGB876");
    }

    @Test
    public void shouldThrowNull_whenCarDoesntExist_inUpdate(){
        Site site = new Site();
        SiteDTO siteDTO = new SiteDTO();

        when(siteRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(site));
        when(transformer.updateSite(site, siteDTO)).thenReturn(null);

        Site updatedSite = siteService.update("FGB876", siteDTO);

        assertNull(updatedSite);

        verify(siteRepository).findById("FGB876");
    }
}
