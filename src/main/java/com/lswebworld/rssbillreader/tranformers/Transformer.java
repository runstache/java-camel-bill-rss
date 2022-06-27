package com.lswebworld.rssbillreader.tranformers;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.rometools.rome.feed.synd.SyndFeed;
import java.net.URISyntaxException;
import java.util.List;

/**
 * RSS element Transformer Interface.
 */
public interface Transformer {

  /**
   * Transforms an instance of an RSS Feed to a Bill Info Data Class.
   *
   * @param feed Synd Feed
   * @return Bill Info.
   */
  List<BillInfo> transform(SyndFeed feed) throws URISyntaxException;


}
