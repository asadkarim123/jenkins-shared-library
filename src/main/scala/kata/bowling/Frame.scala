package kata.bowling

//import Status._

class Frame {
  var rolls: Array[Int] = new Array[Int](2)

  def completedRolls: Array[Int] = {
    rolls.filter(_.isValidInt)
  }

//  def frameStatus(): Status = {
//    if(completedRolls.length == 0){
//      INCOMPLETE
//    }else if(rolls(0) == 10){
//      STRIKE
//    }else if(completedRolls.length == 2){
//      if(rolls(0) + rolls(1) == 10){
//        SPARE
//      }else{
//        OPEN
//      }
//    }else{
//      println("something is wrong")
//      OPEN
//    }
//  }
}

object Status extends Enumeration {
  type Status = Value
  val INCOMPLETE, OPEN, SPARE, STRIKE = Value
}

