package io

import game.DuelGame._
import game.MultiplayerGame._
import game.SoloGame._
import game.{DuelGame, MultiplayerGame}
import io.circe.Decoder
import io.circe.parser.decode

import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.util.Try

object Filesystem {

  def getFiles(path: String): Seq[Path] = {
    val filePaths = Try(Files.walk(Paths.get(path)).iterator().asScala.toSeq)
    filePaths.getOrElse(Seq.empty)
      .filter(_.toString.endsWith(".json"))
      .filterNot(_.toString.contains("empty"))
  }

  def readJsonFile[T](file: Path, decoder: Decoder[T]): Seq[T] = {
    val content = new String(Files.readAllBytes(file))
    decode[List[T]](content)(Decoder.decodeList(decoder)) match {
      case Right(t) => t
      case Left(error) =>
        println(s"Failed to decode JSON from $file: $error")
        Seq.empty
    }
  }

  def getGames[T](path: String, decoder: Decoder[T])(implicit o: Ordering[T]): Seq[T] =
    getFiles(path).flatMap(readJsonFile(_, decoder)).sorted

  private val thisScriptPath = "/Users/alecsokol/villainous"

  private val otherDir = s"$thisScriptPath/records/duel"
  val otherGames: Seq[DuelGame] = getGames(otherDir, otherGameDecoder)(duelGameOrdering)

  private val soloDir = s"$thisScriptPath/records/solo"
  val soloGames: Seq[DuelGame] = getGames(soloDir, soloGameDecoder)(soloGameOrdering).map(_.toDuelGame)

  val tournamentDir = s"$thisScriptPath/records/tournaments"
  val tournamentGames: Seq[DuelGame] = getGames(tournamentDir, tournamentGameDecoder)(duelGameOrdering)

  private val multiplayerDir = s"$thisScriptPath/records/multiplayer"
  val multiplayerGames: Seq[MultiplayerGame] = getGames(multiplayerDir, multiplayerGameDecoder)(multiplayerGameOrdering)

  val nonSoloGames: List[DuelGame] = (otherGames ++ tournamentGames).sortBy(_.date).toList
  val allGames: List[DuelGame] = (nonSoloGames ++ soloGames).sortBy(_.date)
  val legalGames: List[DuelGame] = allGames.filter(_.isLegal)

}
