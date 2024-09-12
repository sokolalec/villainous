package model.stats

import model.environment.{Player, Villain}
import model.event.EloChange

case class PlayerCharacter(player: Player, villain: Villain) {
  override def toString: String = s"${player.name}'s $villain"
}
