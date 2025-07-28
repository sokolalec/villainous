package game

trait Game[W, L] {
  def winner: W
  def loser: L
  def date: Long
  def isLegal: Boolean
}
