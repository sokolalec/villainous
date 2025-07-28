package tournaments
import game.DuelGame
import io.PlayableDuel
import model.Player.{alec, dennis}
import model.{Player, Villain}
import util.RNG.getRandom

object Tournament13 extends Tournament {
  override val version: String = "013"
  override val availableVillains: Set[Villain] = Set.empty

  override val finished: Boolean = false // 21 points

  val brackets: Seq[Set[Villain]] = Tournament11.brackets

  def isUpperBracket(b1: Set[Villain], b2: Set[Villain]): Boolean = brackets.indexOf(b1) < brackets.indexOf(b2)

  def isUpperBracket(v1: Villain, v2: Villain, brackets: Seq[Set[Villain]]): Boolean = {
    val v1Bracket = brackets.indexWhere(_.contains(v1))
    val v2Bracket = brackets.indexWhere(_.contains(v2))
    v1Bracket < v2Bracket
  }

  def bracketPoints(b1: Set[Villain], b2: Set[Villain]): Int = Math.abs(brackets.indexOf(b1) - brackets.indexOf(b2))

  def getBracketMatchups(b1: Set[Villain], b2: Set[Villain]): Seq[DuelGame] = games().filter {
    g => (b1.contains(g.winner) || b2.contains(g.winner)) && (b1.contains(g.loser) || b2.contains(g.loser))
  }

  def score(player: Player): Int = {
    games().filter(g => g.winnerPlayer == player).map(g => {
      val b1 = brackets.indexWhere(_.contains(g.winner))
      val b2 = brackets.indexWhere(_.contains(g.loser))
      b1 - b2 + 6
    }).sum
  }

  def gamesAvailable(b1: Set[Villain], b2: Set[Villain]): Boolean = {
    getBracketMatchups(b1, b2).length < b1.size * b2.size
  }

  def anyGamesAvailable(bracket: Set[Villain]): Boolean = {
    val otherBrackets = brackets.filter(b => b != bracket)
    otherBrackets.exists(b => gamesAvailable(b, bracket))
  }

  def villainSorter(v1: Villain, v2: Villain): Boolean = v1.entryName < v2.entryName

  def bracketVillainSorter(v1: Villain, v2: Villain): Boolean = {
    val v1Bracket = brackets.indexWhere(_.contains(v1))
    val v2Bracket = brackets.indexWhere(_.contains(v2))

    if (v1Bracket == v2Bracket) villainSorter(v1, v2)
    else if (v1Bracket < v2Bracket) true
    else false
  }

  case class PastGame(firstV: Villain, firstP: Player, secondV: Villain, secondP: Player, winner: Villain)

  def gameSorter(g1: DuelGame, g2: DuelGame, reverse: Boolean): Boolean = {
    def extract(g: DuelGame): (Villain, Villain) = {
      if (isUpperBracket(g.winner, g.loser, brackets)) {
        if (reverse) (g.winner, g.loser) else (g.loser, g.winner)
      } else {
        if (reverse) (g.loser, g.winner) else (g.winner, g.loser)
      }
    }

    val (g1Primary, g1Secondary) = extract(g1)
    val (g2Primary, g2Secondary) = extract(g2)

    if (g1Primary == g2Primary) bracketVillainSorter(g1Secondary, g2Secondary)
    else bracketVillainSorter(g1Primary, g2Primary)
  }

  def getPastGame(g: DuelGame, reverse: Boolean): PastGame = {
    if (isUpperBracket(g.winner, g.loser, brackets)) {
      if (reverse) PastGame(g.winner, g.winnerPlayer, g.loser, g.loserPlayer, g.winner)
      else PastGame(g.loser, g.loserPlayer, g.winner, g.winnerPlayer, g.winner)
    } else {
      if (reverse) PastGame(g.loser, g.loserPlayer, g.winner, g.winnerPlayer, g.winner)
      else PastGame(g.winner, g.winnerPlayer, g.loser, g.loserPlayer, g.winner)
    }
  }

