package de.adorsys.registry.manager.service;

import java.io.File;

public interface AspspOutboundService {

    File importFile(File file);

    byte[] exportFile();
}
