package model

case class PlayerVillain(player: Player, villain: Villain) {

  override def toString: String = s"${player.name}'s $villain"

}
