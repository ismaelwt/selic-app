package com.challenge.selic.config;

import com.challenge.selic.core.calculo.application.CalculoApplicationService;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class Config {


    @MockBean
    protected CalculoApplicationService calculoApplicationService;

    @MockBean
    protected SelicTableClient selicTableClient;
}
