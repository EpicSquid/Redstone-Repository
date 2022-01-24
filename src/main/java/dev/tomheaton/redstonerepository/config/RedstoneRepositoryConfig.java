package dev.tomheaton.redstonerepository.config;

import dev.tomheaton.redstonerepository.RedstoneRepository;

public class RedstoneRepositoryConfig {

    public static void register() {
        RedstoneRepository.LOGGER.info("config register");
    }
}
