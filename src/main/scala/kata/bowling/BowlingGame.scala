package kata.bowling

class BowlingGame(numPlayers: Int=1) {
  val players: Array[Player] = Array.fill[Player](numPlayers)(new Player)
  var currentPlayer = numPlayers-1
  var currentFrame = 1

  def nextRoll: (Int, Int) = {
    if(players(currentPlayer).isFrameFinished) {
      val p = if(currentPlayer >= players.length-1) 0 else currentPlayer+1
      if (players(p).isFrameFinished) (p, players(p).frame+1)
      else (p, players(p).frame)
    }
    else (currentPlayer, players(currentPlayer).frame)
  }

  def roll(pins: Int): Unit = {
    val t = nextRoll
    currentPlayer = t._1
    currentFrame = t._2
    players(currentPlayer).roll(pins)
  }

  def isOver(): Boolean = {
    nextRoll._2 > 10
  }

  def getWinner():(Int, Player) = {
    var winner: Player = players.reduce((x,y) => if(y.score>x.score) y else x)
    (players.indexOf(winner), winner)
  }
}
