package game

import model.{PlayerVillain, Team, Villain}

case class TeamGame(winnerVillain: Villain,
                    winner: Team[PlayerVillain],
                    loser: Team[PlayerVillain],
                    villainOrder: List[Villain],
                    date: Long) extends Game[Team[PlayerVillain], Team[PlayerVillain]] {

  override def isLegal: Boolean = (loser.members.map(_.villain) ++ winner.members.map(_.villain)).forall(_.playedCorrectSince < date)

}
