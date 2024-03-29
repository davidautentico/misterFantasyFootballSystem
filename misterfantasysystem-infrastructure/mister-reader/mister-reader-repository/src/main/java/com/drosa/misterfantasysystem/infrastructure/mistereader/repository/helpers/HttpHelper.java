package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.helpers;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.util.Map;

import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class HttpHelper {

  private final ConfigurationRepository configurationRepository;

  @SneakyThrows
  public Document connect(String url, Map<String, String> cookies) {
    Document doc = null;
    int numTries = 0;

    while ((doc == null) && (numTries < configurationRepository.getMaxRetries())) {
      doc = internalConnect(url, cookies);
      numTries++;
      sleep(configurationRepository.getRetryTimeInMs());
    }

    if (doc==null){
      log.error("Max tries exceeded connecting to {}",url);
    }
    return doc;
  }

  private Document internalConnect(String url, Map<String, String> cookies) {
    Document doc = null;
    try {
      doc = Jsoup.connect(url).cookies(cookies).get();
    } catch (IOException e) {
      return null;
    }

    return doc;
  }
}
