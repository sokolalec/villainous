package tournaments

import game.DuelGame
import model.{Player, Villain}
import stats.PlayerRecord

import scala.util.Try

case class Bracket(tournamentGames: Seq[DuelGame], availableVillains: Map[Player, Set[Villain]]) {

  private def bracketGames(): Seq[DuelGame] = tournamentGames.filter(g =>
    availableVillains.get(g.winnerPlayer).exists(villains => villains.contains(g.winner))
  )

  private def winCount(player: Player): Int = bracketGames().count(_.winnerPlayer == player)

  private val players = availableVillains.keys.toSet

  val finished: Boolean = {
    val requiredWins = availableVillains.head._2.size + 1
    players.map(winCount).max >= requiredWins
  }

  val winner: Option[Player] = if (finished) {
    Try(players.map(p => (p, winCount(p))).maxBy(_._2)._1).map(Some(_)).getOrElse(None)
  } else None

  def getVillainsRemaining(player: Player): Set[Villain] = {
    val villains = availableVillains(player)
    val playCounts: Map[Villain, Int] = PlayerRecord.getPlayCounts(bracketGames(), player)

    if (playCounts.isEmpty) villains
    else if (playCounts.size != villains.size) villains -- playCounts.keys
    else {
      val maxPlayCount = playCounts.values.max // throws on empty map
      if (playCounts.forall(kv => kv._2 == maxPlayCount)) villains
      else villains -- playCounts.filter(kv => kv._2 == maxPlayCount).keys
    }
  }

}
