package com.drosa.misterfantasysystem.domain.usecases;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerInfo;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerReport;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerTop;
import com.drosa.misterfantasysystem.domain.entities.MisterTeam;
import com.drosa.misterfantasysystem.domain.enums.MarketRecommendation;
import com.drosa.misterfantasysystem.domain.enums.PlayerPosition;
import com.drosa.misterfantasysystem.domain.helpers.MisterBestTeamHelper;
import com.drosa.misterfantasysystem.domain.helpers.MisterPlayerHelper;
import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import com.drosa.misterfantasysystem.domain.repositories.MisterReaderRepository;
import com.drosa.misterfantasysystem.domain.services.MisterPlayerTeamChooser;
import com.drosa.misterfantasysystem.domain.services.MisterPlayerUserBestTeam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Provide list of good player to buy from the market or others players different from the owner.
 */
@Component
@Slf4j
public class MisterPlayerReportUseCase {

  private final MisterReaderRepository misterReaderRepository;

  private final ConfigurationRepository configurationRepository;

  private final MisterPlayerTeamChooser misterPlayerTeamChooser;

  private final MisterBestTeamHelper misterBestTeamHelper;

  private final MisterPlayerHelper misterPlayerHelper;

  private final MisterPlayerUserBestTeam misterPlayerUserBestTeam;

  public MisterPlayerReportUseCase(MisterReaderRepository misterReaderRepository,
      ConfigurationRepository configurationRepository,
      MisterPlayerTeamChooser misterPlayerTeamChooser,
      MisterBestTeamHelper misterBestTeamHelper, MisterPlayerHelper misterPlayerHelper,
      MisterPlayerUserBestTeam misterPlayerUserBestTeam) {
    this.misterReaderRepository = misterReaderRepository;
    this.configurationRepository = configurationRepository;
    this.misterPlayerTeamChooser = misterPlayerTeamChooser;
    this.misterBestTeamHelper = misterBestTeamHelper;
    this.misterPlayerHelper = misterPlayerHelper;
    this.misterPlayerUserBestTeam = misterPlayerUserBestTeam;
  }

