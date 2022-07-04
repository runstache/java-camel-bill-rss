package com.lswebworld.rssbillreader.repositories;

import com.lswebworld.bills.data.dataobjects.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for saving Bill Information.
 */
@Repository
public interface BillDataRepository extends JpaRepository<BillInfo, String> {
}
