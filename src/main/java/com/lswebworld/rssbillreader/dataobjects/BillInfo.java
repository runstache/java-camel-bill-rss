package com.lswebworld.rssbillreader.dataobjects;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Class for storing Bill information.
 */
@Entity
@Table(name = "bills")
@Getter
@Setter
@EqualsAndHashCode
public class BillInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "identifier")
  private String identifier;

  @Column(name = "description")
  private String description;

  @Column(name = "url")
  private String url;

  @Column(name = "pub_date")
  private ZonedDateTime pubDate;

  @Column(name = "prime_sponsor")
  private String primeSponsor;

  @Column(name =  "co_sponsors")
  private String coSponsors;

  @Column(name = "last_action")
  private String lastAction;

  @Column(name = "enacted")
  private boolean enacted;

  @Column(name = "passed_house")
  private boolean passedHouse;

  @Column(name = "passed_senate")
  private boolean passedSenate;

}
