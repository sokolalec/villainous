import elo.{Elo, PlayerVillainElo, VillainElo}
import game.{DuelGame, PlayableGame}
import io.Filesystem.{allGames, legalGames, nonSoloGames, soloGames}
import model.Player._
import model.Villain._
import model.{Player, PlayerVillain, Villain}
import stats.EloOps.{calculateElo, displaySortedElo}
import stats.PlayerRecord.numGamesGoingFirst
import stats.Stats.{generateRecords, getPlayerStats, showRecords}
import tournaments._
import util.GameOps.proposeMultiDuelGame

object Main {

  type PlayerElo = Map[PlayerVillain, Elo[PlayerVillain]]

  def showPlayerStats(games: Seq[DuelGame], players: Seq[Player]): Unit = players.foreach { p =>
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

  def main(args: Array[String]): Unit = {
    println("\n\nAll Games:")
    showPlayerStats(nonSoloGames, List(alec, dennis, michael))
    println("\nSolo Games:")
    showPlayerStats(soloGames, List(alec, dennis))

    val games = legalGames // legalGames, soloGames, etc

    showPlayerRecords(games, Some(alec))
    showPlayerRecords(games, Some(dennis))
    showPlayerRecords(games, None)

    val villainEloRatings = calculateElo[Villain](games, VillainElo)
    displaySortedElo(villainEloRatings)

    val playerEloRatings = calculateElo[PlayerVillain](games, PlayerVillainElo)
    displaySortedElo(playerEloRatings, minGames = 2)

    println("")

    val historyVillains = Set(DrFacilier)
    showEloHistory(playerEloRatings)(dennis, historyVillains)

    val historyVillains2 = Set(PrinceJohn, Maleficent, KingCandy)
    showEloHistory(playerEloRatings)(alec, historyVillains2)

    println("")

//    println(s"Total Games: ${nonSoloGames.length}")
//    val alecFirst = numGamesGoingFirst(alec, nonSoloGames)
//    val dennisFirst = numGamesGoingFirst(dennis, nonSoloGames)
//    println(s"alec: $alecFirst   dennis: $dennisFirst")

//    println(proposeMultiDuelGame(Set(alec, dennis), 2).get)

    Tournament9.generateNextGame(alec, dennis)

    println("")
    println("")
  }

}
