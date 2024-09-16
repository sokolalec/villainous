package model

import io.Filesystem.{readJsonFile, tournamentFiles}
import model.Expansion.ownedExpansions
import model.Villain.{Gaston, MadamMim, MotherGothel, Syndrome, Ursula}
import model.game.DuelGame
import model.game.DuelGame.tournamentGameDecoder

case class Tournament(version: String, availableVillains: Set[Villain])

object Tournament {

  val ownedVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  val bannedVillains: Set[Villain] = Set(MotherGothel, Syndrome, Gaston, Ursula, MadamMim)

  def getTournamentGames(tournament: String): List[DuelGame] =
    tournamentFiles
      .filter(_.getFileName.toString.startsWith(tournament))
      .flatMap(f => readJsonFile(f, tournamentGameDecoder))
      .toList

  val tournament1: List[DuelGame] = getTournamentGames("001")
  val tournament2: List[DuelGame] = getTournamentGames("002")
  val tournament3: List[DuelGame] = getTournamentGames("003")
  val tournament4: List[DuelGame] = getTournamentGames("004")
  val tournament5: List[DuelGame] = getTournamentGames("005")

}
