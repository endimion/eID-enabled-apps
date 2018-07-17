/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.serviceImpl;

import teem.loginapp.model.dao.StorkAttributeMngRepository;
import teem.loginapp.model.dao.StorkAttributesDAO;
import teem.loginapp.model.dmo.StorkAttributesDMO;
import teem.loginapp.model.dmo.StrokAttributesMongoDMO;
import teem.loginapp.service.StorkAttributeService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nikos
 */
@Service
public class StorkAttributeServiceImpl implements StorkAttributeService{

    @Autowired
    private StorkAttributesDAO attrDAO;
    
    
    @Autowired
    private StorkAttributeMngRepository attrRepo;
    
    @Override
    @Transactional
    public StorkAttributesDMO findByName(String name) {
        return attrDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<StorkAttributesDMO> getEnabled() {
        return attrDAO.getEnabledAttributes();
    }

    @Override
    @Transactional
    public List<StrokAttributesMongoDMO> getEnabledMng() {
        return attrRepo.findAll().stream().filter(attr ->{
            return attr.isEnabled() ;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StrokAttributesMongoDMO save(StrokAttributesMongoDMO dmo) {
        return attrRepo.save(dmo);
    }

    @Override
    public StrokAttributesMongoDMO findByNameMng(String name) {
       return attrRepo.findFirstByName(name);
    }
    
    @Override
    public StrokAttributesMongoDMO findByEiDASNameMng(String name) {
       return attrRepo.findFirstByEidasName(name);
    }
    
}
