package io

object Color {

  val reset = "\u001b[0m"
  val red = "\u001b[31m"
  val green = "\u001b[32m"
  val yellow = "\u001b[33m"
  val blue = "\u001b[34m"
  val white = "\u001b[37m"
  val purple = "\u001b[38;5;93m"
  val brown = "\u001b[38;5;94m"
  val orange = "\u001b[38;5;214m"
  val pink = "\u001b[38;5;219m"

  def color(text: String, color: String): String = s"$color$text$reset"

}
