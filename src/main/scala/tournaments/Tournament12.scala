package tournaments

import game.DuelGame
import io.PlayableDuel
import model.Player.{alec, dennis}
import model.{Player, Villain}
import util.RNG.getRandom

import scala.collection.mutable

object Tournament12 extends Tournament {

  override val version: String = "012"

  override val availableVillains: Set[Villain] = Set.empty

  val brackets: Seq[Set[Villain]] = Tournament11.brackets

  private def winCount(player: Player, games: Seq[DuelGame]) = ???

  //  override val finished: Boolean = ???

  def showBrackets(): Unit = {
    brackets.foreach { b =>
      val bracketGames = getBracketGames(b)
      if (bracketComplete(b)) println(s"Bracket Winner: ${bracketWinner(b)}")
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

  // assume bracketComplete
  def bracketWinner(bracket: Set[Villain]): Player = {
    val bracketGames = getBracketGames(bracket)
    val alecWins = getWins(bracketGames, alec).size
    if (alecWins > 2) alec else dennis
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
