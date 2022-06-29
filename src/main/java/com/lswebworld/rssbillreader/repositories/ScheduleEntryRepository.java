package com.lswebworld.rssbillreader.repositories;

import com.lswebworld.rssbillreader.dataobjects.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Jpa Respository for Schedule Entries.
 */
@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
}
