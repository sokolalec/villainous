package model

import model.Expansion.ownedExpansions
import model.Villain.{Gaston, MadamMim, MotherGothel, Syndrome, Ursula}

case class Tournament(version: String, availableVillains: Set[Villain])

object Tournament {

  val ownedVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  val bannedVillains: Set[Villain] = Set(MotherGothel, Syndrome, Gaston, Ursula, MadamMim)

}
