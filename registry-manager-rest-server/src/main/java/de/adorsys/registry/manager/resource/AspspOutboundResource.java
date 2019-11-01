package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspOutboundClient;
import de.adorsys.registry.manager.service.AspspOutboundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/aspsps/outbound")
public class AspspOutboundResource {

    private AspspOutboundService aspspOutboundService;
    private AspspOutboundClient aspspOutboundClient;

    public AspspOutboundResource(AspspOutboundService aspspOutboundService, AspspOutboundClient aspspOutboundClient) {
        this.aspspOutboundService = aspspOutboundService;
        this.aspspOutboundClient = aspspOutboundClient;
    }

    @PostMapping("/export")
    public ResponseEntity<Void> exportData() throws IOException {
        aspspOutboundClient.exportFile(aspspOutboundService.exportData());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/import")
    public ResponseEntity<Void> importData() {
        aspspOutboundService.importData(aspspOutboundClient.importFile());

        return ResponseEntity.noContent().build();
    }
}
