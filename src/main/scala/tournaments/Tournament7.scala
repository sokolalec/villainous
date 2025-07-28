package tournaments

import game.MatchUp
import io.PlayableDuel
import model.Player.{alec, dennis}
import model.Villain._
import model.{Player, Villain}
import util.GameOps.getFirstPlayer
import util.MatchUps.getMatch

import scala.annotation.tailrec

object Tournament7 extends Tournament {

  override val version: String = "007"

  private val upperVillains: Set[Villain] = Set(MadamMim, Maleficent, DrFacilier, Lotso, Jafar, CaptainHook, EvilQueen, HornedKing)
  private val middleVillains: Set[Villain] = Set(ShereKhan, MotherGothel, CruelladeVil, LadyTremaine, Pete, PrinceJohn, KingCandy, Ratigan)
  private val lowerVillains: Set[Villain] = Set(Ursula, Scar, Yzma, QueenofHearts, Hades, Syndrome, Gaston, OogieBoogie)

  private val upperBracket = Bracket(games(), Map(alec -> upperVillains, dennis -> upperVillains))
  private val middleBracket = Bracket(games(), Map(alec -> middleVillains, dennis -> middleVillains))
  private val lowerBracket = Bracket(games(), Map(alec -> lowerVillains, dennis -> lowerVillains))

  private val brackets = Seq(middleBracket, upperBracket, lowerBracket)

  override val finished: Boolean = {
    val wins = players().map(winCount)
    wins.nonEmpty && wins.max >= 2
  }

  override val availableVillains: Set[Villain] = brackets.flatMap(_.availableVillains).flatMap(_._2).toSet

  private def winCount(player: Player): Int = brackets.count(_.winner.contains(player))

  @tailrec
  private def getActiveBracket(brackets: Seq[Bracket]): Option[Bracket] = brackets match {
    case Nil => None
    case h :: t => if (!h.finished) Some(h) else getActiveBracket(t)
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    if (finished) {
      congratulate()
      None
    } else {
      getActiveBracket(brackets) match {
        case Some(bracket) =>
          val player1Villains = bracket.getVillainsRemaining(player1)
          val player2Villains = bracket.getVillainsRemaining(player2)

          println(s"$player1: $player1Villains")
          println(s"$player2: $player2Villains")

          getMatch(player1Villains, player2Villains) match {
            case None => None
            case Some(MatchUp(v1, v2)) =>
              val firstPlayer = getFirstPlayer(v1, v2)
              Some(PlayableDuel(player1, v1, player2, v2, firstPlayer))
          }
        case None => None
      }
    }
  }

}
