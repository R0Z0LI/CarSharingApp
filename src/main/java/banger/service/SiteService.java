package banger.service;

import banger.dto.SiteDTO;
import banger.model.Car;
import banger.model.Site;
import banger.repository.SiteRepository;
import banger.service.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SiteService {
    @Autowired
    SiteRepository repository;
    @Autowired
    UserService userService;
    @Autowired
    Transformer transformer;

    private static Logger logger = LoggerFactory.getLogger(SiteService.class);

    public List<Site> findAll() {
        logger.info("find all");
        List<Site> sites = repository.findAll();
        logger.debug(sites.toString());
        return sites;
    }

    public Site find(String id){
        logger.debug("finding by id: " + id);
        Optional<Site> oSite = repository.findById(id);
        if(oSite.isEmpty()) {
            logger.error("Site does not exists");
            return null;
        }
        Site site = oSite.get();
        logger.info("found site by id: " + site.getId());
        logger.debug(site.toString());
        return site;
    }

    public Site create(SiteDTO s) {
        logger.debug("siteDTO address: " + s.getAddress());
        if(repository.existsByAddress(s.getAddress())){
            logger.error("site already exists with address: "+s.getAddress());
            return null;
        }
        Site site = transformer.transform(s);
        if(site == null) {
            logger.error("error during site transformation (non-existent car in availableCars)");
            return null;
        }
        logger.info("siteDTO transformed to site");
        repository.save(site);
        logger.info("Created a site" + site.getId());
        logger.debug(site.toString());
        return site;
    }

    public String deleteById(String id) {
        logger.debug("deleting by id: " + id);
        if(!repository.existsById(id)) {
            logger.error("site does not exist with id :"+id);
            return null;
        }
        Site s = repository.getById(id);
        logger.info("site found");
        List<Site> remainingSites = repository.findAll();
        remainingSites.remove(s);
        if(remainingSites.isEmpty()){
            logger.warn("can not delete last site");
            return null;
        }
        Site toMove = remainingSites.get(0);
        logger.info("moving cars from site#" + s.getId() + " to site#" + toMove.getId());
        List<Car> movedCars = new ArrayList<>();
        movedCars.addAll(s.getAvailableCars());
        for(Car c : s.getAvailableCars()){
            c.setSite(toMove);
        }
        toMove.getAvailableCars().addAll(movedCars);
        logger.debug("car move done");
        s.getAvailableCars().clear();
        repository.deleteById(id);
        logger.info("deleted site id: " + id);
        userService.notifySiteDeletion(s, toMove, movedCars);
        return "Sikeres telephely törlés!";
    }

    public Site update(String siteId, SiteDTO update) {
        logger.debug("siteId: " + siteId + " SiteDTO Address: " + update.getAddress());
        Optional<Site> oSite = repository.findById(siteId);
        if(oSite.isEmpty()) {
            logger.error("site does not exist");
            return null;
        }
        logger.info("site found");
        Site site = oSite.get();
        site = transformer.updateSite(site, update);
        logger.debug("SiteDTO transformed to Site");
        if(site == null) {
            logger.error("error during site transformation (non-existent car in availableCars)");
            return null;
        }
        repository.save(site);
        logger.info("updated successfully");

        logger.debug(site.toString());
        return site;
    }
}
