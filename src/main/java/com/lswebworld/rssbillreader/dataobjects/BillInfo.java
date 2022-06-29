package com.lswebworld.rssbillreader.dataobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillInfo {

  @Column(name = "title")
  private String title;

  @Id
  @Column(name = "identifier")
  @JsonProperty("guid")
  private String identifier;

  @Column(name = "description")
  @JsonProperty("description")
  private String description;

  @Column(name = "url")
  @JsonProperty("link")
  private String url;

  @Column(name = "pub_date")
  @JsonProperty("pubDate")
  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime pubDate;

  @Column(name = "prime_sponsor")
  @JsonProperty("primeSponsor")
  private String primeSponsor;

  @Column(name = "co_sponsors")
  @JsonProperty("coSponsors")
  private String coSponsors;

  @Column(name = "last_action")
  @JsonProperty("lastAction")
  private String lastAction;

  @Column(name = "enacted")
  @JsonProperty("enacted")
  @JsonDeserialize(using = BooleanDeserializer.class)
  @JsonSerialize(using = BooleanSerializer.class)
  private boolean enacted;

  @Column(name = "passed_house")
  @JsonProperty("passedHouse")
  @JsonDeserialize(using = BooleanDeserializer.class)
  @JsonSerialize(using = BooleanSerializer.class)
  private boolean passedHouse;

  @Column(name = "passed_senate")
  @JsonProperty("passedSenate")
  @JsonDeserialize(using = BooleanDeserializer.class)
  @JsonSerialize(using = BooleanSerializer.class)
  private boolean passedSenate;

  @Column(name = "created_on")
  @JsonIgnore
  private ZonedDateTime createdOn;

  @Column(name = "updated_on")
  @JsonIgnore
  private ZonedDateTime updatedOn;

}
