package de.adorsys.registry.manager.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AspspManagerController {

    private final String ASPSP_MANAGER_URL = "/manager";

    @GetMapping(ASPSP_MANAGER_URL)
    public String manage() {
        return "index";
    }
}
