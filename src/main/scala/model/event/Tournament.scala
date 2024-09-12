package model.event

import model.environment.Expansion.ownedExpansions
import model.environment.Villain
import model.environment.Villain.{Gaston, MadamMim, MotherGothel, Syndrome, Ursula}

case class Tournament(version: String, availableVillains: Set[Villain])

object Tournament {

  val ownedVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  val bannedVillains: Set[Villain] = Set(MotherGothel, Syndrome, Gaston, Ursula, MadamMim)

}
