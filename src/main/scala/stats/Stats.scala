package stats

import game.DuelGame
import model.{Player, Villain}
import tournaments.Tournament11
import tournaments.Tournament13.isUpperBracket

object Stats {

  /**
   * Excludes SoloGames, because that wouldn't make sense.
   */
  def getPlayerStats(games: Seq[DuelGame], player: Player): PlayerRecord = {
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
    sortedRecords.foreach { case (v, r) =>
      val winRate = (r.wins.size * 1.0 / (r.wins.size + r.losses.size) * 100).toInt
      println(s"  ($winRate%) ${v.toString}: (${r.wins.size}, ${r.losses.size})") }
    println("")
  }

  // Maps a basic RGB value (0–255 each) to a 256-color ANSI color code
  def rgbToAnsi256(r: Int, g: Int, b: Int): Int = {
    val levels = Array(0, 95, 135, 175, 215, 255)

    def findNearest(x: Int) = levels.minBy(l => math.abs(l - x))

    val red = findNearest(r)
    val green = findNearest(g)
    val blue = findNearest(b)

    16 + (levels.indexOf(red) * 36) + (levels.indexOf(green) * 6) + levels.indexOf(blue)
  }

  // Colors text based on percent from 0 (red) -> 50 (yellow) -> 100 (green)
  def colorizePercent(pct: Long): String = {
    val text = pct.toInt.toString + "%"
    val clampedPct = math.max(0, math.min(100, pct.toInt))

    val (r, g, b) = if (clampedPct <= 50) {
      val green = (clampedPct * 255) / 50
      (255, green, 0)
    } else {
      val red = 255 - ((clampedPct - 50) * 255) / 50
      (red, 255, 0)
    }

    val colorCode = rgbToAnsi256(r, g, b)

    s"\u001b[38;5;${colorCode}m$text\u001b[0m"
  }

  def showWinRates(games: Seq[DuelGame], brackets: Seq[Set[Villain]]): Unit = {
    brackets.foreach(b => {
      println(s"Bracket ${brackets.indexOf(b)}:")
      b.foreach(v => {
        val lowerWins = games.count(g => g.winner == v && isUpperBracket(v, g.loser, brackets))
        val lowerLosses = games.count(g => g.loser == v && isUpperBracket(v, g.winner, brackets))

        val upperWins = games.count(g => g.winner == v && isUpperBracket(g.loser, v, brackets))
        val upperLosses = games.count(g => g.loser == v && isUpperBracket(g.winner, v, brackets))

        val sameWins = games.count(g => g.winner == v && !(isUpperBracket(v, g.loser, brackets) || isUpperBracket(g.loser, v, brackets)))
        val sameLosses = games.count(g => g.loser == v && !(isUpperBracket(g.winner, v, brackets) || isUpperBracket(v, g.winner, brackets)))

        val totalWins = lowerWins + upperWins + sameWins
        val totalLosses = lowerLosses + upperLosses + sameLosses
        val totalPercent = math.round(totalWins.toDouble / (totalWins + totalLosses) * 100).toInt
        println(s"  $v Overall: ${colorizePercent(totalPercent)}  ($totalWins, $totalLosses)")
        if (lowerWins + lowerLosses > 0) println(s"    vs Lower Brackets: ${colorizePercent(math.round(lowerWins.toDouble / (lowerWins + lowerLosses) * 100))}%  ($lowerWins, $lowerLosses)")
        if (sameWins + sameLosses > 0) println(s"    vs Same Bracket:   ${colorizePercent(math.round(sameWins.toDouble / (sameWins + sameLosses) * 100))}%  ($sameWins, $sameLosses)")
        if (upperWins + upperLosses > 0) println(s"    vs Upper Brackets: ${colorizePercent(math.round(upperWins.toDouble / (upperWins + upperLosses) * 100))}%  ($upperWins, $upperLosses)")
        println("")
      })
    })
  }

}
