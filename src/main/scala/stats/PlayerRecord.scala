package stats

import model.{Player, Villain}
import model.game.DuelGame

case class PlayerRecord(player: Player, record: Record) {

  private val wins = record.wins
  private val winCount = record.wins.length
  private val losses = record.losses
  private val lossCount = record.losses.length

  override def toString: String = {
    val winRate = winCount.toDouble / (winCount + lossCount) * 100
    val formattedPercentage: String = f"$winRate%.2f"

    s"""
       |  '$player': $formattedPercentage""".stripMargin
  }

  private def getWinRates: (Double, Double) = {
    val firstWins = wins.count(g => g.winner == g.firstPlayer)
    val firstLosses = losses.count(g => g.loser == g.firstPlayer)

    val firstWinRate = firstWins.toDouble / (firstWins + firstLosses) * 100
    val secondWinRate = (winCount - firstWins).toDouble / (winCount - firstWins + lossCount - firstLosses) * 100

    (firstWinRate, secondWinRate)
  }

  lazy val detailedStats: String = {
    val (firstWinRate, secondWinRate) = getWinRates

    val formattedPercentage: String = f"$firstWinRate%.2f"
    val secondFormattedPercentage: String = f"$secondWinRate%.2f"

    this.toString + s"""
       |  '$player' (going first): $formattedPercentage
       |  '$player' (going second): $secondFormattedPercentage
       |  """.stripMargin
  }

  lazy val auditStats: String = {
    val firstWins = wins.count(g => g.winner == g.firstPlayer)
    val firstLosses = losses.count(g => g.loser == g.firstPlayer)

    val winRate = firstWins.toDouble / (firstWins + firstLosses) * 100
    val formattedPercentage: String = f"$winRate%.2f"

    val secondWinRate = (wins.length - firstWins).toDouble / (wins.length - firstWins + losses.length - firstLosses) * 100
    val secondFormattedPercentage: String = f"$secondWinRate%.2f"

    this.toString +
      s"""
         |Displaying stats for '$player' (going first):
         |  Total Wins: $firstWins
         |  Total Losses: $firstLosses
         |  Win Rate: $formattedPercentage
         |
         |Displaying stats for '$player' (going second):
         |  Total Wins: ${wins.length - firstWins}
         |  Total Losses: ${losses.length - firstLosses}
         |  Win Rate: $secondFormattedPercentage
         |""".stripMargin
  }

}

object PlayerRecord {

  def getPlayCounts(games: Seq[DuelGame], player: Player): Map[Villain, Int] = {
    games.flatMap { game =>
      val winnerVillainCount = if (game.winnerPlayer == player) Some(game.winner) else None
      val loserVillainCount = if (game.loserPlayer == player) Some(game.loser) else None
      List(winnerVillainCount, loserVillainCount).flatten
    }
    .groupBy(identity)
    .view.mapValues(_.size)
    .toMap
  }

  def showPlayCounts(games: Seq[DuelGame], player: Player): Map[Villain, Int] = {
    val counts = getPlayCounts(games, player)

    println(s"$player games:")
    val villainCount: Seq[(Villain, Int)] = counts.toSeq.sortBy(-_._2)
    villainCount.foreach { case (villain, count) =>
      println(s"  $villain: $count")
    }
    println("")

    counts
  }

}
