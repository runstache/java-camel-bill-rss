package com.lswebworld.rssbillreader.repositories;

import com.lswebworld.rssbillreader.dataobjects.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Jpa Respository for Schedule Entries.
 */
@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {

  /**
   * Deletes all of the Schedule Entries based on Identifier.
   *
   * @param identifier Identifier.
   */
  void deleteAllByIdentifier(String identifier);
}
