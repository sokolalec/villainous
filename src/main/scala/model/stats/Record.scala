package model.stats

import model.environment.Villain
import model.event.Game

case class Record(villain: Villain, wins: List[Game], losses: List[Game])
