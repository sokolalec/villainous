package elo

import model.game.DuelGame

trait EloCalculable[T] {

  def eloWinner(game: DuelGame): T

  def eloLoser(game: DuelGame): T

}
