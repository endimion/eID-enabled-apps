/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.serviceImpl;

import teem.loginapp.model.dao.StorkAttributeMngRepository;
import teem.loginapp.model.dao.StorkAttributeValueMgnRepository;
import teem.loginapp.model.dao.StorkPersonMngRepository;
import teem.loginapp.model.dmo.StorkAttributeValueMongoDMO;
import teem.loginapp.model.dmo.StorkPersonMngDMO;
import teem.loginapp.service.StorkPersonService;
import teem.loginapp.utils.MongoUtils;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nikos
 */
@Service
public class StorkPersonServiceImpl implements StorkPersonService {

    @Autowired
    private StorkPersonMngRepository personRepo;

    @Autowired
    private StorkAttributeValueMgnRepository attrValRepo;

    @Autowired
    private StorkAttributeMngRepository attrRepo;

    @Override
    @Transactional
    public StorkPersonMngDMO findByEid(String eid) {
        return personRepo.findByEid(eid);
    }

    @Override
    @Transactional
    public StorkPersonMngDMO findByToken(String token) {
        return personRepo.findByToken(token);
    }

    @Override
    @Transactional
    public void saveOrUpdateEidAttribute(StorkPersonMngDMO person) {
        Set<StorkAttributeValueMongoDMO> newValues =new HashSet<StorkAttributeValueMongoDMO>();
        //cascade save to all objects
        if (person.getAttributesValues() != null) {
            person.getAttributesValues().stream().forEach(attrVal -> {
                newValues.add(MongoUtils.cascadeSaveToAttributes(attrVal, attrValRepo, attrRepo));
            });
        }
        personRepo.save(person);
    }

}
