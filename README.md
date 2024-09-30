# Villainous

Hello, I made this repo as a fun way to eventually figure out which Villainous characters are the best.

## Using this Repo

For the most part, just replace all the `records/tournaments/*.json` with your own data!
You can also change the player names within `model/Player.scala`, but it's not really necessary.
I just wanted as little code in `main` as possible.

Feel free to change the record schema and models as needed.
The `json` reader implementation can be found within `model/event/Game.scala`.

Depending on how many games you've played, consider adjusting the Elo scaling via `eloFactor` in `util/EloOps.scala`.

## Our Data

There are two different subsets of records - tournaments, and other games.
Generally, tournament games are more "serious", where both players are trying to win.

The rules of our tournaments change over time in order to keep things fresh.
In all tournaments, generally the goal is to win once with each character.
After you have won with a particular Villain, you do not play that Villain again for the rest of the tournament.
The first player to run out of Villains, wins!

Tournament `001`:
- ownedExpansions = (Original, PerfectlyWretched, DespicablePlots)
- This ruleset was more unique than the other tournament games:
  - From the 12 available Villains, we did a draft pick of 6 each
  - One player drafts one Villain first, then the second player drafts two, then the first player drafts two, etc.
  - Eventually, the player with the first pick is left with the last remaining unpicked Villain
  - Finally, each player got to ban one Villain that the other had drafted
- Each match, both players independently select which Villain they want to play, and reveal their choice simultaneously
- The player who went first in the first game was decided by coin flip
- In the rest of the games, the winner of the most recent game was allowed to decide if they wanted to go first or second

Tournament `002`:
- ownedExpansions = (Original, PerfectlyWretched, DespicablePlots)
- No draft, no bans, just win once with every character
- All Villains were set out in a line based on their perceived rank
  - The "best" Villain was at the top
  - The "second best" Villain was at the bottom
  - The "third best" Villain was the second from the top
  - The "fourth best" Villain was the second from the bottom
  - Repeat this process until the mediocre Villains are in the center of the line
- One player starts at the top of the line, and the other at the bottom
- When a player wins with a Villain, they move to the next Villain in the line, moving towards each other
  - If one player would move onto the same Villain the other player just lost with, they skip over that Villain, but must go back and win with that Villain immediately after the other player wins
- The first player to reach the opposite side of the line, wins
- If both players have the same Villain remaining, it is a draw
- The player going first in each individual match is decided by coin flip

Tournament `003`:
- ownedExpansions = (Original, WickedToTheCore, PerfectlyWretched, DespicablePlots, BiggerAndBadder)
- Basic rules, simply win with each Villain before your opponent
- Each game, both players generated a random number between 1 and the number of Villains they had left to win with
- If both players end up picking the same Villain, the first player to roll that number keeps that Villain, and the other re-rolls
- When there is only 1 Villain left for a player to win with, they can pick which Villain their opponent plays (from that player's remaining Villains)

Tournament `004`:
- ownedExpansions = (Original, WickedToTheCore, EvilComesPrepared, PerfectlyWretched, DespicablePlots, BiggerAndBadder, FilledwithFright, SugarandSpite)
- Same basic rules as tournament `003`
- Games were generated pseudo-randomly (see below) using the code in this repo

Tournament `005`:
- ownedExpansions = (Original, WickedToTheCore, EvilComesPrepared, PerfectlyWretched, DespicablePlots, BiggerAndBadder, FilledwithFright, SugarandSpite)
- First person to 12 wins, wins the tournament
- When a Villain wins a game, it is out of the tournament and cannot be played again
- One person picks a matchup of two villains; the other picks which one they want to play in that matchup
- A coin flip decides who dictates the matchup (the winner of the coin flip can make the other player choose the matchup if they want)

Tournament `006`:
- ownedExpansions = (Original, WickedToTheCore, EvilComesPrepared, PerfectlyWretched, DespicablePlots, BiggerAndBadder, FilledwithFright, SugarandSpite)
- First person to 25 wins, wins the tournament
- After a Villain is played, it can no longer be played again until each Villain has been played at least that number of times (i.e. random round robin)
- Games are randomly decided using the Villains remaining for each player

## Game Generation

This repo was initially only built as a way to keep track of stats, but during tournament `003`, we started using RNG to decide which character to play.
That turned out to be a very fun idea, but as the size of our collection grew, it became tedious to keep track of which characters were available for which players during an elimination tournament.
I also kept forgetting to flip a coin (virtually) before we sat down to play, and so, the idea for full random game generation with 1 click was born!

If you want to use the random game generator, make sure that `model/environment/Expansion.ownedExpansions` is set correctly based on what you own.
Games proposed by `GameOps.getNextGame()` assume that you are following the same elimination style tournament rules, where the winner is the first to win with each character.

Note that certain matchups were considered essentially impossible to win due to unstoppable double-fating, such as Gaston vs Madam Mim.
These match ups will never be generated by the random game generator, because even if there is that 5% chance that you could win with perfect card order, it is not fun to play for either party.
This change to game generation happened as a direct result of the `Gaston` games in tournament `003`.
If you want to ignore this, the feature is coded into `MatchUps.getPossibleMatches()` and leverages the `hasDoubleFate` and `impossibleWhenDoubleFated` fields of the `model/environment/Villain` class.

## Contributing

You are more than welcome to propose improvements or features to this repo!
That being said, I will probably not accept your changes.
