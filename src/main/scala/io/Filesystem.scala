package io

import io.circe.Decoder
import io.circe.parser.decode
import model.game.DuelGame.{otherGameDecoder, tournamentGameDecoder}
import model.game.MultiplayerGame.multiplayerGameDecoder
import model.game.SoloGame.soloGameDecoder
import model.game.{DuelGame, MultiplayerGame}
import util.Datetime.epochOf

import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Filesystem {

  def readJsonFile[T](file: Path, decoder: Decoder[T]): Seq[T] = {
    val content = new String(Files.readAllBytes(file))
    decode[List[T]](content)(Decoder.decodeList(decoder)) match {
      case Right(t) => t
      case Left(error) =>
        println(s"Failed to decode JSON from $file: $error")
        Seq.empty
    }
  }

  private val thisScriptPath = "/Users/alecsokol/villainous"

  private val otherDir = s"$thisScriptPath/records/duel"
  private val otherPath = Paths.get(otherDir)
  private val otherFiles = Files
    .walk(otherPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))
  val otherGames: Seq[DuelGame] = otherFiles.flatMap(f => {
    val year = f.getParent.getFileName.toString
    val gameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/") + "/" + year
    val decoder = otherGameDecoder(epochOf(gameDate))
    readJsonFile(f, decoder)
  })

  private val soloDir = s"$thisScriptPath/records/solo"
  private val soloPath = Paths.get(soloDir)
  private val soloFiles = Files
    .walk(soloPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))
  val soloGames: Seq[DuelGame] = soloFiles.flatMap(f => {
    val year = f.getParent.getFileName.toString
    val gameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/") + "/" + year
    val decoder = soloGameDecoder(epochOf(gameDate))
    readJsonFile(f, decoder)
  }).map(_.toDuelGame())

  private val tournamentDir = s"$thisScriptPath/records/tournaments"
  private val tournamentPath = Paths.get(tournamentDir)
  val tournamentFiles = Files
    .walk(tournamentPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))

  val tournamentGames: List[DuelGame] = tournamentFiles.flatMap(f => readJsonFile(f, tournamentGameDecoder)).toList

  private val multiplayerDir = s"$thisScriptPath/records/multiplayer"
  private val multiplayerPath = Paths.get(multiplayerDir)
  val multiplayerFiles = Files
    .walk(multiplayerPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))

  val multiplayerGames: Seq[MultiplayerGame] = multiplayerFiles.flatMap(f => {
    val year = f.getParent.getFileName.toString
    val gameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/") + "/" + year
    val decoder = multiplayerGameDecoder(epochOf(gameDate))
    readJsonFile(f, decoder)
  })

  val nonSoloGames: List[DuelGame] = (otherGames ++ tournamentGames).toList
  val allGames: List[DuelGame] = nonSoloGames ++ soloGames

}
