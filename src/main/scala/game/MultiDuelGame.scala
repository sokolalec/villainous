package game

import model.{Player, Villain}

case class MultiDuelGame(winner: Villain, winnerTeam: Set[Villain], loserTeam: Set[Villain], winnerPlayer: Player, loserPlayer: Player, villainOrder: List[Villain], date: Long)
