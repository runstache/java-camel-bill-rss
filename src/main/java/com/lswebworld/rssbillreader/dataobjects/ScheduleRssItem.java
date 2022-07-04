package com.lswebworld.rssbillreader.dataobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Schedule Rss Container item.
 */
@Getter
@Setter
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRssItem {

  @JsonProperty("item")
  private ScheduleInfo item;

}
