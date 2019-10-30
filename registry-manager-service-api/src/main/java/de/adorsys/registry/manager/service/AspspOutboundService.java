package de.adorsys.registry.manager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AspspOutboundService {

    void importData(byte[] data);

    MultipartFile exportData() throws IOException;
}
