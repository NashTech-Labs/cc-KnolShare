package com.knoldus.utils

import com.google.inject.Inject

class TechnologyNames @Inject()(configReader: TwitterConfigReader) {
  private val list : List[String] = configReader.getTechnologyNames()
  def get(): List[String] = list
}
