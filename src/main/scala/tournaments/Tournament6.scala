package tournaments
import game.MatchUp
import io.PlayableDuel
import model.Expansion._
import model.{Player, Villain}
import stats.PlayerRecord
import util.GameOps.getFirstPlayer
import util.MatchUps.getMatch

object Tournament6 extends Tournament {

  override val version: String = "006"

  override val finished: Boolean = players().map(winCount).max >= 25

  override val availableVillains: Set[Villain] = Set(Original,
    WickedToTheCore,
    EvilComesPrepared,
    PerfectlyWretched,
    DespicablePlots,
    BiggerAndBadder,
    FilledwithFright,
    SugarandSpite).flatMap(_.villains)

  private def winCount(player: Player): Int = games().count(_.winnerPlayer == player)

  def getVillainsRemaining(player: Player): Set[Villain] = {
    val playCounts: Map[Villain, Int] = PlayerRecord.getPlayCounts(games(), player)

    if (playCounts.isEmpty) availableVillains
    else if (playCounts.size != availableVillains.size) availableVillains -- playCounts.keys
    else {
      val maxPlayCount = playCounts.values.max // throws on empty map
      if (playCounts.forall(kv => kv._2 == maxPlayCount)) availableVillains
      else availableVillains -- playCounts.filter(kv => kv._2 == maxPlayCount).keys
    }
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    if (finished) {
      congratulate()
      None
    } else {
      val player1Villains = getVillainsRemaining(player1)
      val player2Villains = getVillainsRemaining(player2)

      getMatch(player1Villains, player2Villains) match {
        case None => None
        case Some(MatchUp(v1, v2)) =>
          val firstPlayer = getFirstPlayer(v1, v2)
          Some(PlayableDuel(player1, v1, player2, v2, firstPlayer))
      }
    }
  }

}
