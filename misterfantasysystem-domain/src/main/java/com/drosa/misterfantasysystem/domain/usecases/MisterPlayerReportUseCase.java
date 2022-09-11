package com.drosa.misterfantasysystem.domain.usecases;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayerReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Provide list of good player to buy from the market or others players different from the owner.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class MisterPlayerReportUseCase {

  public final MisterPlayerInfoExtractorUseCase misterPlayerInfoExtractorUseCase;

  //public final JavaMailSenderImpl mailSender;

  public final String ownerUser;

  public MisterPlayerReport dispatch() {
    return MisterPlayerReport.builder().build();
  }

  /*public MisterPlayerReport dispatch() {
    // 1. Find the actual player list
    List<MisterPlayer> actualPlayerList =
        misterPlayerInfoExtractorUseCase.dispatch().getPlayers().stream().filter(Objects::nonNull).collect(
            Collectors.toList());

    // 2. Extract metrics from owner team
    List<MisterPlayer> ownerPlayers = actualPlayerList.stream().filter(player -> player.getOwner().equalsIgnoreCase(ownerUser))
        .sorted(Comparator.comparing(MisterPlayer::getPosition))
        .collect(Collectors.toList());

    System.out.println("*** OWNER PLAYERS ***");
    ownerPlayers.forEach(player -> {
      System.out.println(player.printStats());
    });

    List<MisterPlayer> marketPlayers = actualPlayerList.stream().filter(player ->
            player.isInMarket() && !StringUtils.hasLength(player.getOwner()))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .collect(Collectors.toList());

    System.out.println("*** NON FREE MARKET PLAYERS ***");

    marketPlayers.forEach(player -> {
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
        .limit(10)
        .collect(Collectors.toList());
    double gkAvgPoints = gkPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double gkPerformance =
        gkPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> dfPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.DF))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(40)
        .collect(Collectors.toList());
    double dfAvgPoints = dfPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double dfPerformance =
        dfPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> mfPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.MF))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(40)
        .collect(Collectors.toList());
    double mfAvgPoints = mfPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double mfPerformance =
        mfPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    List<MisterPlayer> fwPlayers = actualPlayerList.stream()
        .filter(player -> player.getPosition().equals(PlayerPosition.FW))
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(20)
        .collect(Collectors.toList());
    double fwAvgPoints = fwPlayers.stream().mapToDouble(MisterPlayer::getTotalPoints).average().getAsDouble();
    double fwPerformance =
        fwPlayers.stream().mapToDouble(misterPlayer -> misterPlayer.getTotalPoints() * 1.0 / (misterPlayer.getValue() * 1.0 / 1000000))
            .average().getAsDouble();

    MisterPlayerTop gkMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.GK)
        .numberOfPlayers(10)
        .avgPoints(gkAvgPoints)
        .avgPerformance(gkPerformance)
        .build();

    MisterPlayerTop dfMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.DF)
        .numberOfPlayers(40)
        .avgPoints(dfAvgPoints)
        .avgPerformance(dfPerformance)
        .build();

    MisterPlayerTop mfMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.MF)
        .numberOfPlayers(40)
        .avgPoints(mfAvgPoints)
        .avgPerformance(mfPerformance)
        .build();

    MisterPlayerTop fwMisterPlayerTop = MisterPlayerTop.builder()
        .position(PlayerPosition.FW)
        .numberOfPlayers(20)
        .avgPoints(fwAvgPoints)
        .avgPerformance(fwPerformance)
        .build();

    return MisterPlayerReport.builder()
        .marketPlayers(marketPlayers)
        .ownerPlayers(ownerPlayers)
        .gkTop(gkMisterPlayerTop)
        .dfTop(dfMisterPlayerTop)
        .mfTop(mfMisterPlayerTop)
        .fwTop(fwMisterPlayerTop)
        .build();
  }*/

}