  public Optional<MisterPlayerReport> dispatch() {

    final String ownerUser = configurationRepository.getOwner();

    // obtención de la información de los jugadores
    Optional<MisterPlayerInfo> misterPlayerInfo = Optional.ofNullable(misterReaderRepository.getPlayerInfo());

    if (misterPlayerInfo.isEmpty()) {
      return Optional.empty();
    }

    // 1. Find the actual player list
    List<MisterPlayer> actualPlayerList =
        misterPlayerInfo.get().getPlayers().stream().filter(Objects::nonNull).collect(
            Collectors.toList());

    // 2.- print sort by 21/22 stats
    System.out.println("**** Sorted BY 21/22***");
    List<MisterPlayer> players2122 = actualPlayerList.stream()
        .filter(misterPlayer -> misterPlayer.getTotalPoints2122() > 90)
        .sorted(Comparator.comparing(MisterPlayer::getPosition).thenComparing(MisterPlayer::getTotalPoints2122).reversed())
        .collect(Collectors.toList());

    players2122.forEach(player -> {
      System.out.println(player.getName()
          + " " + player.getSurname() + ";" + player.getTeamRef() + ";" + player.getPosition() + ";" + player.getTotalPoints2122());
    });
    System.out.println("**** End 21/22");

    // elaboración del informe
    // User best team
    List<MisterTeam> bestTeams = misterPlayerUserBestTeam.dispatch(actualPlayerList);

    // 2. Extract metrics from owner team
    List<MisterPlayer> ownerPlayers = actualPlayerList.stream().filter(player -> player.getOwner().equalsIgnoreCase(ownerUser))
        .sorted(Comparator.comparing(MisterPlayer::getPosition))
        .collect(Collectors.toList());

    List<MisterPlayer> ownerDFs = ownerPlayers.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.DF))
        .sorted(Comparator.comparing(MisterPlayer::getPosition).thenComparing(MisterPlayer::getTotalPoints).reversed())
        .limit(3).collect(Collectors.toList());
    int lastDfIndex = ownerDFs.size() > 2 ? 2 : ownerDFs.size() - 1;
    int ownerdfPoints = ownerDFs.get(lastDfIndex).getTotalPoints();
    double ownerdfPerformance = ownerDFs.get(lastDfIndex).getTotalPoints() * 1.0 / (ownerDFs.get(lastDfIndex).getValue() * 1.0 / 1000000);

    List<MisterPlayer> ownerMFs = ownerPlayers.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.MF))
        .sorted(Comparator.comparing(MisterPlayer::getPosition).thenComparing(MisterPlayer::getTotalPoints).reversed())
        .limit(4).collect(Collectors.toList());
    int lastMfIndex = ownerMFs.size() > 3 ? 3 : ownerMFs.size() - 1;
    int ownermfPoints = ownerMFs.get(lastMfIndex).getTotalPoints();
    double ownermfPerformance = ownerMFs.get(lastMfIndex).getTotalPoints() * 1.0 / (ownerMFs.get(lastMfIndex).getValue() * 1.0 / 1000000);

    List<MisterPlayer> ownerFWs = ownerPlayers.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.FW))
        .sorted(Comparator.comparing(MisterPlayer::getPosition).thenComparing(MisterPlayer::getTotalPoints).reversed())
        .limit(3).collect(Collectors.toList());
    int lastFWIndex = ownerFWs.size() > 2 ? 2 : ownerFWs.size() - 1;
    int ownerfwPoints = ownerFWs.get(lastFWIndex).getTotalPoints();
    double ownerfwPerformance = ownerFWs.get(lastFWIndex).getTotalPoints() * 1.0 / (ownerFWs.get(lastFWIndex).getValue() * 1.0 / 1000000);

    MisterTeam ownerBestTeam = misterBestTeamHelper.getBestTeamStrategic(ownerPlayers);
    int totalOwnerValue = ownerPlayers.stream().map(MisterPlayer::getValue).reduce(0L, Long::sum).intValue();
    int totalOwnerPoints = ownerBestTeam.getTeamPoints();

    List<MisterTeam> misterTeamList = misterPlayerTeamChooser.dispatch(actualPlayerList, 81000000, ownerUser);

    System.out.println("*** OWNER PLAYERS ***");
    ownerPlayers.forEach(player -> {
      System.out.println(player.printStats());
    });

    List<MisterPlayer> buyablePlayers = actualPlayerList.stream().filter(player ->
            player.isInMarket() || misterPlayerHelper.playerNotOwner(player, ownerUser))
        .map(player -> {
          MarketRecommendation marketRecommendation = MarketRecommendation.NONE;
          int playerPoints = player.getTotalPoints();
          double playerPerformance = misterPlayerHelper.getPerformance(player, ownerUser);

          if (player.getPosition() == PlayerPosition.DF) {
            if (playerPoints >= 0.8 * ownerdfPoints) {
              if (playerPoints >= ownerdfPoints && playerPerformance >= ownerdfPerformance) {
                marketRecommendation = MarketRecommendation.IMPORTANT_BUY;
              } else if (playerPoints > ownerdfPoints || playerPerformance > ownerdfPerformance) {
                marketRecommendation = MarketRecommendation.NORMAL_BUY;
              }
            }
          } else if (player.getPosition() == PlayerPosition.MF) {
            if (playerPoints >= 0.8 * ownermfPoints) {
              if (playerPoints >= ownermfPoints && playerPerformance >= ownermfPerformance) {
                marketRecommendation = MarketRecommendation.IMPORTANT_BUY;
              } else if (playerPoints > ownermfPoints || playerPerformance > ownermfPerformance) {
                marketRecommendation = MarketRecommendation.NORMAL_BUY;
              }
            }
          } else if (player.getPosition() == PlayerPosition.FW) {
            if (playerPoints >= 0.8 * ownerfwPoints) {
              if (playerPoints >= ownerfwPoints && playerPerformance >= ownerfwPerformance) {
                marketRecommendation = MarketRecommendation.IMPORTANT_BUY;
              } else if (playerPoints > ownerfwPoints || playerPerformance > ownerfwPerformance) {
                marketRecommendation = MarketRecommendation.NORMAL_BUY;
              }
            }
          }
          return player.toBuilder().marketRecommendation(marketRecommendation).build();
        })
        .filter(misterPlayer -> misterPlayer.getMarketRecommendation() == MarketRecommendation.IMPORTANT_BUY
            || misterPlayer.getMarketRecommendation() == MarketRecommendation.NORMAL_BUY && !misterPlayerHelper.isPlayer(misterPlayer))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .collect(Collectors.toList());

    System.out.println("*** NON FREE MARKET PLAYERS ***");

    buyablePlayers.forEach(player -> {
      System.out.println(player.printStats());
    });

    // 3. Extract on-sale players or from other players
    List<MisterPlayer> purchablePlayers = actualPlayerList.stream().filter(player ->
        (player.isInMarket() && !player.getOwner().equalsIgnoreCase(ownerUser)) || (!player.getOwner().equalsIgnoreCase(ownerUser)
            && !ObjectUtils.isEmpty(player.getOwner()))
    ).collect(Collectors.toList());

    // 4. Extract elected players from 3 vs 2
    List<MisterPlayer> elected = purchablePlayers.stream().filter(player -> {
      double performance = player.getTotalPoints() * 1.0 / (player.getClause() * 1.0 / 1000000);

      return Boolean.TRUE;
    }).collect(Collectors.toList());

    // COLLECT STATS
    List<MisterPlayer> gkPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.GK))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(6)
        .collect(Collectors.toList());
    double gkAvgPoints = gkPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double gkPerformance =
        gkPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> dfPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.DF))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(18)
        .collect(Collectors.toList());
    double dfAvgPoints = dfPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double dfPerformance =
        dfPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> mfPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.MF))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(24)
        .collect(Collectors.toList());
    double mfAvgPoints = mfPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double mfPerformance =
        mfPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> fwPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.FW))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(18)
        .collect(Collectors.toList());
    double fwAvgPoints = fwPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double fwPerformance =
        fwPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    MisterPlayerTop gkMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.GK)
        .numberOfPlayers(6)
        .avgPoints(gkAvgPoints)
        .avgPerformance(gkPerformance)
        .build();

    MisterPlayerTop dfMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.DF)
        .numberOfPlayers(18)
        .avgPoints(dfAvgPoints)
        .avgPerformance(dfPerformance)
        .build();

    MisterPlayerTop mfMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.MF)
        .numberOfPlayers(24)
        .avgPoints(mfAvgPoints)
        .avgPerformance(mfPerformance)
        .build();

    MisterPlayerTop fwMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.FW)
        .numberOfPlayers(18)
        .avgPoints(fwAvgPoints)
        .avgPerformance(fwPerformance)
        .build();

    return Optional.of(MisterPlayerReport.builder()
        .allUserBestTeam(bestTeams)
        .totalOwnerPoints(totalOwnerPoints)
        .totalOwnerValue(totalOwnerValue)
        .misterTeamBestTeams(misterTeamList)
        .misterPlayerInfo(misterPlayerInfo.get())
        .marketPlayers(buyablePlayers)
        .ownerPlayers(ownerPlayers)
        .gkTop(gkMisterPlayerTop)
        .dfTop(dfMisterPlayerTop)
        .mfTop(mfMisterPlayerTop)
        .fwTop(fwMisterPlayerTop)
        .build());

  }


}
