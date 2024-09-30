import elo.{PlayerCharacterElo, VillainElo}
import io.Filesystem.{allGames, legalGames, nonSoloGames, soloGames}
import model.Player._
import model.Villain._
import model.game.DuelGame
import model.{Player, PlayerVillain, Villain}
import stats.EloOps.{calculateElo, displaySortedElo}
import stats.PlayerRecord.getPlayCounts
import stats.Stats.{generateRecords, getPlayerStats, showRecords}
import tournaments._
import util.GameOps.getNextGame

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

//    println("Tournament 1:")
//    showPlayerStats(tournament1, List(alec, dennis))
//    println("Tournament 2:")
//    showPlayerStats(tournament2, List(alec, dennis))
//    println("Tournament 3:")
//    showPlayerStats(tournament3, List(alec, dennis))
//    println("Tournament 4:")
//    showPlayerStats(tournament4, List(alec, dennis))
//    println("Tournament 5:")
//    showPlayerStats(tournament5, List(alec, dennis))
    println("\nAll Games:")
    showPlayerStats(nonSoloGames, List(alec, dennis, michael))
    println("\nSolo Games:")
    showPlayerStats(soloGames, List(alec, dennis))

//    val games = soloGames
//    val games = tournament4
    val games = legalGames

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

    //      ############################################
    //      #    Alec     #  Dennis   #  First Player  #
    //      #  Madam Mim  #  Ratigan  #   Madam Mim    #
    //      ############################################

    println(Tournament6.score())
    println(Tournament6.generateNextGame(alec, dennis).get)

    println("")
  }

}
