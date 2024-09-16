import elo.{PlayerCharacterElo, VillainElo}
import model.Player._
import model.Villain._
import model.Tournament._
import model.game.DuelGame
import stats.EloOps.{calculateElo, displaySortedElo}
import io.Filesystem.{allGames, nonSoloGames}
import model.{Player, PlayerVillain, Tournament, Villain}
import stats.PlayerRecord.showPlayCounts
import util.GameOps.getNextGame
import stats.Stats.{generateRecords, getPlayerStats, showRecords}

object Main {

  def showPlayerStats(games: List[DuelGame], players: List[Player]): Unit = players.foreach { p =>
    println(getPlayerStats(games, p).detailedStats)
  }

  def showPlayerRecords(games: List[DuelGame], player: Option[Player]): Unit = {
    def filterPlayer: Player => Boolean = (p: Player) => player.forall(_ == p)
    val playerRecords = generateRecords(games, filterPlayer)

    player match {
      case Some(p) => println(s"${p.toString}'s Records:")
      case None => println("All Records:")
    }
    showRecords(playerRecords)
  }

  def main(args: Array[String]): Unit = {
    println("")

    println("Tournament 1:")
    showPlayerStats(tournament1, List(alec, dennis))
    println("Tournament 2:")
    showPlayerStats(tournament2, List(alec, dennis))
    println("Tournament 3:")
    showPlayerStats(tournament3, List(alec, dennis))
    println("Tournament 4:")
    showPlayerStats(tournament4, List(alec, dennis))
    println("\nAll Games")
    showPlayerStats(nonSoloGames, List(alec, dennis, michael))

//    val games = tournament4
    val games = allGames.sortBy(_.date)

    showPlayerRecords(games, Some(alec))
    showPlayerRecords(games, Some(dennis))
    showPlayerRecords(games, None)

    val villainEloRatings = calculateElo[Villain](games, VillainElo)
    displaySortedElo(villainEloRatings)

    val playerEloRatings = calculateElo[PlayerVillain](games, PlayerCharacterElo)
    displaySortedElo(playerEloRatings, minGames = 2)

//    val eloHistoryTarget = PlayerVillain(alec, Jafar)
//    println(s"${eloHistoryTarget.player}'s ${eloHistoryTarget.villain} Rating Changes:")
//    playerEloRatings(eloHistoryTarget).showEloHistory()

//    showPlayCounts(games, alec)

    val tournament5 = Tournament("005", ownedVillains -- bannedVillains)
    println(getNextGame(tournament5, alec, dennis).get)
  }

}
