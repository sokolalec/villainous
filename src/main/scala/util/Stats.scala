package util

import model.{Player, Villain}
import model.game.DuelGame
import stats.{PlayerStats, Record, RecordStats}

object Stats {

  def getPlayerWins(games: List[DuelGame], player: Player): List[DuelGame] = games.filter(_.winnerPlayer == player)

  def getPlayerLosses(games: List[DuelGame], player: Player): List[DuelGame] = games.filter(_.loserPlayer == player)

  def getPlayerStats(games: List[DuelGame], player: Player): PlayerStats = {
    val multiPlayerGames = games.filterNot(g => g.loserPlayer == g.winnerPlayer)
    PlayerStats(player, getPlayerWins(multiPlayerGames, player), getPlayerLosses(multiPlayerGames, player))
  }

  private def higherWinRate(a: RecordStats, b: RecordStats): Boolean = {
    val aRate = a.wins.toDouble / (a.wins + a.losses)
    val bRate = b.wins.toDouble / (b.wins + b.losses)

    if (b.wins == 0 && a.wins == 0) a.losses < b.losses
    else b.wins + b.losses == 0 || (aRate > bRate || (aRate == bRate && a.wins > b.wins))
  }

  def generateRecords(games: Seq[DuelGame], include: Player => Boolean): List[Record] = {
    val villains = Villain.values
    villains.map { villain =>
      val wins = games.filter(g => g.winner == villain && include(g.winnerPlayer)).toList
      val losses = games.filter(g => g.loser == villain && include(g.loserPlayer)).toList
      Record(villain, wins, losses)
    }.toList
  }

  def generateRecordStats(games: List[DuelGame], include: Player => Boolean): List[RecordStats] = {
    generateRecords(games, include)
      .map(r => RecordStats(r.villain, r.wins.size, r.losses.size))
      .sortWith(higherWinRate)
  }

}
