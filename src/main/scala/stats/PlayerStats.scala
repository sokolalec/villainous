package stats

import model.{Player, Villain}
import model.game.DuelGame

case class PlayerStats(player: Player, wins: List[DuelGame], losses: List[DuelGame]) {

  override def toString: String = {
    val winRate = wins.length.toDouble / (wins.length + losses.length) * 100
    val formattedPercentage: String = f"$winRate%.2f"

    s"""
       |Displaying stats for '$player':
       |  Total Wins: ${wins.length}
       |  Total Losses: ${losses.length}
       |  Win Rate: $formattedPercentage
       |""".stripMargin
  }

  lazy val detailedStats: String = {
    val firstWins = wins.count(g => g.winner == g.firstPlayer)
    val firstLosses = losses.count(g => g.loser == g.firstPlayer)

    val winRate = firstWins.toDouble / (firstWins + firstLosses) * 100
    val formattedPercentage: String = f"$winRate%.2f"

    val secondWinRate = (wins.length - firstWins).toDouble / (wins.length - firstWins + losses.length - firstLosses) * 100
    val secondFormattedPercentage: String = f"$secondWinRate%.2f"

    this.toString + s"""
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

object PlayerStats {

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
