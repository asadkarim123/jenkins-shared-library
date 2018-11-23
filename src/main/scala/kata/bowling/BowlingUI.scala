package kata.bowling

object BowlingUI extends App {
  val game = new BowlingGame(2)

  while(!game.isOver()){
    val nextRoll: (Int, Int) = game.nextRoll
    print(s"Player ${nextRoll._1+1} enter score for frame ${nextRoll._2}:\n")
    val pins = scala.io.StdIn.readInt()
    game.roll(pins)
    println(s"Player ${game.currentPlayer+1} - your total score is ${game.players(game.currentPlayer).score}.")
  }
  println("Game over!")
  val winner: (Int, Player) = game.getWinner()
  println(s"Winner is Player ${winner._1+1} with total score ${winner._2.score}")
}
