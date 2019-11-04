package de.adorsys.registry.manager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AspspAdapterService {

    /**
     * Receives an array of bytes and transfers it for further saving into the
     * existing database.
     *
     * @param data as an array of bytes
     */
    void importData(byte[] data);

    /**
     * Gets the data from the existing database and makes the {@link MultipartFile}
     * from it for further transfer into Adapter
     * <p>
     * Obtains all records from the database as an array of bytes via
     * {@link AspspCsvService} and builds MultipartFile based on that data.
     *
     * @return MultipartFile with all records from the current repository
     * @throws IOException if copying data into {@link java.io.OutputStream} fails
     */
    MultipartFile exportData() throws IOException;
}
