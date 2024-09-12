package model.stats

import model.event.VillainEloChange

case class VillainElo(current: Double, audit: List[VillainEloChange])
