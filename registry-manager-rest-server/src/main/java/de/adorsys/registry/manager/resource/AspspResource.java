package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.converter.AspspTOConverter;
import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.PageBO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(AspspResource.ASPSP_URI)
public class AspspResource {

    private static final Logger logger = LoggerFactory.getLogger(AspspResource.class);
    public static final String ASPSP_URI = "/v1/aspsps";

    private final AspspService aspspService;
    private AspspTOConverter converter;

    public AspspResource(AspspService aspspService, AspspTOConverter converter) {
        this.aspspService = aspspService;
        this.converter = converter;
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Get ASPSPs")
    @GetMapping
    ResponseEntity<List<AspspTO>> getAspsps(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "bic", required = false) String bic,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam(value = "adapterId", required = false) String adapterId,
                                            @RequestParam(value = "iban", required = false) String iban, // if present - other params ignored
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.info("Get all ASPSPs");

        PageBO bos;

        if (iban != null && !iban.isEmpty()) {
            bos = aspspService.getByIban(iban, page, size);
        } else {
            bos = aspspService.getByAspsp(buildAspspBO(name, bic, bankCode, adapterId), page, size);
        }

        return ResponseEntity
                       .status(HttpStatus.OK)
                       .header("X-Total-Elements", String.valueOf(bos.getTotalElements()))
                       .body(converter.toAspspTOList(bos.getContent()));
    }

    private AspspBO buildAspspBO(String name, String bic, String bankCode, String adapterId) {
        AspspBO bo = new AspspBO();

        bo.setAdapterId(adapterId);
        bo.setName(name);
        bo.setBic(bic);
        bo.setBankCode(bankCode);

        return bo;
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Create new ASPSP")
    @PostMapping
    public ResponseEntity<AspspTO> create(@RequestBody AspspTO aspsp) {
        logger.info("Create new ASPSP={}", aspsp);

        AspspBO bo = converter.toAspspBO(aspsp);
        bo = aspspService.save(bo);

        return ResponseEntity
                       .status(HttpStatus.CREATED)
                       .body(converter.toAspspTO(bo));
    }

    @ApiOperation("Check for duplicates")
    @PostMapping("/validate")
    public ResponseEntity<Boolean> checkNewAspsp(@RequestBody AspspTO aspsp) {
        logger.info("Check ASPSP for duplicates={}", aspsp);
        Boolean result = aspspService.hasDuplicate(converter.toAspspBO(aspsp));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Update ASPSP")
    @PutMapping
    public ResponseEntity update(@RequestBody AspspTO aspsp) {
        logger.info("Update ASPSP={}", aspsp);

        AspspBO bo = converter.toAspspBO(aspsp);
        aspspService.save(bo);

        return ResponseEntity
                       .accepted()
                       .build();
    }

    @PreAuthorize("hasAnyRole('MANAGER','DEPLOYER')")
    @ApiOperation("Delete ASPSP")
    @DeleteMapping("/{aspspId}")
    public ResponseEntity deleteById(@PathVariable(("aspspId")) UUID id) {
        logger.info("Delete ASPSP by id={}", id);

        aspspService.deleteById(id);

        return ResponseEntity
                       .noContent()
                       .build();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Count all ASPSPs")
    @GetMapping("/count")
    public ResponseEntity count() {
        logger.info("Count all records in the database");

        return ResponseEntity.status(HttpStatus.OK).body(aspspService.count());
    }
}
