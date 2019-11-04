package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.service.AspspCsvService;
import de.adorsys.registry.manager.service.AspspAdapterService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AspspAdapterServiceImpl implements AspspAdapterService {

    private AspspCsvService aspspCsvService;

    public AspspAdapterServiceImpl(AspspCsvService aspspCsvService) {
        this.aspspCsvService = aspspCsvService;
    }

    @Override
    public void importData(byte[] data) {
        aspspCsvService.importCsv(data);
    }

    @Override
    public MultipartFile exportData() throws IOException {
        Path file = Files.createTempFile("tmp", "transfer");
        int fileSize = Integer.parseInt(file.toFile().length() + "");
        Files.write(file, aspspCsvService.exportCsv());

        FileItem fileItem = new DiskFileItem("file", "multipart/form-data", true, file.toFile().getName(), fileSize, file.toFile().getParentFile());
        IOUtils.copy(Files.newInputStream(file), fileItem.getOutputStream());

        Files.deleteIfExists(file);
        return new CommonsMultipartFile(fileItem);
    }
}
