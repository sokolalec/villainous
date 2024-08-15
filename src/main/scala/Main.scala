import model.Player._
import model.stats.{Elo, PlayerCharacter}
import util.EloOps.{calculateElo, displaySortedElo, getBestCharacters, getWorstCharacters, sortedElo}
import util.Filesystem.tournamentGames
import util.GameOps.{getNextGame, getVillainsRemaining}
import util.Stats.{generateRecordStats, getPlayerStats}

object Main {

  def showPlayerStats(): Unit = {
    println(getPlayerStats(tournamentGames, alec).detailedStats)
    println(getPlayerStats(tournamentGames, dennis).detailedStats)
  }

  def showEloRatings(eloRatings: Map[PlayerCharacter, Elo]): Unit = {
    displaySortedElo(eloRatings)

    val alecBest = getBestCharacters(eloRatings, alec, 3)
    val dennisBest = getBestCharacters(eloRatings, dennis, 3)

    val alecWorst = getWorstCharacters(eloRatings, alec, 3)
    val dennisWorst = getWorstCharacters(eloRatings, alec, 3)

    alecBest.foreach(character => {
      println(character._1.toString)
      character._2.showEloHistory()
      println("")
    })
  }

  def main(args: Array[String]): Unit = {

    val allRecords = generateRecordStats(tournamentGames)
    allRecords.foreach(println)

    // showPlayerStats()

    println(getNextGame("004", alec, dennis).get)

    val eloRatings: Map[PlayerCharacter, Elo] = calculateElo(tournamentGames)
    showEloRatings(eloRatings)

    // calculate win rate only using the first time a villain was played
  }

}
