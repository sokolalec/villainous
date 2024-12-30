package model

case class PlayerTeam(player: Player, villains: Set[Villain]) {

  override def toString: String = s"${player.name}'s Team: $villains"

}
