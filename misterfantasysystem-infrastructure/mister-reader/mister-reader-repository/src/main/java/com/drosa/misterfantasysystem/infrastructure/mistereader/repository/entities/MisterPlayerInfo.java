package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class MisterPlayerInfo {

  private final Instant createdAt;

  private final int gameWeek;

  private final List<MisterPlayer> players;
}
