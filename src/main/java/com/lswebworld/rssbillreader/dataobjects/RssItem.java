package com.lswebworld.rssbillreader.dataobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lswebworld.bills.data.dataobjects.BillInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * RSS Encapsulated item.
 */
@Getter
@Setter
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RssItem {

  @JsonProperty("item")
  private BillInfo item;

}
