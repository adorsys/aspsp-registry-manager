package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.converter.AspspTOConverter;
import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.model.AspspBO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/aspsps")
public class AspspResource {

    private static final Logger logger = LoggerFactory.getLogger(AspspResource.class);

    private final AspspService aspspService;
    private final AspspTOConverter converter;

    public AspspResource(AspspService aspspService, AspspTOConverter converter) {
        this.aspspService = aspspService;
        this.converter = converter;
    }

    @ApiOperation("Get ASPSPs")
    @GetMapping
    ResponseEntity<List<AspspTO>> getAspsps(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "bic", required = false) String bic,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam(value = "iban", required = false) String iban, // if present - other params ignored
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.info("Get all ASPSPs");

        List<AspspBO> aspspBOs;

        if (iban != null && !iban.isEmpty()) {
            aspspBOs = aspspService.getByIban(iban, page, size);
        } else {
            aspspBOs = aspspService.getByAspsp(buildAspspBO(name, bic, bankCode), page, size);
        }

        return ResponseEntity
                       .status(HttpStatus.OK)
                       .body(converter.toAspspTOList(aspspBOs));
    }

    private AspspBO buildAspspBO(String name, String bic, String bankCode) {
        AspspBO bo = new AspspBO();

        bo.setName(name);
        bo.setBic(bic);
        bo.setBankCode(bankCode);

        return bo;
    }

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

    @ApiOperation("Delete ASPSP")
    @DeleteMapping("/{aspspId}")
    public ResponseEntity deleteById(@PathVariable(("aspspId")) UUID id) {
        logger.info("Delete ASPSP by id={}", id);

        aspspService.deleteById(id);

        return ResponseEntity
                       .noContent()
                       .build();
    }
}
