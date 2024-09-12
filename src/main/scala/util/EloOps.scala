package util

import model.environment.{Player, Villain}
import model.event.{EloChange, VillainEloChange}
import model.game.DuelGame
import model.stats.{Elo, PlayerCharacter, VillainElo}

object EloOps {

  /**
   * Determines general sensitivity to ELO changes
   */
  def getEloFactor(villainGames: Int, maxVillainGames: Int): Int = {
    val eloFactor = 64
    // can do fancier stuff like this but meh
    // Math.round(villainGames.toDouble / maxVillainGames * eloFactor / 2 + eloFactor / 2).toInt
    eloFactor
  }

  def calculateNewRating(winnerRating: Double, loserRating: Double, winnerGames: Int, loserGames: Int, maxGames: Int): (Double, Double) = {
    val expectedWinner = 1.0 / (1.0 + Math.pow(10, (loserRating - winnerRating) / 400))
    val expectedLoser = 1.0 / (1.0 + Math.pow(10, (winnerRating - loserRating) / 400))

    val newWinnerRating = winnerRating + getEloFactor(winnerGames, maxGames) * (1 - expectedWinner)
    val newLoserRating = loserRating + getEloFactor(loserGames, maxGames) * (0 - expectedLoser)

    (newWinnerRating, newLoserRating)
  }

  def getMaxGames(games: List[DuelGame]): Map[PlayerCharacter, Int] = {
    val gameCount: Map[PlayerCharacter, Int] = Map.empty.withDefaultValue(0)

    games.foldLeft(gameCount) { (counts, game) =>
      val winner = PlayerCharacter(game.winnerPlayer, game.winner)
      val loser = PlayerCharacter(game.loserPlayer, game.loser)
      counts.updated(winner, counts(winner) + 1).updated(loser, counts(loser))
    }
  }

  def villainElo(games: List[DuelGame]): Map[Villain, VillainElo] = {
    val gameCounts = getMaxGames(games)
    val maxGames = gameCounts.toList.map(_._2).max

    val initialRatings: Map[Villain, VillainElo] = Map.empty.withDefaultValue(VillainElo(1500.0, List.empty))

    games.foldLeft(initialRatings) { (ratings, game) =>
      val winner = PlayerCharacter(game.winnerPlayer, game.winner)
      val loser = PlayerCharacter(game.loserPlayer, game.loser)

      val winnerElo = ratings(game.winner)
      val loserElo = ratings(game.loser)

      val (newWinnerRating, newLoserRating) = calculateNewRating(winnerElo.current, loserElo.current, gameCounts(winner), gameCounts(loser), maxGames)

      val newWinnerElo = VillainElo(newWinnerRating, winnerElo.audit :+ VillainEloChange(game.loser, newWinnerRating - winnerElo.current))
      val newLoserElo = VillainElo(newLoserRating, loserElo.audit :+ VillainEloChange(game.winner, newLoserRating - loserElo.current))

      ratings.updated(game.winner, newWinnerElo).updated(game.loser, newLoserElo)
    }
  }

  def playerElo(games: List[DuelGame]): Map[PlayerCharacter, Elo] = {
    val gameCounts = getMaxGames(games)
    val maxGames = gameCounts.toList.map(_._2).max

    val initialRatings: Map[PlayerCharacter, Elo] = Map.empty.withDefaultValue(Elo(1500.0, List.empty))

    games.foldLeft(initialRatings) { (ratings, game) =>
      val winner = PlayerCharacter(game.winnerPlayer, game.winner)
      val loser = PlayerCharacter(game.loserPlayer, game.loser)

      val winnerElo = ratings(winner)
      val loserElo = ratings(loser)

      val (newWinnerRating, newLoserRating) = calculateNewRating(winnerElo.current, loserElo.current, gameCounts(winner), gameCounts(loser), maxGames)

      val newWinnerElo = Elo(newWinnerRating, winnerElo.audit :+ EloChange(loser, newWinnerRating - winnerElo.current))
      val newLoserElo = Elo(newLoserRating, loserElo.audit :+ EloChange(winner, newLoserRating - loserElo.current))

      ratings.updated(winner, newWinnerElo).updated(loser, newLoserElo)
    }
  }

  def sortedElo(elo: Map[PlayerCharacter, Elo]): Seq[(PlayerCharacter, Elo)] = elo.toSeq.sortBy(-_._2.current)

  def sortedVillainElo(elo: Map[Villain, VillainElo]): Seq[(Villain, VillainElo)] = elo.toSeq.sortBy(-_._2.current)

  def getBestCharacters(eloRatings: Map[PlayerCharacter, Elo], player: Player, n: Int): List[(PlayerCharacter, Elo)] = {
    sortedElo(eloRatings.filter(_._1.player == player)).take(n).toList
  }

  def getWorstCharacters(eloRatings: Map[PlayerCharacter, Elo], player: Player, n: Int): List[(PlayerCharacter, Elo)] = {
    sortedElo(eloRatings.filter(_._1.player == player)).reverse.take(n).toList
  }

  def displaySortedVillainElo(elo: Map[Villain, VillainElo]): Unit = {
    println(f"${"Player"}%-19s ${"ELO Rating"}%-10s")
    println("=" * 30)
    sortedVillainElo(elo).foreach { case (player, rating) =>
      println(f"${player}%-22s ${rating.current}%-10.2f")
    }
    println("")
  }

  def displaySortedElo(elo: Map[PlayerCharacter, Elo]): Unit = {
    println(f"${"Player"}%-22s ${"ELO Rating"}%-10s")
    println("=" * 33)
    sortedElo(elo).foreach { case (player, rating) =>
      println(f"${player}%-25s ${rating.current}%-10.2f")
    }
    println("")
  }

}
