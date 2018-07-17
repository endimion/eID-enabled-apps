/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.testControllers;

import teem.loginapp.controllers.RestControllers;
import teem.loginapp.service.MailService;
import teem.loginapp.service.StorkAttributeService;
import teem.loginapp.service.SwellrtAccountService;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.any;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import teem.loginapp.model.dmo.StrokAttributesMongoDMO;
import teem.loginapp.service.ActiveDirectoryService;
import teem.loginapp.service.PropertiesService;
import teem.loginapp.service.TeemProjectService;

/**
 *
 * @author nikos
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class TestSaveJSONPOST {

    @Configuration
    static class SaveJSONPOSTConfig {

        @Bean
        public RestControllers restControllers() {
            return new RestControllers();
        }

        @Bean
        public PropertiesService propServ() {
            return Mockito.mock(PropertiesService.class);
        }

        @Bean
        public MailService mailServ() {
            return Mockito.mock(MailService.class);
        }

        @Bean
        public SwellrtAccountService accountServ() {
            return Mockito.mock(SwellrtAccountService.class);
        }

        @Bean
        public StorkAttributeService attributeServ() {
            return Mockito.mock(StorkAttributeService.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("tokens");
        }

        @Bean
        public TeemProjectService projServ() {
            return Mockito.mock(TeemProjectService.class);
        }

        @Bean
        public ActiveDirectoryService adserv() {
            return Mockito.mock(ActiveDirectoryService.class);
        }

    }
    @Autowired
    private TeemProjectService temProjServ;
    @Autowired
    private StorkAttributeService attrServ;
    @Autowired
    private SwellrtAccountService accountServ;

    @Autowired
    private MailService mailServ;

    @Autowired
    private RestControllers restControllers;

    @Autowired
    private PropertiesService propServ;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        List<String> eidasProps = new ArrayList();
        eidasProps.add("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier");
        eidasProps.add("http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName");
//        Mockito.when(propServ.getProperties())
//                .thenReturn(eidasProps);

        StrokAttributesMongoDMO attr = new StrokAttributesMongoDMO();
        attr.setEidasName("attrname");
        attr.setName("StorkName");
        attr.setComplex(0);
        attr.setEnabled(true);
        attr.setId("id");
        attr.setFriendlyName("fname");
        attr.setRequestedLoa("loa");
        attr.setRequestedStorkQaa(1);
        attr.setRequired(1);

        Mockito.when(attrServ.findByEiDASNameMng(Matchers.anyString())).thenReturn(attr);
    }

//    @Test
    public void testWhenCookiesAreMissing() throws Exception {
        assertEquals(true, true);
        String responseString = "{\"http://eidas.europa.eu/attributes/naturalperson/CurrentAddress\":{\"value\":\"null\",\"complex\":\"0\",\"required\":\"0\"},\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\":{\"value\":\"javier\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/PlaceOfBirth\":{\"value\":\"Place of Birth\",\"complex\":\"0\",\"required\":\"0\"},\"http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName\":{\"value\":\"Garcia\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/DateOfBirth\":{\"value\":\"1980-01-01\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/Gender\":{\"value\":\"Male\",\"complex\":\"0\",\"required\":\"0\"},\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\":{\"value\":\"CD/GR/12345\",\"complex\":\"0\",\"required\":\"1\"}}";
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.restControllers).build();
        mockMvc.perform(post("/saveJSONPOST").param("r", responseString)
                .param("t", "token"))
                .andExpect(status().isOk());

    }

//    @Test
    public void testUAegeanString() throws Exception {
        String responseString = "{\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\":{\"value\":\"Nikos\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName\":{\"value\":\"Triantafyllou\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\":{\"value\":\"triantafyllou.ni@aegean.gr\",\"complex\":\"0\",\"required\":\"1\"}}";
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.restControllers).build();
        mockMvc.perform(post("/saveJSONPOST").param("r", responseString)
                .param("t", "token"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUAegeanStringTrailingSlash() throws Exception {
        String responseString = "{\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\":{\"value\":\"Nikos\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName\":{\"value\":\"Triantafyllou\",\"complex\":\"0\",\"required\":\"1\"},\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\":{\"value\":\"triantafyllou.ni@aegean.gr\",\"complex\":\"0\",\"required\":\"1\"}}";
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.restControllers).build();
        mockMvc.perform(post("/saveJSONPOST").param("r", responseString)
                .param("t", "token/"))
                .andExpect(status().isOk());
    }

}
