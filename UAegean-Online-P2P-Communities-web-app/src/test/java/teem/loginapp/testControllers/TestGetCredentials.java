/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.testControllers;

import java.util.ArrayList;
import teem.loginapp.controllers.RestControllers;
import teem.loginapp.pojo.SwellrtEvent;
import teem.loginapp.service.MailService;
import teem.loginapp.service.StorkAttributeService;
import teem.loginapp.service.SwellrtAccountService;
import teem.loginapp.service.TeemProjectService;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import teem.loginapp.service.ActiveDirectoryService;
import teem.loginapp.service.PropertiesService;
import teem.loginapp.serviceImpl.ActiveDirectoryServiceImpl;
import teem.loginapp.serviceImpl.PropertiesServiceImpl;

/**
 *
 * @author nikos
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class TestGetCredentials {

    @Configuration
    static class SaveJSONPOSTConfig {

        @Bean
        @Primary
        public StorkAttributeService attrServ() {
            return Mockito.mock(StorkAttributeService.class);
        }

        @Bean
        @Primary
        public MailService mailserv() {
            return Mockito.mock(MailService.class);
        }

        @Bean
        @Primary
        public SwellrtAccountService accountService() {
            return Mockito.mock(SwellrtAccountService.class);
        }

        @Bean
        @Primary
        public TeemProjectService projectServ() {
            return Mockito.mock(TeemProjectService.class);
        }

        @Bean
        @Primary
        public RestControllers restControllers() {
            return new RestControllers();
        }

        @Bean
        @Primary
        public PropertiesService propServ() {
            return new PropertiesServiceImpl();
        }

        @Bean
        @Primary
        public ActiveDirectoryService adServ() {
            return new ActiveDirectoryServiceImpl();
        }

        @Bean
        @Primary
        public CacheManager cacheManager() {
            return new CacheManager() {
                @Override
                public Cache getCache(String name) {
                    return Mockito.mock(Cache.class);
                }

                @Override
                public Collection<String> getCacheNames() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
    }

    @Autowired
    private RestControllers restControllers;

    @Autowired
    private SwellrtAccountService accountService;

    @Autowired
    private MailService mailserv;

    @Autowired
    private StorkAttributeService attrserv;

    @Autowired
    private CacheManager cacheManager;

   

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    ValueWrapper mockWrapper;

    @Before
    public void setup() {
        Mockito.when(mailserv.sendEmailsForEvent(any(SwellrtEvent.class), any(List.class)))
                .thenReturn("OK");
        mockWrapper = Mockito.mock(ValueWrapper.class);
        request.addHeader(HttpHeaders.HOST, "myhost.com");
//        request.setLocalPort(PORT_VALID); // e.g. 8081
        request.setRemoteAddr("127.0.0.1"); // e.g. 127.0.0.1
        Mockito.when(cacheManager.getCache("ips").get(request.getRemoteAddr())).thenReturn(mockWrapper);

    }

    private final static String token = "53f9dc9a-b1fd-4fe5-9484-a4a16ccfd3c5";

    @Test
    public void testAccountFound() {

      

        Mockito.when(accountService.findByToken(token)).thenReturn(null);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.restControllers).build();

    }

}
