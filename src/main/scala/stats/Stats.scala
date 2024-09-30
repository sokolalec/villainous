package stats

import model.game.DuelGame
import model.{Player, Villain}

object Stats {

  /**
   * Excludes SoloGames, because that wouldn't make sense.
   */
  def getPlayerStats(games: List[DuelGame], player: Player): PlayerRecord = {
    val wins = games.filter(_.winnerPlayer == player)
    val losses = games.filter(_.loserPlayer == player)
    PlayerRecord(player, Record(wins, losses))
  }

  /**
   * Used to sort when displaying records.
   */
  private def higherWinRate(a: Record, b: Record): Boolean = {
    val aRate = a.wins.size.toDouble / (a.wins.size + a.losses.size)
    val bRate = b.wins.size.toDouble / (b.wins.size + b.losses.size)

    if (b.wins.isEmpty && a.wins.isEmpty) a.losses.size < b.losses.size
    else b.wins.size + b.losses.size == 0 || (aRate > bRate || (aRate == bRate && a.wins.size > b.wins.size))
  }

  /**
   * Returns a Map[Villain, Record(wins, losses)]
   */
  def generateRecords(games: Seq[DuelGame], include: Player => Boolean): Map[Villain, Record] =
    Villain.values.map { villain =>
      val wins = games.filter(g => g.winner == villain && include(g.winnerPlayer)).toList
      val losses = games.filter(g => g.loser == villain && include(g.loserPlayer)).toList
      (villain, Record(wins, losses))
    }.toMap

  /**
   * Villain: (wins, losses)
   */
  def showRecords(records: Map[Villain, Record]): Unit = {
    val sortedRecords = records.toList.sortWith { case ((_, recordA), (_, recordB)) =>
      higherWinRate(recordA, recordB)
    }
    sortedRecords.foreach { case (v, r) => println(s"  ${v.toString}: (${r.wins.size}, ${r.losses.size})") }
    println("")
  }

}
