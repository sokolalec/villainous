package tournaments
import game.PlayableGame
import model.Expansion._
import model.Villain._
import model.{Player, Villain}
import util.GameOps.getFirstPlayer

object Tournament2 extends Tournament {

  override val version: String = "002"

  override val availableVillains: Set[Villain] = Set(Original, PerfectlyWretched, DespicablePlots).flatMap(_.villains)

  override val finished: Boolean = {
    val wins = players().map(winCount)
    wins.max == 12 || wins.min == 11
  }

  private def winCount(player: Player): Int = games().count(_.winnerPlayer == player)

  // strongest first, second strongest last, third strongest second, fourth strongest second to last...
  private val ordering: List[Villain] = List(
    CaptainHook,
    QueenofHearts,
    Jafar,
    Ursula,
    HornedKing,
    Gaston,
    CruelladeVil,
    Pete,
    MotherGothel,
    LadyTremaine,
    PrinceJohn,
    Maleficent
  )

  private def topPlayer(): Player = if (games().head.winner == ordering.head) games().head.winnerPlayer
    else games().head.loserPlayer

  private def getNextVillain(player: Player, skipOne: Boolean = false): Option[Villain] = {
    val order = if (player == topPlayer()) ordering else ordering.reverse

    order.find(v => !games().exists(g => g.winnerPlayer == player && g.winner == v)) match {
      case Some(nextVillain) =>
        if (!skipOne) Some(nextVillain)
        else {
          val otherVillains = order.dropWhile(_ != nextVillain).drop(1)
          otherVillains.find(v => games().exists(g => g.winnerPlayer == player && g.winner == v))
        }
      case None => None
    }
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableGame] = {
    if (finished) {
      congratulate()
      None
    } else {
      games().lastOption match {
        case None =>
          val first = ordering.head
          val last = ordering.last
          Some(PlayableGame(player1, first, player2, last, getFirstPlayer(first, last)))
        case Some(g) =>
          val bottom = if (topPlayer() == player1) player2 else player1

          val nextTop = getNextVillain(topPlayer())
          val nextBottom = getNextVillain(bottom)

          nextTop.flatMap(nt => nextBottom.map(nb => {
            if (nt == nb) {
              if (winCount(topPlayer()) == ordering.size - 1) {
                val bottomVillain = getNextVillain(bottom, skipOne = true).get
                PlayableGame(topPlayer(), nt, bottom, bottomVillain, getFirstPlayer(nt, bottomVillain))
              } else if (winCount(bottom) == ordering.size - 1) {
                val topVillain = getNextVillain(topPlayer(), skipOne = true).get
                PlayableGame(topPlayer(), topVillain, bottom, nb, getFirstPlayer(topVillain, nb))
              } else {
                val topVillain = if (g.loserPlayer == topPlayer()) nt else getNextVillain(topPlayer(), skipOne = true).get
                val bottomVillain = if (g.loserPlayer == bottom) nb else getNextVillain(bottom, skipOne = true).get
                PlayableGame(topPlayer(), topVillain, bottom, bottomVillain, getFirstPlayer(topVillain, bottomVillain))
              }
            } else PlayableGame(topPlayer(), nt, bottom, nb, getFirstPlayer(nt, nb))
          }))


      }
    }
  }

}
