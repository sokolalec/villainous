package model

case class Player(name: String) {
  override def toString: String = name
}

object Player {

  val alec: Player = Player("Alec")
  val dennis: Player = Player("Dennis")
  val michael: Player = Player("Michael")

}
