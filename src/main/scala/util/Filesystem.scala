package util

import io.circe.Decoder
import io.circe.parser.decode
import model.game.DuelGame.{tournamentGameDecoder, otherGameDecoder}
import model.game.MultiplayerGame.multiplayerGameDecoder
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

  private val otherDir = s"$thisScriptPath/records/other"
  private val otherPath = Paths.get(otherDir)
  private val otherFiles = Files
    .walk(otherPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))

  val otherGames: Seq[DuelGame] = otherFiles.flatMap(f => {
    val gameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/")
    val decoder = otherGameDecoder(epochOf(gameDate))
    readJsonFile(f, decoder)
  })

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
    val gameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/")
    val decoder = multiplayerGameDecoder(epochOf(gameDate))
    readJsonFile(f, decoder)
  })

  val allGames: List[DuelGame] = (otherGames ++ tournamentGames).toList

}
