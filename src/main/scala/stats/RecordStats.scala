package stats

import model.Villain

case class RecordStats(villain: Villain, wins: Int, losses: Int) {
  override def toString = s"${villain.toString}: ($wins, $losses)"
}
