package model.stats

import model.environment.Villain

case class RecordStats(villain: Villain, wins: Int, losses: Int) {
  override def toString = s"${villain.entryName}: ($wins, $losses)"
}
