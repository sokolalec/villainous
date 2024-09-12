package model.stats

import model.environment.Villain
import model.game.DuelGame

case class Record(villain: Villain, wins: List[DuelGame], losses: List[DuelGame])
