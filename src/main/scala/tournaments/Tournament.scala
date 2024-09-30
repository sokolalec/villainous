package tournaments

import game.PlayableGame
import io.Filesystem.{readJsonFile, tournamentFiles}
import model.{Player, Villain}
import model.game.DuelGame
import model.game.DuelGame.tournamentGameDecoder

import scala.util.Try

trait Tournament {

  val version: String

  val finished: Boolean = false

  val availableVillains: Set[Villain]

  def games(): Seq[DuelGame] = tournamentFiles
    .filter(f => Try(f.getFileName.toString.startsWith(version)).getOrElse(false)) // startsWith throws on empty string
    .flatMap(f => readJsonFile(f, tournamentGameDecoder))
    .sortBy(_.date)
    .toList

  def players(): Set[Player] = games().flatMap(g => List(g.winnerPlayer, g.loserPlayer)).distinct.toSet

  def score(): Map[Player, Int] = players().map(p => (p, games().count(_.winnerPlayer == p))).toMap

  def generateNextGame(player1: Player, player2: Player): Option[PlayableGame]

  def congratulate(): Unit = {
    if (finished) {
      val winner = score().maxBy(_._2)._1
      val msg = s"   #  Tournament $version Winner:   $winner  #"
      val border = "   " + "#" * (msg.length - 3)
      println("")
      println(border)
      println(msg)
      println(border)
      println("")
    }
  }

}
