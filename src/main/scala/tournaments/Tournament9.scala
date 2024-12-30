package tournaments
import game.PlayableDuel
import model.Expansion.ownedExpansions
import model.Player.{alec, dennis}
import model.Villain.{DrFacilier, ShereKhan}
import model.{Player, Villain}

object Tournament9 extends Tournament {
  override val version: String = "009"
  override val availableVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  val villains: Map[Player, Villain] = Map(alec -> ShereKhan, dennis -> DrFacilier)

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    val playableVillains = availableVillains - villains(player1) - villains(player2)

    val p1Played = playableVillains.filter(v => games().exists(g => (g.winnerPlayer == player1 && g.winner == v) || (g.loserPlayer == player1 && g.loser == v)))
    val p2Played = playableVillains.filter(v => games().exists(g => (g.winnerPlayer == player2 && g.winner == v) || (g.loserPlayer == player2 && g.loser == v)))

    println(s"Remaining: \n  $player1: ${playableVillains -- p1Played}\n  $player2: ${playableVillains -- p2Played}")
    None
  }
}