  def showPastGames(history: Seq[PastGame]): Unit = {
    val rows = history.map { g =>
      val left = s"${g.firstV} (${g.firstP})"
      val leftLength = g.firstV.entryName.length + 3 + g.firstP.toString.length
      val result = if (g.winner == g.firstV) "WON" else "LOST"
      val right = s"${g.secondV} (${g.secondP})"
      (left, leftLength, result, right)
    }

    val maxLeftWidth = rows.map(_._2).max
    val maxResultWidth = rows.map(_._3.length).max

    rows.foreach { case (left, visibleLen, result, right) =>
      val padLeftSpaces = " " * (maxLeftWidth - visibleLen)
      val padResultSpaces = " " * (maxResultWidth - result.length)
      println(s"    $left$padLeftSpaces   $result$padResultSpaces    $right")
    }
  }

  def showSortedBracketGames(games: Seq[DuelGame], reverse: Boolean): Unit = {
    if (games.nonEmpty) {
      val sorted = games.sortWith((g1, g2) => gameSorter(g1, g2, reverse))
      val history = sorted.map(g => getPastGame(g, reverse))
      showPastGames(history)
    }
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableDuel] = {
    val randomizer = getRandom()

    val bracket1 = randomizer.shuffle(brackets.filter(anyGamesAvailable)).head
    val bracket2 = randomizer.shuffle(brackets.filter(b => b != bracket1).filter(b => gamesAvailable(bracket1, b))).head

    val (upperBracket, lowerBracket) = if (isUpperBracket(bracket1, bracket2)) (bracket1, bracket2) else (bracket2, bracket1)

    val previousGames: Seq[DuelGame] = getBracketMatchups(upperBracket, lowerBracket)

    println(s"  Bonus Points: ${bracketPoints(bracket1, bracket2)}\n  Upper Bracket\n    $upperBracket\n  Lower Bracket\n    $lowerBracket\n\n  Previous Games:")

    showSortedBracketGames(previousGames, false)
    println(s"\n  Score:\n    Alec: ${score(alec)}\n    Dennis: ${score(dennis)}")
    None
  }

  def printStats(): Unit = {
    println("Number of Expected Wins: " + Tournament13.games().count(g => Tournament13.brackets.indexWhere(_.contains(g.winner)) < Tournament13.brackets.indexWhere(_.contains(g.loser))))
    println("Number of Upsets: " + Tournament13.games().count(g => Tournament13.brackets.indexWhere(_.contains(g.winner)) > Tournament13.brackets.indexWhere(_.contains(g.loser))))

    def upsetCounts(games: Seq[DuelGame], brackets: Seq[Set[Villain]], allVillains: Seq[Villain]): Map[Villain, Int] = {
      // Helper to get a villain's tier index
      def tierIndex(v: Villain): Int = brackets.indexWhere(_.contains(v))

      // Filter only upset games
      val upsetGames = games.filter(g => tierIndex(g.winner) > tierIndex(g.loser))

      // Count upsets per villain
      val grouped = upsetGames.groupBy(_.winner).view.mapValues(_.size).toMap

      // Return full map including 0s for villains without any upsets
      allVillains.map(v => v -> grouped.getOrElse(v, 0)).toMap
    }

    val results = upsetCounts(Tournament13.games(), Tournament13.brackets, Villain.values)

    results.toSeq.sortBy(-_._2).foreach { case (v, count) =>
      println(f"$v%-20s $count")
    }
  }

  def printRemainingGames(): Unit = {
    val maxNameLength = Villain.values.map(_.length).max
    val padding = 2
    Villain.values.foreach(v => {
      val vGames = Tournament13.games().filter(g => g.winner == v || g.loser == v)
      val vGamesSet = vGames.flatMap(g => List(g.winner, g.loser)).toSet

      val thisBracket = Tournament13.brackets.find(_.contains(v)).get
      val remainingVs = Villain.values.toSet.diff(vGamesSet).diff(thisBracket)

      val paddedName = v.toString + " " * (maxNameLength + padding - v.length)
      val remainingPrintout = if (remainingVs.isEmpty) "None" else remainingVs.mkString(", ")
      println(s"$paddedName$remainingPrintout")
    })
  }
}
