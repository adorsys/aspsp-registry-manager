package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.service.AspspCsvService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;

@RestController
@RequestMapping("/v1/aspsps/csv")
public class AspspCsvResource {
    private static final Logger logger = LoggerFactory.getLogger(AspspCsvResource.class);

    private final AspspCsvService aspspCsvService;

    public AspspCsvResource(AspspCsvService aspspCsvService) {
        this.aspspCsvService = aspspCsvService;
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Get all ASPSPs as CSV file")
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> export() {
        logger.info("Get all ASPSPs as CSV file");

        byte[] response = aspspCsvService.exportCsv();
        String fileName = "aspsps.csv";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(response.length);
        responseHeaders.setContentType(new MediaType("text", "csv"));
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Post all ASPSPs from CSV file")
    @PostMapping(value = "/import", consumes = {"multipart/form-data"})
    public void importCsv(@RequestParam MultipartFile file) {
        logger.info("Post all ASPSPs from CSV file");

        try {
            aspspCsvService.importCsv(file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Merge ASPSPs")
    @PostMapping(value = "/merge", consumes = {"multipart/form-data"})
    public ResponseEntity merge(@RequestParam MultipartFile file) throws IOException {
        logger.info("Merge ASPSPs");

        aspspCsvService.merge(file.getBytes());

        return ResponseEntity
            .noContent()
            .build();
    }
}
