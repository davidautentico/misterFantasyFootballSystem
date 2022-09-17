package com.drosa.misterfantasysystem.boot.watchdog;

import static java.lang.Thread.sleep;

import java.util.List;
import java.util.Properties;

import com.drosa.misterfantasysystem.boot.repositories.HelperConfigRepository;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerReport;
import com.drosa.misterfantasysystem.domain.entities.MisterTeam;
import com.drosa.misterfantasysystem.domain.usecases.MisterPlayerReportUseCase;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MisterWatchDogProcess implements
    ApplicationListener<ContextRefreshedEvent> {

  private final HelperConfigRepository helperConfigRepository;

  private final MisterPlayerReportUseCase misterPlayerReportUseCase;

  public MisterWatchDogProcess(HelperConfigRepository helperConfigRepository,
      MisterPlayerReportUseCase misterPlayerReportUseCase) {
    this.helperConfigRepository = helperConfigRepository;
    this.misterPlayerReportUseCase = misterPlayerReportUseCase;
  }

  @Override
  @SneakyThrows
  public void onApplicationEvent(ContextRefreshedEvent event) {

    log.info("Comenzando el watchdog...");

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

    int numTries = 120;
    int tries = 0;
    int lastHashCode = -1;
    MisterTeam bestTeam = MisterTeam.builder().teamPoints(0).build();
    while (tries < numTries) {
      MisterPlayerReport misterPlayerReport = misterPlayerReportUseCase.dispatch();

      boolean hasToUpdated = false;
      if (misterPlayerReport.getMisterTeamBestTeams().size() > 0) {
        hasToUpdated = misterPlayerReport.getMisterTeamBestTeams().get(0).getTeamPoints() > bestTeam.getTeamPoints();
      }

      int hashCode = misterPlayerReport.hashCode();
      if (hashCode != lastHashCode || hasToUpdated) {
        if (hasToUpdated) {
          bestTeam = misterPlayerReport.getMisterTeamBestTeams().get(0).toBuilder().build();
        }
        MisterPlayerReport actualMisterPlayerReport = misterPlayerReport.toBuilder().misterTeamBestTeams(List.of(bestTeam)).build();
        // TEST SEND EMAIL
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("davidautentico@gmail.com");

        msg.setSubject("Mister Report, best team found: " + hasToUpdated);

        msg.setText(actualMisterPlayerReport.printReport() + " \n hashCode=" + hashCode);

        mailSender.send(msg);

        lastHashCode = hashCode;
      }
      sleep(5 * 60 * 1000);
      tries++;
    }
  }

}
