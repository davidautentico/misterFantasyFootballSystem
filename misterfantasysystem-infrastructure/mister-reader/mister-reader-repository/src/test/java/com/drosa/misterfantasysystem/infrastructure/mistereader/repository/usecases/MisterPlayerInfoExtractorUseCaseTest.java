package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.usecases;

import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.helpers.HttpHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MisterPlayerInfoExtractorUseCaseTest {

  private static final String MISTER_URL = "https://mister.mundodeportivo.com";

  private final String MARKET_URL = "https://mister.mundodeportivo.com/market";

  private final String PLAYER_BASE_URL = "https://mister.mundodeportivo.com/feed#players";

  private final String LOGIN_URL = "https://mister.mundodeportivo.com/api2/auth/signin/email";

  private final int MAX_TRIES = 3;

  private final int RETRY_TIME_IN_MS = 200;

  @Test
  public void testMisterConnectivity() {
    HttpHelper httpHelper = new HttpHelper(MAX_TRIES, RETRY_TIME_IN_MS);
    MisterPlayerInfoExtractorUseCase misterPlayerInfoExtractorUseCase =
        new MisterPlayerInfoExtractorUseCase(MISTER_URL, LOGIN_URL, MARKET_URL, PLAYER_BASE_URL, httpHelper);

    misterPlayerInfoExtractorUseCase.dispatch();
  }
}