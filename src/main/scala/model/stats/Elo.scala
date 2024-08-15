package model.stats

import model.event.EloChange

case class Elo(current: Double, audit: List[EloChange]) {

  override def toString: String = current.toString

  def showEloHistory(): Unit = {
    println(s"Displaying ELO history:")
    audit.foreach { event => println(s"  ${event.opponent} ${event.eloDifference}") }
  }

}
