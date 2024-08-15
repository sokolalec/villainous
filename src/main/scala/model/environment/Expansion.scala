package model.environment

import model.environment.Villain._

trait Expansion {
  val villains: Set[Villain]
}

object Expansion {
  object Original extends Expansion {
    val villains: Set[Villain] = Set(CaptainHook, PrinceJohn, Maleficent, Ursula, Jafar, QueenofHearts)
  }

  object WickedToTheCore extends Expansion {
    val villains: Set[Villain] = Set(DrFacilier, EvilQueen, Hades)
  }

  object EvilComesPrepared extends Expansion {
    val villains: Set[Villain] = Set(Ratigan, Scar, Yzma)
  }

  object PerfectlyWretched extends Expansion {
    val villains: Set[Villain] = Set(CruelladeVil, MotherGothel, Pete)
  }

  object DespicablePlots extends Expansion {
    val villains: Set[Villain] = Set(Gaston, HornedKing, LadyTremaine)
  }

  object BiggerAndBadder extends Expansion {
    val villains: Set[Villain] = Set(Lotso, MadamMim, Syndrome)
  }

  object FilledwithFright extends Expansion {
    val villains: Set[Villain] = Set(OogieBoogie)
  }

  object SugarandSpite extends Expansion {
    val villains: Set[Villain] = Set(KingCandy, ShereKhan)
  }

  val ownedExpansions: Set[Expansion] =
    Set(
      Original,
      WickedToTheCore,
      EvilComesPrepared,
      PerfectlyWretched,
      DespicablePlots,
      BiggerAndBadder,
      FilledwithFright,
      SugarandSpite
    )
}
