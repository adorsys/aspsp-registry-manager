package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.converter.CsvFileImportValidationReportTOConverter;
import de.adorsys.registry.manager.converter.CsvFileMergeValidationReportTOConverter;
import de.adorsys.registry.manager.model.CsvFileImportValidationReportTO;
import de.adorsys.registry.manager.model.CsvFileMergeValidationReportTO;
import de.adorsys.registry.manager.service.AspspCsvService;
import de.adorsys.registry.manager.service.model.CsvFileImportValidationReportBO;
import de.adorsys.registry.manager.service.model.CsvFileMergeValidationReportBO;
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
    private final CsvFileImportValidationReportTOConverter importValidationReportConverter;
    private final CsvFileMergeValidationReportTOConverter mergeValidationReportConverter;

    public AspspCsvResource(AspspCsvService aspspCsvService,
                            CsvFileImportValidationReportTOConverter importValidationReportConverter,
                            CsvFileMergeValidationReportTOConverter mergeValidationReportConverter) {
        this.aspspCsvService = aspspCsvService;
        this.importValidationReportConverter = importValidationReportConverter;
        this.mergeValidationReportConverter = mergeValidationReportConverter;
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Get all ASPSPs as CSV file")
    @GetMapping(value = "/download", produces = "text/csv")
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
    @ApiOperation("Validate CSV file with ASPSPs for uploading")
    @PostMapping(value = "/validate/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<CsvFileImportValidationReportTO> validateImportCsv(@RequestParam MultipartFile file) {
        logger.info("Validate the CSV file with ASPSPs");

        try {
            CsvFileImportValidationReportBO validationReportBO = aspspCsvService.validateImportCsv(file.getBytes());
            CsvFileImportValidationReportTO validationReportTO
                    = importValidationReportConverter.toCsvFileImportValidationReportTO(validationReportBO);

            if (validationReportBO.isNotValid()) {
                return ResponseEntity.badRequest()
                               .body(validationReportTO);
            }

            return ResponseEntity.ok(validationReportTO);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Post all ASPSPs from CSV file")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public void importCsv(@RequestParam MultipartFile file) {
        logger.info("Post all ASPSPs from CSV file");

        try {
            aspspCsvService.importCsv(file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Validate CSV file with ASPSPs for merging")
    @PostMapping(value = "/validate/merge", consumes = {"multipart/form-data"})
    public ResponseEntity<CsvFileMergeValidationReportTO> validateMergeCsv(@RequestParam MultipartFile file) {
        logger.info("Validate the CSV file with ASPSPs");

        try {
            CsvFileMergeValidationReportBO validationReportBO = aspspCsvService.validateMergeCsv(file.getBytes());
            CsvFileMergeValidationReportTO validationReportTO
                    = mergeValidationReportConverter.toCsvFileMergeValidationReportTO(validationReportBO);

            if (validationReportBO.isNotValid()) {
                return ResponseEntity.badRequest()
                               .body(validationReportTO);
            }

            return ResponseEntity.ok(validationReportTO);
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
