package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.repositories;

import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MisterReaderConfigRepositoryImpl implements MisterReaderConfigRepository {

  private final ConfigurationRepository configurationRepository;

  public MisterReaderConfigRepositoryImpl(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  @Override
  public String getMisterBaseUrl() {
    return configurationRepository.getMisterBaseUrl();
  }

  @Override
  public String getMisterLoginUrl() {
    return configurationRepository.getMisterLoginUrl();
  }

  @Override
  public String getMisterMarketUrl() {
    return configurationRepository.getMisterMarketUrl();
  }
}
