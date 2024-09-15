package elo

case class Elo[T](current: Double, audit: List[EloChange[T]]) {

  override def toString: String = current.toString

  def showEloHistory(): Unit = {
    println(s"Displaying ELO history:")
    audit.foreach { event => println(s"  ${event.opponent} ${event.eloDifference}") }
  }

}
