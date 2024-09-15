package model

import enumeratum._
import io.Color
import io.Color._

sealed abstract class Villain(override val entryName: String, color: String) extends EnumEntry {
  val hasDoubleFate = false
  val impossibleWhenDoubleFated = false

  override def toString: String = Color.color(entryName, color)
}

object Villain extends Enum[Villain] with CirceEnum[Villain] {
  val values: IndexedSeq[Villain] = findValues

  // Set 1
  case object CaptainHook extends Villain("Captain Hook", red) {
    override val hasDoubleFate: Boolean = true
  }
  case object PrinceJohn  extends Villain("Prince John", yellow)
  case object Maleficent extends Villain("Maleficent", green)
  case object Ursula extends Villain("Ursula", purple) {
    override val hasDoubleFate: Boolean = true
  }
  case object Jafar extends Villain("Jafar", brown)
  case object QueenofHearts extends Villain("Queen of Hearts", red)

  // Set 2
  case object DrFacilier extends Villain("Dr Facilier", purple)
  case object EvilQueen extends Villain("Evil Queen", purple)
  case object Hades extends Villain("Hades", blue)

  // Set 3
  case object Ratigan extends Villain("Ratigan", white)
  case object Scar extends Villain("Scar", orange)
  case object Yzma extends Villain("Yzma", purple)

  // Set 4
  case object CruelladeVil extends Villain("Cruella de Vil", white)
  case object MotherGothel extends Villain("Mother Gothel", yellow) {
    override val impossibleWhenDoubleFated: Boolean = true
  }
  case object Pete extends Villain("Pete", white)

  // Set 5
  case object Gaston extends Villain("Gaston", yellow) {
    override val impossibleWhenDoubleFated: Boolean = true
  }
  case object HornedKing extends Villain("Horned King", green)
  case object LadyTremaine extends Villain("Lady Tremaine", blue) {
    override val hasDoubleFate: Boolean = true
  }

  // Set 6
  case object Lotso extends Villain("Lotso", pink)
  case object MadamMim extends Villain("Madam Mim", purple) {
    override val hasDoubleFate: Boolean = true
  }
  case object Syndrome extends Villain("Syndrome", orange) {
    override val impossibleWhenDoubleFated: Boolean = true
  }

  // Set 7
  case object OogieBoogie extends Villain("Oogie Boogie", green)

  // Set 8
  case object KingCandy extends Villain("King Candy", pink)
  case object ShereKhan extends Villain("Shere Khan", green)
}
