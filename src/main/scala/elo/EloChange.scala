package elo

case class EloChange[T](opponent: T, eloDifference: Double)
