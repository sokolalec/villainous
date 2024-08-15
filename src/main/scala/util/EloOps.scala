package util

import model.Player
import model.event.{EloChange, Game}
import model.stats.{Elo, PlayerCharacter}

object EloOps {

  /**
   * Determines general sensitivity to ELO changes
   */
  val eloFactor = 64

  def calculateNewRating(winnerRating: Double, loserRating: Double): (Double, Double) = {
    val expectedWinner = 1.0 / (1.0 + Math.pow(10, (loserRating - winnerRating) / 400))
    val expectedLoser = 1.0 / (1.0 + Math.pow(10, (winnerRating - loserRating) / 400))

    val newWinnerRating = winnerRating + eloFactor * (1 - expectedWinner)
    val newLoserRating = loserRating + eloFactor * (0 - expectedLoser)

    (newWinnerRating, newLoserRating)
  }

  def calculateElo(games: List[Game]): Map[PlayerCharacter, Elo] = {
    val initialRatings: Map[PlayerCharacter, Elo] = Map.empty.withDefaultValue(Elo(1500.0, List.empty))

    games.foldLeft(initialRatings) { (ratings, game) =>
      val winner = PlayerCharacter(game.winnerPlayer, game.winner)
      val loser = PlayerCharacter(game.loserPlayer, game.loser)

      val winnerElo = ratings(winner)
      val loserElo = ratings(loser)

      val (newWinnerRating, newLoserRating) = calculateNewRating(winnerElo.current, loserElo.current)

      val newWinnerElo = Elo(newWinnerRating, winnerElo.audit :+ EloChange(loser, newWinnerRating - winnerElo.current))
      val newLoserElo = Elo(newLoserRating, loserElo.audit :+ EloChange(winner, newLoserRating - loserElo.current))

      ratings.updated(winner, newWinnerElo).updated(loser, newLoserElo)
    }
  }

  def sortedElo(elo: Map[PlayerCharacter, Elo]): Seq[(PlayerCharacter, Elo)] = elo.toSeq.sortBy(-_._2.current)

  def getBestCharacters(eloRatings: Map[PlayerCharacter, Elo], player: Player, n: Int): List[(PlayerCharacter, Elo)] = {
    sortedElo(eloRatings.filter(_._1.player == player)).take(n).toList
  }

  def getWorstCharacters(eloRatings: Map[PlayerCharacter, Elo], player: Player, n: Int): List[(PlayerCharacter, Elo)] = {
    sortedElo(eloRatings.filter(_._1.player == player)).reverse.take(n).toList
  }

  def displaySortedElo(elo: Map[PlayerCharacter, Elo]): Unit = {
    println(f"${"Player"}%-20s ${"ELO Rating"}%-10s")
    println("=" * 30)
    sortedElo(elo).foreach { case (player, rating) =>
      println(f"${player}%-22s ${rating.current}%-10.2f")
    }
    println("")
  }

}
