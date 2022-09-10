package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.usecases;

import static java.lang.Thread.sleep;

import java.util.Properties;

import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities.MisterPlayerReport;
import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.helpers.HttpHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@ExtendWith(MockitoExtension.class)
class MisterPlayerReportUseCaseTest {

  private static final String MISTER_URL = "https://mister.mundodeportivo.com";

  private final String MARKET_URL = "https://mister.mundodeportivo.com/market";

  private final String PLAYER_BASE_URL = "https://mister.mundodeportivo.com/feed#players";

  private final String LOGIN_URL = "https://mister.mundodeportivo.com/api2/auth/signin/email";

  private final String OWNER_USER = "Sama";

  private final int MAX_TRIES = 3;

  private final int RETRY_TIME_IN_MS = 200;

  @SneakyThrows
  @Test
  public void testMisterBuyableReport() {
    HttpHelper httpHelper = new HttpHelper(MAX_TRIES, RETRY_TIME_IN_MS);
    MisterPlayerInfoExtractorUseCase misterPlayerInfoExtractorUseCase =
        new MisterPlayerInfoExtractorUseCase(MISTER_URL, LOGIN_URL, MARKET_URL, PLAYER_BASE_URL, httpHelper);

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);

    mailSender.setUsername("davidautentico@gmail.com");
    mailSender.setPassword("gadpkvenunvhypub");

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    MisterPlayerReportUseCase misterPlayerBuyablePlayersUseCase =
        new MisterPlayerReportUseCase(misterPlayerInfoExtractorUseCase, mailSender, OWNER_USER);

    int numTries = 120;
    int tries = 0;
    int lastHashCode = -1;
    while (tries < numTries) {
      MisterPlayerReport misterPlayerReport = misterPlayerBuyablePlayersUseCase.dispatch();
      int hashCode = misterPlayerReport.hashCode();
      if (hashCode != lastHashCode) {
        // TEST SEND EMAIL
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("davidautentico@gmail.com");

        msg.setSubject("Mister Report");

        msg.setText(misterPlayerReport.printReport() + " \n hashCode=" + hashCode);

        mailSender.send(msg);

        lastHashCode = hashCode;
      }
      sleep(15 * 60 * 1000);
      tries++;
    }
  }
}