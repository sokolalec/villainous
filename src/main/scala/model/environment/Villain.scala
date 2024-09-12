package model.environment

import enumeratum._

sealed abstract class Villain(override val entryName: String) extends EnumEntry {
  val hasDoubleFate = false
  val impossibleWhenDoubleFated = false
  override def toString: String = entryName
}

object Villain extends Enum[Villain] with CirceEnum[Villain] {
  val values: IndexedSeq[Villain] = findValues

  // Set 1
  case object CaptainHook extends Villain("Captain Hook") {
    override val hasDoubleFate: Boolean = true
  }
  case object PrinceJohn  extends Villain("Prince John")
  case object Maleficent extends Villain("Maleficent")
  case object Ursula extends Villain("Ursula") {
    override val hasDoubleFate: Boolean = true
  }
  case object Jafar extends Villain("Jafar")
  case object QueenofHearts extends Villain("Queen of Hearts")

  // Set 2
  case object DrFacilier extends Villain("Dr Facilier")
  case object EvilQueen extends Villain("Evil Queen")
  case object Hades extends Villain("Hades")

  // Set 3
  case object Ratigan extends Villain("Ratigan")
  case object Scar extends Villain("Scar")
  case object Yzma extends Villain("Yzma")

  // Set 4
  case object CruelladeVil extends Villain("Cruella de Vil")
  case object MotherGothel extends Villain("Mother Gothel") {
    override val impossibleWhenDoubleFated: Boolean = true
  }
  case object Pete extends Villain("Pete")

  // Set 5
  case object Gaston extends Villain("Gaston") {
    override val impossibleWhenDoubleFated: Boolean = true
  }
  case object HornedKing extends Villain("Horned King")
  case object LadyTremaine extends Villain("Lady Tremaine") {
    override val hasDoubleFate: Boolean = true
  }

  // Set 6
  case object Lotso extends Villain("Lotso")
  case object MadamMim extends Villain("Madam Mim") {
    override val hasDoubleFate: Boolean = true
  }
  case object Syndrome extends Villain("Syndrome") {
    override val impossibleWhenDoubleFated: Boolean = true
  }

  // Set 7
  case object OogieBoogie extends Villain("Oogie Boogie")

  // Set 8
  case object KingCandy extends Villain("King Candy")
  case object ShereKhan extends Villain("Shere Khan")
}
