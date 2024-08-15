package util

import scala.util.Random

object RNG {

  def getRandom(): Random = new Random(System.currentTimeMillis())

}
