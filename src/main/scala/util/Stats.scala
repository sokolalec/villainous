package util

import model.environment.Villain
import model.event.Game
import model.stats.{PlayerStats, Record, RecordStats}
import model.{Player, stats}

object Stats {

  def getPlayerWins(games: List[Game], player: Player): List[Game] = games.filter(_.winnerPlayer == player)

  def getPlayerLosses(games: List[Game], player: Player): List[Game] = games.filter(_.loserPlayer == player)

  def getPlayerStats(games: List[Game], player: Player): PlayerStats =
    stats.PlayerStats(player, getPlayerWins(games, player), getPlayerLosses(games, player))

  def getVillainGames(games: List[Game], villain: Villain): List[Game] =
    games.filter(g => g.winner == villain || g.loser == villain)

  def higherWinRate(a: RecordStats, b: RecordStats): Boolean = {
    val aRate = a.wins.toDouble / (a.wins + a.losses)
    val bRate = b.wins.toDouble / (b.wins + b.losses)
    b.wins + b.losses == 0 || (aRate > bRate || (aRate == bRate && a.wins > b.wins))
  }

  def generateRecords(games: Seq[Game]): List[Record] = {
    val villains = Villain.values
    villains.map { villain =>
      val wins = games.filter(_.winner == villain).toList
      val losses = games.filter(_.loser == villain).toList
      Record(villain, wins, losses)
    }.toList
  }

  def generateRecordStats(games: List[Game]): List[RecordStats] = {
    generateRecords(games)
      .map(r => RecordStats(r.villain, r.wins.size, r.losses.size))
      .sortWith(higherWinRate)
  }

}
