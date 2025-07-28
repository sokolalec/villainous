package tournaments

import game.DuelGame
import io.Filesystem.allGames
import io.PlayableDuel
import model.Villain._
import model.{Player, Villain}

import scala.annotation.tailrec

object Tournament14 extends Tournament {
  override val version: String = "014"
  override val availableVillains: Set[Villain] = Villain.values.toSet

  val bracket1: Set[Villain] = Set(EvilQueen, MadamMim, CaptainHook, Ratigan)
  val bracket2: Set[Villain] = Set(Lotso, Maleficent, KingCandy, OogieBoogie)
  val bracket3: Set[Villain] = Set(ShereKhan, Ursula, HornedKing, Jafar, DavyJones)
  val bracket4: Set[Villain] = Set(DrFacilier, Syndrome, LadyTremaine, CruelladeVil)
  val bracket5: Set[Villain] = Set(PrinceJohn, Yzma, Gaston, QueenofHearts, Tamatoa)
  val bracket6: Set[Villain] = Set(MotherGothel, Hades, Scar, Pete)

  val brackets: Seq[Set[Villain]] = Seq(bracket1, bracket2, bracket3, bracket4, bracket5, bracket6)

  def points(v1: Villain, v2: Villain): Int = {
    6 + Tournament14.brackets.indexWhere(_.contains(v1)) - Tournament14.brackets.indexWhere(_.contains(v2))
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    val choosingPlayer = if (games().size % 2 == 0) player1 else player2

    def playedBy(game: DuelGame, player: Player, villain: Villain): Boolean = {
      (game.winnerPlayer == player && game.winner == villain) || (game.loserPlayer == player && game.loser == villain)
    }

    val missingGames = Villain.values.map(v => {
      val playedGames = allGames.filter(g => playedBy(g, choosingPlayer, v))
      (v, Villain.values.toSet -- playedGames.flatMap(g => Seq(g.winner, g.loser)).toSet)
    }).toMap
    println(s"Games $choosingPlayer has never played: ${missingGames.map { case (_, matchups) => matchups.size }.sum}")
    println("")
    missingGames.foreach { case (v, matchups) =>
      println(s" $v:")
      matchups.foreach(m => println(s"     $m  (${points(v, m)})"))
      println("")
    }
    println("")
    println("")
    None
  }

  def playerScore(games: Seq[DuelGame], player: Player): Int = {
    games.filter(_.winnerPlayer == player).map(g => points(g.winner, g.loser))
  }.sum

  case class MiniTournament(games: Seq[DuelGame], player1: Player, player1Score: Int, player2: Player, player2Score: Int)

  def getMiniTournaments(player1: Player, player2: Player): Seq[MiniTournament] = {
    @tailrec
    def iterate(g: Seq[DuelGame], p1: Int, p2: Int, tournaments: Seq[MiniTournament], thisTournament: Seq[DuelGame]): Seq[MiniTournament] = {
      g match {
        case Nil =>
          if (thisTournament.nonEmpty) tournaments :+ MiniTournament(thisTournament, player1, p1, player2, p2) else tournaments
        case h :: t =>
          val (newP1, newP2) = (playerScore(thisTournament :+ h, player1), playerScore(thisTournament :+ h, player2))
          if (newP1 > 33 || newP2 > 33) iterate(t, 0, 0, tournaments :+ MiniTournament(thisTournament :+ h, player1, newP1, player2, newP2), Seq.empty)
          else iterate(t, newP1, newP2, tournaments, thisTournament :+ h)
      }
    }
    iterate(games(), 0, 0, Seq.empty, Seq.empty)
  }

  def showScores(player1: Player, player2: Player): Unit = {
    val tournaments = getMiniTournaments(player1, player2)
    val player1TournamentWins = tournaments.count(t => t.player1Score > 33)
    val player2TournamentWins = tournaments.count(t => t.player2Score > 33)

    if (player1TournamentWins > 0) println(s"  $player1 Tournament Wins: $player1TournamentWins")
    if (player2TournamentWins > 0) println(s"  $player2 Tournament Wins: $player2TournamentWins")
    println("")
    tournaments.foreach(t => {
      println("  MiniTournament:")
      println(s"     $player1's score: ${playerScore(t.games, player1)}")
      println(s"     $player2's score: ${playerScore(t.games, player2)}")
    })
  }
}
