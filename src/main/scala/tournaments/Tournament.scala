package tournaments

import game.DuelGame.tournamentGameDecoder
import game.DuelGame
import io.Filesystem.{getGames, tournamentDir}
import io.PlayableDuel
import model.{Player, Villain}

trait Tournament {

  val version: String

  val finished: Boolean = false

  val availableVillains: Set[Villain]

  def games(): Seq[DuelGame] = {
    val currentTournament = s"$tournamentDir/$version.json"
    val archivedTournament = s"$tournamentDir/archived/$version.json"
    getGames(currentTournament, tournamentGameDecoder) ++ getGames(archivedTournament, tournamentGameDecoder)
  }

  def players(): Set[Player] = games().flatMap(g => List(g.winnerPlayer, g.loserPlayer)).distinct.toSet

  def score(): Map[Player, Int] = players().map(p => (p, games().count(_.winnerPlayer == p))).toMap

  def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel]

  def congratulate(): Unit = {
    if (finished) {
      val winner = score().maxBy(_._2)._1
      val msg = s"   #  Tournament $version Winner:   $winner  #"
      val border = "   " + "#" * (msg.length - 3)
      println("")
      println(border)
      println(msg)
      println(border)
      println("")
    }
  }

}
