package com.mycompany.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.mycompany.repository.UsuarioRepository;
import com.mycompany.service.CustomUserDetailsService;

import java.util.Map;

@Component
public class StartupChecks implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupChecks.class);

    private final ApplicationContext ctx;

    public StartupChecks(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, UsuarioRepository> repos = ctx.getBeansOfType(UsuarioRepository.class);
        Map<String, CustomUserDetailsService> uds = ctx.getBeansOfType(CustomUserDetailsService.class);

        logger.info("Startup check: UsuarioRepository beans found = {}", repos.keySet());
        logger.info("Startup check: CustomUserDetailsService beans found = {}", uds.keySet());
    }
}
