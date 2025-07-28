package tournaments

import game.{DuelGame, MatchUp}
import io.PlayableDuel
import model.Expansion._
import model.Player.{alec, dennis}
import model.Villain._
import model.{Player, Villain}
import util.GameOps.getFirstPlayer
import util.MatchUps.{getMatch, getRandomT}
import util.RNG.getRandom

object Tournament8 extends Tournament {

  override val version: String = "008"

  override val availableVillains: Set[Villain] = Set(Original,
    WickedToTheCore,
    EvilComesPrepared,
    PerfectlyWretched,
    DespicablePlots,
    BiggerAndBadder,
    FilledwithFright,
    SugarandSpite).flatMap(_.villains)

  private val alecUpperVillains: Set[Villain] = Set(MadamMim, Maleficent, ShereKhan, LadyTremaine, KingCandy, EvilQueen, Scar, PrinceJohn)
  private val alecMiddleVillains: Set[Villain] = Set(CaptainHook, Ratigan, CruelladeVil, HornedKing, DrFacilier, Pete, MotherGothel, Syndrome)
  private val alecLowerVillains: Set[Villain] = availableVillains -- alecUpperVillains -- alecMiddleVillains

  private val dennisUpperVillains: Set[Villain] = Set(DrFacilier, Maleficent, MadamMim, Ratigan, Lotso, Ursula, Yzma, HornedKing)
  private val dennisMiddleVillains: Set[Villain] = Set(LadyTremaine, Gaston, Jafar, PrinceJohn, ShereKhan, Hades, EvilQueen, OogieBoogie)
  private val dennisLowerVillains: Set[Villain] = availableVillains -- dennisUpperVillains -- dennisMiddleVillains

  private val upperBracket = Bracket(games(), Map(alec -> alecUpperVillains, dennis -> dennisUpperVillains))
  private val middleBracket = Bracket(games(), Map(alec -> alecMiddleVillains, dennis -> dennisMiddleVillains))
  private val lowerBracket = Bracket(games(), Map(alec -> alecLowerVillains, dennis -> dennisLowerVillains))

  private val brackets = Set(upperBracket, middleBracket, lowerBracket)

  override val finished: Boolean = {
    val wins = players().map(winCount)
    wins.nonEmpty && wins.max >= 2
  }

  private def winCount(player: Player): Int = brackets.count(_.winner.contains(player))

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    if (finished) {
      congratulate()
      None
    } else {
      getRandomT(brackets.filterNot(_.finished)) match {
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
