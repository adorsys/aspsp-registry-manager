package de.adorsys.registry.manager.service;

public interface AspspCsvService {

    byte[] exportCsv();

    void importCsv(byte[] file);
}
