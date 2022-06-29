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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Schedule Item Data object.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "schedule_items")
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @Column(name = "id")
  private long id;

  @Column(name = "identifier")
  @JsonProperty("guid")
  private String identifier;

  @Column(name = "schedule_date")
  @JsonProperty("pubDate")
  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime scheduleDate;

  @JsonIgnore
  @Column(name = "schedule_type")
  private String scheduleType;

  @JsonIgnore
  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @JsonIgnore
  @Column(name = "updated_on")
  private ZonedDateTime updatedOn;

}
