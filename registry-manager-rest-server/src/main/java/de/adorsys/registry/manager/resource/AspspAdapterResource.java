package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspAdapterClient;
import de.adorsys.registry.manager.service.AspspAdapterService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/aspsps/adapter")
public class AspspAdapterResource {

    private static final Logger logger = LoggerFactory.getLogger(AspspResource.class);
    private AspspAdapterService aspspAdapterService;
    private AspspAdapterClient aspspAdapterClient;

    public AspspAdapterResource(AspspAdapterService aspspAdapterService, AspspAdapterClient aspspAdapterClient) {
        this.aspspAdapterService = aspspAdapterService;
        this.aspspAdapterClient = aspspAdapterClient;
    }

    @PreAuthorize("hasRole('DEPLOYER')")
    @ApiOperation("Export ASPSPs into Adapter")
    @PostMapping("/export")
    public ResponseEntity<Void> exportData() throws IOException {
        logger.info("Export all ASPSPs into Adapter");

        aspspAdapterClient.exportFile(aspspAdapterService.exportData());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DEPLOYER')")
    @ApiOperation("Import ASPSPs from Adapter")
    @PostMapping("/import")
    public ResponseEntity<Void> importData() {
        logger.info("Import all ASPSPs from Adapter");

        aspspAdapterService.importData(aspspAdapterClient.importFile());

        return ResponseEntity.noContent().build();
    }
}
