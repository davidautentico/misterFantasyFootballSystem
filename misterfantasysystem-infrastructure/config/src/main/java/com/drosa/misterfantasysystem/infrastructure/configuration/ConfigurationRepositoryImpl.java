package com.drosa.misterfantasysystem.infrastructure.configuration;

import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

  @Value("${mister.owner:Sama}")
  private String owner;

  @Value("${mister.base-url:https://mister.mundodeportivo.com/}")
  private String misterBaseUrl;

  @Value("${mister.login-url:https://mister.mundodeportivo.com/api2/auth/email?state=eyJwbGF0Zm9ybSI6IndlYiIsInVzZXJBZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMTMuMC4wLjAgU2FmYXJpLzUzNy4zNiIsImFwcHNmbHllcklkIjpudWxsLCJhcHBzZmx5ZXJDb252ZXJzaW9uRGF0YSI6eyJpbnN0YWxsX3RpbWUiOiIyMDIwLTA5LTAxIDA5OjQ2OjIzLjE0MiIsImFmX3N0YXR1cyI6Ik9yZ2FuaWMiLCJhZl9tZXNzYWdlIjoib3JnYW5pYyBpbnN0YWxsIiwiaXNfZmlyc3RfbGF1bmNoIjpmYWxzZX19}")
  private String misterLoginUrl;

  @Value("${mister.market-url:https://mister.mundodeportivo.com/market}")
  private String misterMarketUrl;

  @Value("${mister.http.max-tries:3}")
  private int maxTries;

  @Value("${mister.http.retry-time-in-ms:75}")
  private int retryTimeInMS;

  @Value("${mister.chooser.number-dfs:3}")
  private int numberOfDfs;

  @Value("${mister.chooser.number-mfs:4}")
  private int numberOfMfs;

  @Value("${mister.chooser.number-fws:3}")
  private int numberOfFws;

  @Value("${mister.chooser.number-teams:1}")
  private int numberOfTeams;

  @Value("${mister.chooser.number-iters:400}")
  private int numberOfIters;

  @Override
  public String getOwner() {
    return owner;
  }

  @Override
  public int getMaxRetries() {
    return maxTries;
  }

  @Override
  public int getRetryTimeInMs() {
    return retryTimeInMS;
  }

  @Override
  public String getMisterBaseUrl() {
    return misterBaseUrl;
  }

  @Override
  public String getMisterLoginUrl() {
    return misterLoginUrl;
  }

  @Override
  public String getMisterMarketUrl() {
    return misterMarketUrl;
  }

  @Override
  public int getNumberOfDfs() {
    return numberOfDfs;
  }

  @Override
  public int getNumberOfMfs() {
    return numberOfMfs;
  }

  @Override
  public int getNumberOfFws() {
    return numberOfFws;
  }

  @Override
  public int getNumberOfTeams() {
    return numberOfTeams;
  }

  @Override
  public int getNumberOfIters() {
    return numberOfIters;
  }
}
