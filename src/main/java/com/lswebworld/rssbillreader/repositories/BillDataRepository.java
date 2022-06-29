package com.lswebworld.rssbillreader.repositories;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for saving Bill Information.
 */
@Repository
public interface BillDataRepository extends JpaRepository<BillInfo, String> {
}
