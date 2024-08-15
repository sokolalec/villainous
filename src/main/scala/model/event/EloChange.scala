package model.event

import model.stats.PlayerCharacter

case class EloChange(opponent: PlayerCharacter, eloDifference: Double)
