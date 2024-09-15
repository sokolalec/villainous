package stats

import model.Villain
import model.game.DuelGame

case class Record(villain: Villain, wins: List[DuelGame], losses: List[DuelGame])
