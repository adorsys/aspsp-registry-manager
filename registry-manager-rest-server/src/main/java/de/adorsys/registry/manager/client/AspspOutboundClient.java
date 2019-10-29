package de.adorsys.registry.manager.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "aspspoutboundservice", url = "http://localhost:8999/v1/aspsps/csv")
public interface AspspOutboundClient {

    @RequestLine("POST /import")
    @Headers("Content-Type: multipart/form-data")
    MultipartFile importFile(@Param("file") MultipartFile file);

    @RequestLine("GET /export")
    @Headers("Content-Type: text/csv")
    byte[] exportFile(@Param("file") byte[] file);
}
