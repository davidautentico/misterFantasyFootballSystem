package com.drosa.misterfantasysystem.boot.repositories;

import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HelperConfigRepositoryImpl implements  HelperConfigRepository{

  private final ConfigurationRepository configurationRepository;

  public HelperConfigRepositoryImpl(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  @Override
  public int getMaxRetries() {
    return configurationRepository.getMaxRetries();
  }

  @Override
  public int getRetryTimeInMs() {
    return configurationRepository.getRetryTimeInMs();
  }
}
