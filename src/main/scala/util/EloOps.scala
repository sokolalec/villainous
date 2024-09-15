package util

import elo.{Elo, EloCalculable, EloChange}
import model.game.DuelGame

object EloOps {

  /**
   * Determines general sensitivity to ELO changes.
   * You can do some manipulation here, but don't, because you don't know what you are doing.
   */
  val eloFactor = 64

  def calculateNewRating(winnerRating: Double, loserRating: Double): (Double, Double) = {
    val expectedWinner = 1.0 / (1.0 + Math.pow(10, (loserRating - winnerRating) / 400))
    val expectedLoser = 1.0 / (1.0 + Math.pow(10, (winnerRating - loserRating) / 400))

    val newWinnerRating = winnerRating + eloFactor * (1 - expectedWinner)
    val newLoserRating = loserRating + eloFactor * (0 - expectedLoser)

    (newWinnerRating, newLoserRating)
  }

  def calculateElo[T](games: List[DuelGame], eloOperator: EloCalculable[T]): Map[T, Elo[T]] = {
    val initialRatings: Map[T, Elo[T]] = Map.empty.withDefaultValue(Elo(1500.0, List.empty))

    games.foldLeft(initialRatings) { (ratings, game) =>
      val winner = eloOperator.eloWinner(game)
      val loser = eloOperator.eloLoser(game)

      val winnerElo = ratings(winner)
      val loserElo = ratings(loser)

      val (newWinnerRating, newLoserRating) = calculateNewRating(winnerElo.current, loserElo.current)

      val newWinnerElo = Elo(newWinnerRating, winnerElo.audit :+ EloChange(loser, newWinnerRating - winnerElo.current))
      val newLoserElo = Elo(newLoserRating, loserElo.audit :+ EloChange(winner, newLoserRating - loserElo.current))

      ratings.updated(winner, newWinnerElo).updated(loser, newLoserElo)
    }
  }

  def sortedElo[T](elo: Map[T, Elo[T]]): Seq[(T, Elo[T])] = elo.toSeq.sortBy(-_._2.current)

  def displaySortedElo[T](elo: Map[T, Elo[T]], minGames: Int = 0): Unit = {
    println(f"${"Player"}%-23s ${"ELO Rating"}%-10s")
    println("=" * 34)
    sortedElo(elo)
      .filter(_._2.audit.size >= minGames)
      .foreach { case (player, rating) =>
        val visibleLength = player.toString.replaceAll("\u001b\\[[;\\d]*m", "").length
        val padding = 27 - visibleLength
        val paddedPlayer = player.toString + " " * padding
        val formattedRating = f"${rating.current}%.2f"  // Correctly formatted rating with 2 decimal places
        println(s"$paddedPlayer$formattedRating")
      }
    println("")
  }

}
