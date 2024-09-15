import elo.{PlayerCharacterElo, VillainElo}
import model.Player._
import model.Villain._
import model.Tournament.{bannedVillains, ownedVillains}
import model.game.DuelGame
import util.EloOps.{calculateElo, displaySortedElo}
import io.Filesystem.{allGames, nonSoloGames}
import model.{Player, PlayerVillain, Tournament, Villain}
import util.GameOps.getNextGame
import util.Stats.{generateRecordStats, getPlayerStats}

object Main {

  def showAllPlayerStats(games: List[DuelGame]): Unit = {
    println(getPlayerStats(games, alec).detailedStats)
    println(getPlayerStats(games, dennis).detailedStats)
    println(getPlayerStats(games, michael).detailedStats)
  }

  //    val getNextGame = args.contains("--getNextGame")
  def main(args: Array[String]): Unit = {
    println("")

    val games = allGames.sortBy(_.date)

    val allPlayers: Player => Boolean = _ => true
    def onlyPlayer(player: Player): Player => Boolean = _ == player
    generateRecordStats(games, onlyPlayer(alec)).foreach(println)
//    generateRecordStats(games, allPlayers).foreach(println)

    showAllPlayerStats(nonSoloGames)

    val tournament5 = Tournament("005", ownedVillains -- bannedVillains)
    println(getNextGame(tournament5, alec, dennis).get)

    val eloRatings = calculateElo[PlayerVillain](games, PlayerCharacterElo)
    displaySortedElo(eloRatings, minGames = 2)

    val villainEloRatings = calculateElo[Villain](games, VillainElo)
    displaySortedElo(villainEloRatings)

    val eloHistoryTarget = PlayerVillain(alec, Jafar)
    println(s"${eloHistoryTarget.player}'s ${eloHistoryTarget.villain} Rating Changes:")
    eloRatings(eloHistoryTarget).showEloHistory()

    // calculate win rate only using the first time a villain was played

    // TODO: make the playercharacter/villain elo types parameterized somehow
    // TODO: clean up stats/record classes, they probably aren't needed? or come up with some useful model then that is
    // TODO: make Game trait, make SoloGame separate (make new files for records)
  }

}
