package com.drosa.misterfantasysystem.boot.repositories;

public interface HelperConfigRepository {

  int getMaxRetries();

  int getRetryTimeInMs();

}
