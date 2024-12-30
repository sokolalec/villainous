package model

import enumeratum._
import io.Color
import io.Color._

sealed abstract class Villain(override val entryName: String, color: String) extends EnumEntry {
  val hasDoubleFate = false
  val impossibleWhenDoubleFated = false
  val playedCorrectSince = 0L
  val adjustments: Seq[String] = Seq.empty
  val houseRules: Seq[String] = Seq.empty

  override def toString: String = Color.color(entryName, color)
}

object Villain extends Enum[Villain] with CirceEnum[Villain] {
  val values: IndexedSeq[Villain] = findValues

  val globalAdjustments: Seq[String] = List(
    "Heroes at 0 strength can be vanquished using no allies whatsoever", // 11-15-2024
    "Any type of deck is only re-shuffled from the discard when a card must be drawn from it, not when it becomes empty" // 11-15-2024
  )

  // Set 1
  case object CaptainHook extends Villain("Captain Hook", red) {
    override val hasDoubleFate: Boolean = true
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
  }
  case object PrinceJohn  extends Villain("Prince John", yellow) {
    override val adjustments: Seq[String] = List(
      "Both Bow and Arrow items are discarded when attached to the same hero used in a vanquish action"
    )
  }
  case object Maleficent extends Villain("Maleficent", green)
  case object Ursula extends Villain("Ursula", purple) {
    override val hasDoubleFate: Boolean = true
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Return to Form can only play a hero to Ursula's location, not any location"
    )
  }
  case object Jafar extends Villain("Jafar", brown) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Sorcerous Power can move Genie",
      "The lamp does not attach to Genie",
      "Abu/Aladdin do not gain strength from scimitar when attached"
    )
    override val houseRules: Seq[String] = List(
      "Treachery cannot be played when Jafar has no power"
    )
  }
  case object QueenofHearts extends Villain("Queen of Hearts", red) {
    override val impossibleWhenDoubleFated: Boolean = true
  }

  // Set 2
  case object DrFacilier extends Villain("Dr Facilier", purple) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "The talisman remains attached to a hero until that hero is defeated, even if new heroes are played"
    )
  }
  case object EvilQueen extends Villain("Evil Queen", purple) {
    override val adjustments: Seq[String] = List(
      "Love's First Kiss can be played as long as one of the two actions can be taken"
    )
  }
  case object Hades extends Villain("Hades", blue)

  // Set 3
  case object Ratigan extends Villain("Ratigan", white) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Ratigan cannot win via Robot Queen in the same turn that the real Queen is defeated via explosive trap"
    )
  }
  case object Scar extends Villain("Scar", orange) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
  }
  case object Yzma extends Villain("Yzma", purple) {
    override val playedCorrectSince = 1722729600L // 08-04-2024
    override val adjustments: Seq[String] = List(
      "When fated, look through all cards in that fate pile, not just two",
      "When no cards can be played from the fate pile, they are put back and no cards are played",
      "When Kronk becomes a hero, the power stays on him and can be stolen via effects (he still does not revert to an ally)"
    )
  }

  // Set 4
  case object CruelladeVil extends Villain("Cruella de Vil", white)
  case object MotherGothel extends Villain("Mother Gothel", yellow) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Mother Gothel must pay 1 trust to play Now I'm the Bad Guy"
    )
  }
  case object Pete extends Villain("Pete", white)

  // Set 5
  case object Gaston extends Villain("Gaston", yellow) {
    override val impossibleWhenDoubleFated: Boolean = true
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "When Maurice is played, Maurice's Invention can be attached to any hero at Maurice's location, not just Maurice",
      "Swoon must replace an obstacle in order to be played"
    )
  }
  case object HornedKing extends Villain("Horned King", green)
  case object LadyTremaine extends Villain("Lady Tremaine", blue) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Glass Slippers cover action spots",
      "If an effect is played via Vicious Practical Jokes, the opponent still chooses how to implement the effect",
      "The Key can move Ball Gown Cinderella to Cinderella's Room without trapping her"
    )
    override val houseRules: Seq[String] = List(
      "The Key cannot be used unless both actions can be completed, with the sole exception being Ball Gown Cinderella"
    )
  }

  // Set 6
  case object Lotso extends Villain("Lotso", pink) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Woody's Hat ignores Rex's ability if Rex is played after Woody's Hat is in the realm",
      "Strength modification is treated differently when worded as 'get -1' vs 'lose 1' strength",
      "Heroes cannot be vanquished when Buzz Lightyear is in the Caterpillar Room with them",
      "Heroes at 0 strength can have their strength restored by all fate cards except when explicitly denied",
      "Buzz is not considered to be a hero for Rex",
      "All effects which result in decreased hero strength are treated equally (no difference between 'get' vs 'lose' vs 'reduce')"
    )
    override val houseRules: Seq[String] = List(
      "Spanish Mode can be played at all times, but does not move Buzz when he is a Guardian"
    )
  }
  case object MadamMim extends Villain("Madam Mim", purple) {
    override val hasDoubleFate: Boolean = true
    override val playedCorrectSince = 1720742400L // 07-12-2024
    override val adjustments: Seq[String] = List(
      "You should generally build up the allies on one spot to vanquish them all in one turn"
    )
  }
  case object Syndrome extends Villain("Syndrome", orange) {
    override val impossibleWhenDoubleFated: Boolean = true
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Omnidroid v10 is a hero if the remote has not yet been played",
      "Teamwork discards itself (and the second card) before looking at the top 6 cards (relevant when deck has < 6 cards)"
    )
    override val houseRules: Seq[String] = List(
      "Teamwork cannot be played if it is the only card revealed (e.g. via play 1 fate card conditions)"
    )
  }

  // Set 7
  case object OogieBoogie extends Villain("Oogie Boogie", green) {
    override val impossibleWhenDoubleFated: Boolean = true
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Sally cannot be played to Oogie Boogie's current location",
      "Sally's card text about adjacency refers to Oogie's location, not Sally's"
    )
  }

  // Set 8
  case object KingCandy extends Villain("King Candy", pink) {
    override val playedCorrectSince: Long = 1722729600L // 08-04-2024
    override val adjustments: Seq[String] = List(
      "There is only one path on the track, you can't decide which route when you get to the middle",
      "When the race ends from the glitch being discarded, the racer token remains on the board covering an action"
    )
    override val houseRules: Seq[String] = List(
      "Princess Vanellope must move King Candy backwards at least 1 space",
      "Both the racer and King Candy must complete at least 1 whole lap (no backwards-at-start shenanigans)"
    )
  }
  case object ShereKhan extends Villain("Shere Khan", green) {
    override val playedCorrectSince: Long = 1731628800L // 11-15-2024
    override val adjustments: Seq[String] = List(
      "Shere Khan's win condition was not known when the first game was played",
      "Fire tokens can cover the same action as other fire tokens (no limit)",
      "Bravo, Bravo can be used to perform more than 4 actions per turn (can repeat actions)",
      "Bagheera must move either all or none of the heroes at his location when played"
    )
  }
}
