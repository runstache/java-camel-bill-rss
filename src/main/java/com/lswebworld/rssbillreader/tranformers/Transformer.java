package com.lswebworld.rssbillreader.tranformers;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * RSS element Transformer Interface.
 */
public interface Transformer<T> {

  /**
   * Transforms an instance of an RSS Feed to a Bill Info Data Class.
   *
   * @param value XML/JSON Feed Value
   * @return Bill Info.
   */
  Optional<T> transform(String value) throws URISyntaxException;


}
