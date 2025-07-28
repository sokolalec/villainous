package elo

trait EloCalculable[G, T] {

  def defaultElo(t: T): Elo[T] = Elo(1000.0, List.empty)

  def eloWinner(game: G): T

  def eloLoser(game: G): T

}
