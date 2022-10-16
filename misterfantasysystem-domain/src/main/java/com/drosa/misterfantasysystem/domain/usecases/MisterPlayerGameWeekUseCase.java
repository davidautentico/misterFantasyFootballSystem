package com.drosa.misterfantasysystem.domain.usecases;

import com.drosa.misterfantasysystem.domain.entities.MisterGameWeekReport;
import com.drosa.misterfantasysystem.domain.repositories.MisterReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MisterPlayerGameWeekUseCase {

  private final MisterReaderRepository misterReaderRepository;

  public MisterPlayerGameWeekUseCase(MisterReaderRepository misterReaderRepository) {
    this.misterReaderRepository = misterReaderRepository;
  }

  public MisterGameWeekReport dispatch(){

    misterReaderRepository.getMisterGameWeekInfo();

    return MisterGameWeekReport.builder().build();
  }

}
