import elo.{Elo, EloCalculable, PlayerVillainElo, VillainElo}
import game.DuelGame
import io.Filesystem.{allGames, legalGames}
import model.Player._
import model.{Player, PlayerVillain, Villain}
import stats.EloOps.{calculateElo, displaySortedElo}
import stats.Stats
import stats.Stats.{generateRecords, getPlayerStats, showRecords, showWinRates}
import tournaments._

object Main {

  type PlayerElo = Map[PlayerVillain, Elo[PlayerVillain]]

  def showPlayerStats(games: Seq[DuelGame], players: Seq[Player]): Unit = players.foreach { p =>
    println(getPlayerStats(games, p).detailedStats)
  }

  def showPlayerRecords(games: Seq[DuelGame], player: Option[Player]): Unit = {
    def filterPlayer: Player => Boolean = (p: Player) => player.forall(_ == p)
    val playerRecords = generateRecords(games, filterPlayer)

    player match {
      case Some(p) => println(s"${p.toString}'s Records:")
      case None => println("All Records:")
    }
    showRecords(playerRecords)
  }

  def showPlayerVillainGames(games: Seq[DuelGame], pc: PlayerVillain): Seq[DuelGame] = {
    val g = games.filter(g =>
      (g.winnerPlayer == pc.player && g.winner == pc.villain) || (g.loserPlayer == pc.player && g.loser == pc.villain)
    )
    g.foreach(println)
    g
  }

  def showEloHistory(playerEloRatings: PlayerElo)(player: Player, villains: Iterable[Villain]): Unit = {
    villains.map(v => PlayerVillain(player, v)).foreach { pv =>
      println(s"${pv.player}'s ${pv.villain} Rating Changes:")
      playerEloRatings(pv).showEloHistory()
      println("")
    }
  }

  def displayEloDifference(elo: Map[PlayerVillain, Elo[PlayerVillain]], minGames: Int = 0): Unit = {
    val villainDiff = Villain.values.map(v => {
      val alecPv = PlayerVillain(alec, v)
      val dennisPv = PlayerVillain(dennis, v)

      val alecDefault = PlayerVillainElo.defaultElo(alecPv)
      val dennisDefault = PlayerVillainElo.defaultElo(dennisPv)
      (v, elo.getOrElse(alecPv, alecDefault).current - elo.getOrElse(dennisPv, dennisDefault).current)
    })
    // Math.abs

    val diffSorted = villainDiff.sortBy(_._2).reverse

    println(f"${"Player"}%-19s ${"ELO Difference"}%-10s")
    println("=" * 34)
    diffSorted.foreach { case (villain, diff) =>
        val visibleLength = villain.toString.replaceAll("\u001b\\[[;\\d]*m", "").length
        val padding = 27 - visibleLength
        val paddedPlayer = villain.toString + " " * padding
        val formattedRating = f"${diff}%.2f" // 2 decimal places
        println(s"$paddedPlayer$formattedRating")
      }
    println("")
  }

  def main(args: Array[String]): Unit = {
    val games = legalGames // Tournament13.games().toList legalGames soloGames allGames etc

//    showPlayerStats(games, List(alec, dennis, michael))

    showPlayerRecords(games, Some(alec))
    showPlayerRecords(games, Some(dennis))
    showPlayerRecords(games, None)

    val playerGrouping: PlayerVillain => String = pv => pv.player.toString

    val playerEloRatings: Map[PlayerVillain, Elo[PlayerVillain]] = calculateElo[PlayerVillain](games, PlayerVillainElo)
//    displayEloDifference(playerEloRatings)
    displaySortedElo[PlayerVillain](playerEloRatings, minGames = 2, grouping = Some(playerGrouping))
    println("")

    val villainEloRatings = calculateElo[Villain](games, VillainElo)
    displaySortedElo(villainEloRatings, grouping = None)
    println("")

    Tournament14.generateNextGame(dennis, alec)

    Tournament14.showScores(alec, dennis)

    println("")
    println("")
    println("")


//    Stats.showWinRates(games, Tournament14.brackets)


//    val historyVillains = Set(DrFacilier)
//    showEloHistory(playerEloRatings)(dennis, historyVillains)
//
//    val historyVillains2 = Set(ShereKhan)
//    showEloHistory(playerEloRatings)(alec, historyVillains2)
//
//    println(proposeMultiDuelGame(Set(alec, dennis), 2).get)
//
//    println("Pairings:")
//    val pairings = games.map(g => Set(g.winner, g.loser)).distinct
//    Villain.values.foreach(v => println(s"$v - ${pairings.count(_.contains(v))}"))
//    println("")



//    multiplayerGames.foreach(g => println(g))
//    games.filter(g => g.winnerPlayer == michael || g.loserPlayer == michael).foreach(g => println(g))

//    println(s"All Time Scores:\n  Alec: ${allTimeScore(games, alec)}\n  Dennis: ${allTimeScore(games, dennis)}")
    println("")
    println("")
    println("")
  }

}
