import model.environment.Player._
import model.environment.{Player, Villain}
import model.environment.Villain._
import model.event.Tournament
import model.event.Tournament.{bannedVillains, ownedVillains}
import model.game.DuelGame
import model.stats.{Elo, PlayerCharacter, VillainElo}
import util.EloOps.{displaySortedElo, displaySortedVillainElo, getBestCharacters, getWorstCharacters, playerElo, sortedElo, villainElo}
import util.Filesystem.{allGames, multiplayerGames, tournamentGames}
import util.GameOps.{getNextGame, getVillainsRemaining}
import util.Stats.{generateRecordStats, getPlayerStats}

object Main {

  def showPlayerStats(games: List[DuelGame]): Unit = {
    println(getPlayerStats(games, alec).detailedStats)
    println(getPlayerStats(games, dennis).detailedStats)
    println(getPlayerStats(games, michael).detailedStats)
  }

  def showVillainEloRatings(eloRatings: Map[Villain, VillainElo]): Unit = {
    displaySortedVillainElo(eloRatings)
  }

  def showEloRatings(eloRatings: Map[PlayerCharacter, Elo]): Unit = {
    displaySortedElo(eloRatings)

    val alecBest = getBestCharacters(eloRatings, alec, 3)
    val dennisBest = getBestCharacters(eloRatings, dennis, 3)

    val alecWorst = getWorstCharacters(eloRatings, alec, 3)
    val dennisWorst = getWorstCharacters(eloRatings, alec, 3)

//    alecBest.foreach(character => {
//      println(character._1.toString)
//      character._2.showEloHistory()
//      println("")
//    })
  }

  def showPlayCounts(games: Seq[DuelGame], player: Player): Map[Villain, Int] = {
    val counts = games.flatMap { game =>
        val winnerVillainCount = if (game.winnerPlayer == player) Some(game.winner) else None
        val loserVillainCount = if (game.loserPlayer == player) Some(game.loser) else None
        List(winnerVillainCount, loserVillainCount).flatten
      }
      .groupBy(identity)
      .view.mapValues(_.size)
      .toMap

    println(s"$player games:")
    val villainCount: Seq[(Villain, Int)] = counts.toSeq.sortBy(-_._2)
    villainCount.foreach { case (villain, count) =>
      println(s"  $villain: $count")
    }
    println("")

    counts
  }

  def main(args: Array[String]): Unit = {
    val games = allGames

    val allPlayers: Player => Boolean = _ => true
    val onlyPlayer: Player => Boolean = _ == dennis
    val allRecords = generateRecordStats(games, allPlayers)
    allRecords.foreach(println)

    val alecLosses = games.filter(g => g.loserPlayer == alec).map(_.loser).distinct.toSet
    println(s"\nAlec Never Lost With: ${ownedVillains -- alecLosses}")
    val dennisWins = games.filter(g => g.winnerPlayer == dennis).map(_.winner).distinct.toSet
    println(s"\nDennis Never Won With: ${ownedVillains -- dennisWins}")

//    println(allGames.filter(g => g.loser == DrFacilier))

    // showPlayerStats(games)

    val tournament5 = Tournament("005", ownedVillains -- bannedVillains)
    println(getNextGame(tournament5, alec, dennis).get)

    val eloRatings: Map[PlayerCharacter, Elo] = playerElo(games)
    showEloRatings(eloRatings)

    val alecRemaining = getVillainsRemaining(tournament5, alec).map(PlayerCharacter(alec, _))
    // println(s"alec remaining - $alecRemaining")
    val dennisRemaining = getVillainsRemaining(tournament5, dennis).map(PlayerCharacter(dennis, _))
    // println(s"dennis remaining - $dennisRemaining")

    val villainEloRatings = villainElo(allGames)
    showVillainEloRatings(villainEloRatings)

    val alecCounts = showPlayCounts(games, alec)
    val dennisCounts = showPlayCounts(games, dennis)

    showEloRatings(eloRatings.filter(kv => {
      val (p, v) = kv
      if (p.player == alec) alecCounts(p.villain) >= 5
      else dennisCounts(p.villain) >= 5
    }))

    println(multiplayerGames)

    // calculate win rate only using the first time a villain was played

    // TODO: make the playercharacter/villain elo types parameterized somehow
    // TODO: clean up stats/record classes, they probably aren't needed? or come up with some useful model then that is
    // TODO: make Game trait, make SoloGame separate (make new files for records)
  }

}
