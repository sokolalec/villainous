package util

import io.circe.Decoder
import io.circe.parser.decode
import model.event.Game
import util.Datetime.epochOf

import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Filesystem {

  private val thisScriptPath = "/Users/alecsokol/villainous"

  private val otherDir = s"$thisScriptPath/records/other"
  private val otherPath = Paths.get(otherDir)
  private val otherFiles = Files
    .walk(otherPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))

  val otherGames: Seq[Game] = otherFiles.flatMap(f => {
    val otherGameDate = f.getFileName.toString.stripSuffix(".json").replace("-", "/")
    val decoder = Game.otherGameDecoder(epochOf(otherGameDate))
    readJsonFile(f, decoder)
  })

  private val tournamentDir = s"$thisScriptPath/records/tournaments"
  private val tournamentPath = Paths.get(tournamentDir)
  val tournamentFiles = Files
    .walk(tournamentPath).iterator().asScala.toSeq
    .filter(_.toString.endsWith(".json"))
    .filterNot(_.toString.contains("empty"))

  val tournamentGames: List[Game] = tournamentFiles.flatMap(f => readJsonFile(f, Game.tournamentGameDecoder)).toList

  val allGames: List[Game] = (otherGames ++ tournamentGames).toList

  def readJsonFile(file: Path, decoder: Decoder[Game]): Seq[Game] = {
    val content = new String(Files.readAllBytes(file))
    decode[List[Game]](content)(Decoder.decodeList(decoder)) match {
      case Right(games) => games
      case Left(error) =>
        println(s"Failed to decode JSON from $file: $error")
        Seq.empty
    }
  }

}
