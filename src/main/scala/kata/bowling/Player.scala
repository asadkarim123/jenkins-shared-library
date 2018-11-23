package kata.bowling

class Player {
  var rolls: List[Int] = List()

  def roll(pins : Int) = rolls = rolls :+ pins
  def score: Int = scoreRecursive(0, 1, rolls)

  private def frameComplete: (Int, Boolean) = frameCompleteRecursive(1, rolls)
  def frame: Int = frameComplete._1
  def isFrameFinished: Boolean = frameComplete._2

  def scoreRecursive(score: Int, frame: Int, rolls: List[Int]) : Int = {
    (frame, rolls) match {
      //Game over
      case (11, _)
      => score
      //Strike
      case (_, 10 :: tail)
      => tail.length match {
        case 0 => score + 10
        case 1 => score + 10 + tail.head*2
        case _ => scoreRecursive(score + 10 + tail.take(2).sum, frame + 1, tail)
      }
      //Spare
      case (_, first :: second :: tail) if (first + second == 10)
      => tail.length match {
        case 0 => score + 10
        case _ => scoreRecursive(score + 10 + tail.head, frame + 1, tail)
      }
      //Open
      case (_, first :: second :: tail)
      => scoreRecursive(score + first + second, frame + 1, tail)
      //Incomplete frame
      case (_, first :: tail)
      => scoreRecursive(score + first, frame + 1, tail)
      //Anything else
      case _
      => score
    }
  }

  def frameCompleteRecursive(frame: Int, rolls: List[Int]) : (Int, Boolean) = {
    (frame, rolls) match {
      //Game over
      case (11, _)
      => (frame, true)
      //Last frame
      case (10, 10 :: tail)
      => if(tail.length == 2)(frame, true) else (frame, false)
      case (10, first :: second :: tail)
      => if(first+second == 10 && tail.length == 0)(frame, false) else frameCompleteRecursive(frame + 1, tail.tail)
      //Strike
      case (_, 10 :: tail)
      => frameCompleteRecursive(frame + 1, tail)
      //Open or Spare
      case (_, first :: second :: tail)
      => frameCompleteRecursive(frame + 1, tail)
      //Incomplete frame
      case (_, first :: tail)
      => (frame, false)
      //Anything else
      case _
      => (frame-1, true)
    }
  }
}
