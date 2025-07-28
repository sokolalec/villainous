package tournaments

import game.DuelGame
import io.PlayableDuel
import model.Player._
import model.Villain._
import model.{Player, Villain}
import util.RNG.getRandom

import scala.collection.mutable

object Tournament11 extends Tournament {

  override val version: String = "011"

  override val availableVillains: Set[Villain] = Set.empty

  val bracket1: Set[Villain] = Set(MadamMim, ShereKhan, Lotso, PrinceJohn)
  val bracket2: Set[Villain] = Set(Maleficent, EvilQueen, DrFacilier, Ratigan)
  val bracket3: Set[Villain] = Set(CaptainHook, Jafar, Ursula, Yzma)
  val bracket4: Set[Villain] = Set(CruelladeVil, OogieBoogie, KingCandy, LadyTremaine)
  val bracket5: Set[Villain] = Set(MotherGothel, Pete, HornedKing, Syndrome)
  val bracket6: Set[Villain] = Set(QueenofHearts, Hades, Gaston, Scar)

  val brackets: Seq[Set[Villain]] = Seq(bracket1, bracket2, bracket3, bracket4, bracket5, bracket6)

  def showBrackets(): Unit = {
    brackets.foreach { b =>
      val bracketGames = getBracketGames(b)
      b.foreach { v =>
        val wins = bracketGames.count(_.winner == v)
        val losses = bracketGames.count(_.loser == v)
        println(s"$v's Record: ($wins, $losses)")
      }
      println("")
    }
  }

  def getWins(games: Seq[DuelGame], player: Player): Set[Villain] = {
    val wins = games.filter(_.winnerPlayer == player).map(_.winner).distinct
    val gamesWithWins = games.filter(g => g.winnerPlayer == player || (g.loserPlayer == player && wins.contains(g.loser)))
    val counts: mutable.HashMap[Villain, Int] = new mutable.HashMap[Villain, Int]()
    gamesWithWins.foreach { g =>
      val thisVillain = if (g.winnerPlayer == player) g.winner else g.loser
      if (g.winnerPlayer == player) counts.put(thisVillain, 1) else counts.put(thisVillain, 0)
    }
    counts.toMap.filter(_._2 > 0).keySet
  }

  def bracketComplete(bracket: Set[Villain]): Boolean = {
    val bracketGames = getBracketGames(bracket)
    val alecWins = getWins(bracketGames, alec).size
    val dennisWins = getWins(bracketGames, dennis).size
    Math.max(alecWins, dennisWins) > 2
  }

  def getBracketGames(bracket: Set[Villain]): Seq[DuelGame] =
    games().filter(g => bracket.contains(g.winner) && bracket.contains(g.loser))

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    val randomizer = getRandom()
    val picker = if (randomizer.nextBoolean()) alec else dennis
    val first = if (randomizer.nextBoolean()) alec else dennis
    val bracket = randomizer.shuffle(brackets.filter(b => !bracketComplete(b))).head

    val alecWins = getWins(getBracketGames(bracket), alec)
    val dennisWins = getWins(getBracketGames(bracket), dennis)

    println(s"\n  BRACKET\n    $bracket\n    Alec Wins: $alecWins\n    Dennis Wins: $dennisWins\n\n  First Villain Chooser:\n    $picker\n\n  First Player\n    $first")
    None
  }

}
