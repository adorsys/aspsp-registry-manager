/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.registry.manager;

import de.adorsys.registry.manager.config.User;
import de.adorsys.registry.manager.config.UserDirectory;
import de.adorsys.registry.manager.converter.AspspTOConverter;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.ArrayList;
import java.util.List;

@EnableConfigurationProperties
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@SpringBootApplication(scanBasePackages = "de.adorsys.registry.manager")
public class SpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args); //NOSONAR
    }

    @Bean
    UserDirectory getUserDirectory(){
        return new UserDirectory() {
            @Override
            public List<User> getUsers() {
                return new ArrayList<>();
            }
        };
    }

    @Bean
    AspspTOConverter getAspspTOConverter(){
        return Mappers.getMapper(AspspTOConverter.class);
    }
}
