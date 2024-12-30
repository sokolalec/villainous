package game

import model.{Player, Villain}

import scala.annotation.tailrec

case class PlayableGame(playerVillains: Seq[(Player, Seq[Villain])]) {

  private def centerString(s: String, size: Int): String = {
    val visibleLength = s.replaceAll("\u001b\\[[;\\d]*m", "").length
    val padding = (size - visibleLength) / 2
    val extraPadding = if ((size - visibleLength) % 2 != 0) 1 else 0
    (" " * padding) + s + (" " * (padding + extraPadding))
  }

  override def toString: String = {
    val columnWidths: Map[Player, Int] = playerVillains.map { pv =>
      val (player, villains) = pv
      (player, math.max(player.name.length, villains.map(_.entryName.length).max))
    }.toMap

    val playerColumns: Seq[String] = playerVillains.map { pv =>
      val (player, _) = pv
      centerString(player.name, columnWidths(player))
    }

    val villainColumns: Seq[Seq[String]] = playerVillains.map { pv =>
      val (player, villains) = pv
      villains.map(v => centerString(v.toString, columnWidths(player)))
    }

    // 3 spaces for "#  " and "  #" at the beginning and end
    // 5 spaces for "  #  " in between columns
    // the actual column width
    val header = "#" * (3 + 5 * (playerVillains.size - 1) + columnWidths.values.sum + 3)

    val playerRow = playerColumns.mkString("  #  ")

    @tailrec
    def villainRows(acc: Seq[String], villainCols: Seq[Seq[String]]): Seq[String] =
      if (villainCols.head.isEmpty) acc
      else {
        val row = villainCols.map(_.head).mkString("  #  ")
        villainRows(acc :+ row, villainCols.map(_.drop(1)))
      }

    val indent = "      "

    "\n" +
    indent + header + "\n" +
    indent + s"#  $playerRow  #" + "\n" +
    villainRows(Seq.empty, villainColumns).map(row => s"$indent#  $row  #").mkString("\n") + "\n" +
    indent + header + "\n" +
    "\n" +
    "\n"

//    s"""
//       |
//       |      $header
//       |      #  $playerRow  #
//       |      #  #
//       |      $header
//       |
//       |
//       | """.stripMargin
//    val firstPlayerHeader = "First Player"
//
//    val column1Max = math.max(player1.name.length, villain1.entryName.length)
//    val column2Max = math.max(player2.name.length, villain2.entryName.length)
//    val column3Max = math.max(firstPlayerHeader.length, firstPlayer.entryName.length)
//
//    // Row 1
//    val player1Column = centerString(player1.name, column1Max)
//    val player2Column = centerString(player2.name, column2Max)
//    val firstPlayerColumn = centerString(firstPlayerHeader, column3Max)
//
//    // Row 2
//    val villain1Column = centerString(villain1.toString, column1Max)
//    val villain2Column = centerString(villain2.toString, column2Max)
//    val firstPlayerNameColumn = centerString(firstPlayer.toString, column3Max)
//
//    val header = "#" * (3 + player1Column.length + 5 + player2Column.length + 5 + firstPlayerColumn.length + 3)
//
//    s"""
//       |
//       |      $header
//       |      #  $player1Column  #  $player2Column  #  $firstPlayerColumn  #
//       |      #  $villain1Column  #  $villain2Column  #  $firstPlayerNameColumn  #
//       |      $header
//       |
//       |
//       | """.stripMargin
  }

}
