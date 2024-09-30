package tournaments
import game.{MatchUp, PlayableGame}
import model.Expansion._
import model.{Player, Villain}
import util.GameOps.getFirstPlayer
import util.MatchUps.getMatch

object Tournament3 extends Tournament {

  override val version: String = "003"

  override val availableVillains: Set[Villain] = Set(Original, WickedToTheCore, PerfectlyWretched, DespicablePlots, BiggerAndBadder).flatMap(_.villains)

  override val finished: Boolean = (players().map(winCount).max >= availableVillains.size) || (players().flatMap(getVillainsRemaining).size == 1)

  private def winCount(player: Player): Int = games().count(_.winnerPlayer == player)

  def getVillainsRemaining(player: Player): Set[Villain] =
    availableVillains.filterNot(v => games().exists(g => g.winnerPlayer == player && g.winner == v))

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableGame] = {
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
          Some(PlayableGame(player1, v1, player2, v2, firstPlayer))
      }
    }
  }

}
