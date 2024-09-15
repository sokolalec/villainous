package game

import model.{Player, Villain}

case class PlayableGame(player1: Player, villain1: Villain, player2: Player, villain2: Villain, firstPlayer: Player) {

  private def centerString(s: String, size: Int): String = {
    val padding = (size - s.length) / 2
    val extraPadding = if ((size - s.length) % 2 != 0) 1 else 0
    (" " * padding) + s + (" " * (padding + extraPadding))
  }

  override def toString: String = {
    val firstPlayerHeader = "First Player"

    val column1Max = math.max(player1.name.length, villain1.entryName.length)
    val column2Max = math.max(player2.name.length, villain2.entryName.length)
    val column3Max = math.max(firstPlayerHeader.length, firstPlayer.name.length)

    // Row 1
    val player1Column = centerString(player1.name, column1Max)
    val player2Column = centerString(player2.name, column2Max)
    val firstPlayerColumn = centerString(firstPlayerHeader, column3Max)

    // Row 2
    val villain1Column = centerString(villain1.toString, column1Max)
    val villain2Column = centerString(villain2.toString, column2Max)
    val firstPlayerNameColumn = centerString(firstPlayer.name, column3Max)

    val header = "#" * (3 + player1Column.length + 5 + player2Column.length + 5 + firstPlayerColumn.length + 3)

    s"""
       |$header
       |#  $player1Column  #  $player2Column  #  $firstPlayerColumn  #
       |#  $villain1Column  #  $villain2Column  #  $firstPlayerNameColumn  #
       |$header
       |""".stripMargin
  }

}
