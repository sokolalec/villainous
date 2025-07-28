package tournaments

import io.PlayableDuel
import model.Player._
import model.{Player, Villain}
import model.Villain._


object Tournament10 extends Tournament {

  val bracket1: Set[Villain] = Set(MadamMim, ShereKhan, Lotso, Ratigan, PrinceJohn)           // ALEC WINS
  val bracket2: Set[Villain] = Set(Maleficent, EvilQueen, DrFacilier, Ursula, Yzma)           // ALEC WINS
  val bracket3: Set[Villain] = Set(CaptainHook, Jafar, CruelladeVil, OogieBoogie, KingCandy)  // DENNIS WINS
  val bracket4: Set[Villain] = Set(MotherGothel, Pete, HornedKing, LadyTremaine)              // ALEC WINS
  val bracket5: Set[Villain] = Set(QueenofHearts, Hades, Gaston, Scar, Syndrome)

//  case class SubBracket(bracket: Set[Villain]) extends Bracket(games(), Map(alec -> bracket, dennis -> bracket))

  override val finished: Boolean = true

  override val version: String = "010"
  override val availableVillains: Set[Villain] = Set.empty

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = ???

}
