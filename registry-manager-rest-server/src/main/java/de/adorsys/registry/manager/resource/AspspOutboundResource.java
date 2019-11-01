package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspOutboundClient;
import de.adorsys.registry.manager.service.AspspOutboundService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/aspsps/outbound")
public class AspspOutboundResource {

    private static final Logger logger = LoggerFactory.getLogger(AspspResource.class);
    private AspspOutboundService aspspOutboundService;
    private AspspOutboundClient aspspOutboundClient;

    public AspspOutboundResource(AspspOutboundService aspspOutboundService, AspspOutboundClient aspspOutboundClient) {
        this.aspspOutboundService = aspspOutboundService;
        this.aspspOutboundClient = aspspOutboundClient;
    }

    @PreAuthorize("hasRole('DEPLOYER')")
    @ApiOperation("Export ASPSPs into Adapter")
    @PostMapping("/export")
    public ResponseEntity<Void> exportData() throws IOException {
        logger.info("Export all ASPSPs into Adapter");

        aspspOutboundClient.exportFile(aspspOutboundService.exportData());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DEPLOYER')")
    @ApiOperation("Import ASPSPs from Adapter")
    @GetMapping("/import")
    public ResponseEntity<Void> importData() {
        logger.info("Import all ASPSPs from Adapter");

        aspspOutboundService.importData(aspspOutboundClient.importFile());

        return ResponseEntity.noContent().build();
    }
}
