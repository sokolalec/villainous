package model.event

import model.environment.Villain

case class VillainEloChange(opponent: Villain, eloDifference: Double)
