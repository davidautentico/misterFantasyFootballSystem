package com.drosa.misterfantasysystem.domain.repositories;

public interface ConfigurationRepository {

  String getOwner();

  int getMaxRetries();

  int getRetryTimeInMs();

  String getMisterBaseUrl();

  String getMisterLoginUrl();

  String getMisterMarketUrl();

  int getNumberOfDfs();

  int getNumberOfMfs();

  int getNumberOfFws();

  int getNumberOfTeams();

  int getNumberOfIters();
}
