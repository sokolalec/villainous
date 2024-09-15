package model

import io.circe.{Decoder, HCursor}

case class Player(name: String) {
  override def toString: String = name
}

object Player {

  val alec: Player = Player("Alec")
  val dennis: Player = Player("Dennis")
  val michael: Player = Player("Michael")

  implicit val playerDecoder: Decoder[Player] = new Decoder[Player] {
    final def apply(c: HCursor): Decoder.Result[Player] = c.as[String].map(Player(_))
  }

}
