package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.usecases;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities.MisterPlayer;
import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities.MisterPlayerInfo;
import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.enums.PlayerPosition;
import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.enums.TeamRef;
import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.helpers.HttpHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class MisterPlayerInfoExtractorUseCase {

  @Value("${mister.base-url:https://mister.mundodeportivo.com/}")
  private final String misterBaseUrl;

  @Value("${mister.login-url:https://mister.mundodeportivo.com/api2/auth/signin/email}")
  private final String misterLoginUrl;

  @Value("${mister.market-url:https://mister.mundodeportivo.com/market}")
  private final String misterMarketUrl;

  @Value("${mister.players-url:https://mister.mundodeportivo.com/feed#players}")
  private final String misterBasePlayerUrl;

  private final HttpHelper httpHelper;

  /**
   * Extract player stats: value, clause, points, position, owner, isInMarket
   */
  public MisterPlayerInfo dispatch() {

    List<MisterPlayer> playerList = null;
    int gameWeek = 0;

    try {

      // Login json
      JsonObject gsonObject = new JsonObject();
      gsonObject.addProperty("method", "email");
      gsonObject.addProperty("email", "davidautentico@gmail.com");
      gsonObject.addProperty("password", "Mister.2508");
      String payload = new Gson().toJson(gsonObject);

      Document loginDocument = Jsoup.connect(misterLoginUrl)
          .userAgent("Chrome")
          .header("content-type", "application/json")
          .header("accept", "application/json")
          .requestBody(payload)
          .ignoreContentType(true)
          .post();

      Map<String, String> cookies = loginDocument.connection().response().cookies();

      // Get players in Market
      Set<String> playerHRefInMarket = getPlayerHrefInMarket(cookies);

      // Extract player stats
      System.out.println("Name;Position;Value;Clause;TotalPoints;Performance;Owner;InMarket");
      playerList = TeamRef.stream().flatMap(teamRef -> {
        String teamUrl = misterBaseUrl + "/" + teamRef.getValue();
        Document teamDoc = httpHelper.connect(teamUrl, cookies);

        if (teamDoc != null) {
          Elements panelTeams = teamDoc.getElementsByClass("panel panel-team");
          if (panelTeams.size() == 1) {
            Element team = panelTeams.get(0);
            Elements playerLists = team.getElementsByClass("player-list");
            if (playerLists.size() > 0) {
              List<MisterPlayer> players = getPlayersFromPlayerList(playerLists.get(0), playerHRefInMarket, cookies);
              if (players!=null) return players.stream();
              else return Stream.empty();
            }
          }
        }
        return null;
      }).collect(Collectors.toList());
    } catch (IOException e) {
      log.error("Error loading page. Message {}", e.getMessage());
      e.printStackTrace();
    }
    return MisterPlayerInfo.builder()
        .players(playerList)
        .createdAt(Instant.now())
        .gameWeek(gameWeek)
        .build();
  }

  private Set<String> getPlayerHrefInMarket(Map<String, String> cookies) {

    Document marketDoc = null;
    try {
      marketDoc = Jsoup.connect(misterMarketUrl)
          .cookies(cookies).get();

      //btn btn-sw-link player
      Elements playerRefs = marketDoc.getElementsByClass("btn btn-sw-link player");
      return playerRefs.stream().map(element -> element.attributes().get("href")).collect(Collectors.toSet());
    } catch (IOException e) {
      log.error("Error connecting to market page {}. Message {}", misterMarketUrl, e.getMessage());
      e.printStackTrace();
    }

    return new HashSet<>();
  }

  @SneakyThrows
  private List<MisterPlayer> getPlayersFromPlayerList(Element playerListElement, Set<String> playerHRefInMarket,
      Map<String, String> cookies) {
    Elements playerElements = playerListElement.getElementsByClass("player-row");
    return playerElements.stream().map(element -> {
          Elements playerButtons = element.getElementsByClass("btn btn-sw-link player");
          if (playerButtons.size() > 0) {
            boolean isInMarket = false;
            String playerHref = playerButtons.get(0).attributes().get("href");

            if (playerHRefInMarket.contains(playerHref)) {
              isInMarket = true;
            }

            try {
              sleep(200);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            MisterPlayer misterPlayer = getPlayerFromHref(playerHref, isInMarket, cookies);
            if (misterPlayer == null) {
              return null;
            }
            double performance = misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getClause() * 1.0 / 1000000);
            System.out.println(
                misterPlayer.getName() + " " + misterPlayer.getSurname()
                    + "; " + misterPlayer.getPosition()
                    + "; " + misterPlayer.getValue() / 1000
                    + "; " + misterPlayer.getClause() / 1000
                    + ";" + misterPlayer.getTotalPoints()
                    + ";" + String.format("%,.2f", performance)//.replace(',', '.')
                    + ";" + misterPlayer.getOwner()
                    + ";" + misterPlayer.isInMarket()
            );
            return misterPlayer;
          }
          return null;
        }
    ).collect(Collectors.toList());
  }

  private MisterPlayer getPlayerFromHref(String playerHref, boolean isInMarket, Map<String, String> cookies) {

    String playerUrl = misterBaseUrl + "/" + playerHref;
    Document playerDoc = null;
    playerDoc = httpHelper.connect(playerUrl, cookies);
    if (playerDoc == null) return null;
    // position
    PlayerPosition playerPosition = PlayerPosition.NONE;
    Elements positionElements = playerDoc.getElementsByClass("pos-2 pos-big");
    if (positionElements.size() > 0) {
      playerPosition = PlayerPosition.DF;
    } else {
      positionElements = playerDoc.getElementsByClass("pos-3 pos-big");
      if (positionElements.size() > 0) {
        playerPosition = PlayerPosition.MF;
      } else {
        positionElements = playerDoc.getElementsByClass("pos-4 pos-big");
        if (positionElements.size() > 0) {
          playerPosition = PlayerPosition.FW;
        } else {
          playerPosition = PlayerPosition.GK;
        }
      }
    }
    // owner wrapper sw-profile
    String ownerStr = "";
    Elements wrapperSwProfile = playerDoc.getElementsByClass("wrapper sw-profile");
    if (wrapperSwProfile.size() > 0) {
      Elements boxes = wrapperSwProfile.get(0).getElementsByClass("box box-owner");
      if (boxes.size() > 0) {
        Element ownerBox = boxes.get(0);
        if (ownerBox.childNodes().size() > 2) {
          TextNode ownerNode = (TextNode) ownerBox.childNodes().get(2).childNodes().get(1).childNodes().get(0);
          ownerStr = ownerNode.text();
        }

      }
    }
    // name
    Elements playerInfos = playerDoc.getElementsByClass("player-info");

    if (playerInfos.size()==0) return null;

    String nameStr = "";
    String surnameStr = "";

    Elements names = playerInfos.get(0).getElementsByClass("name");
    if (names.size() > 0) {
      Element name = names.get(0);
      if (name.childNodes().size() > 0) {
        TextNode nameNode = (TextNode) name.childNodes().get(0);
        nameStr = nameNode.text();
      }
    }

    Elements surnames = playerInfos.get(0).getElementsByClass("surname");
    if (surnames.size() > 0) {
      Element surname = surnames.get(0);
      if (surname.childNodes().size() > 0) {
        TextNode surnameNode = (TextNode) surname.childNodes().get(0);
        surnameStr = surnameNode.text();
      }
    }

    // stats
    AtomicInteger totalPoints = new AtomicInteger();
    AtomicLong playerValue = new AtomicLong();
    AtomicLong playerClause = new AtomicLong();
    playerClause.set(-1);
    Elements playerStatsElements = playerDoc.getElementsByClass("player-stats-wrapper");
    if (playerStatsElements.size() > 0) {
      Element playerStatsElement = playerStatsElements.get(0);
      Elements items = playerStatsElement.getElementsByClass("item");
      items.forEach(
          item -> {
            // POINTS
            Elements labels = item.getElementsByClass("label");
            Elements values = item.getElementsByClass("value");

            TextNode label = (TextNode) labels.get(0).childNodes().get(0);
            TextNode value = (TextNode) values.get(0).childNodes().get(0);

            if (label.text().equalsIgnoreCase("POINTS")) {
              totalPoints.set(Integer.parseInt(value.text()));
            } else if (label.text().equalsIgnoreCase("VALUE")) {
              playerValue.set(Long.parseLong(value.text().replace(",", "")));
            } else if (label.text().equalsIgnoreCase("CLAUSE")) {
              playerClause.set(Long.parseLong(value.text().replace(",", "")));
            }

          }
      );
    }

    return MisterPlayer.builder()
        .href(playerHref)
        .owner(ownerStr)
        .name(nameStr)
        .surname(surnameStr)
        .position(playerPosition)
        .totalPoints(totalPoints.get())
        .value(playerValue.get())
        .clause(playerClause.get() > 0 ? playerClause.get() : playerValue.get())
        .isInMarket(isInMarket)
        .build();

  }
}
