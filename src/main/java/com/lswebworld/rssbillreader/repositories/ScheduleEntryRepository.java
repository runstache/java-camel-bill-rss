package com.lswebworld.rssbillreader.repositories;

import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Jpa Respository for Schedule Entries.
 */
@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleInfo, Long> {

  /**
   * Retrieves all of the Schedule Entries based on Identifier and Schedule Type.
   *
   * @param identifier Identifier
   * @param scheduleType Schedule Type.
   * @return List of Schedule Info.
   */
  List<ScheduleInfo> findAllByIdentifierAndScheduleType(String identifier, String scheduleType);
}
