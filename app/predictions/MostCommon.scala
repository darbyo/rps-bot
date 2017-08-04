package predictions

object MostCommon {
  def predict(items: List[String]) = {
    val papers = items.count(_ == "PAPER")
    val rocks = items.count(_ == "ROCK")
    val scissors = items.count(_ == "SCISSORS")
    val dynamite = items.count(_ == "DYNAMITE")

    if (dynamite > scissors && dynamite > papers && dynamite > rocks){
      "WATERBOMB"
    } else if (papers > rocks && papers > scissors) {
      "SCISSORS"
    } else if(scissors > rocks && scissors > papers) {
      "ROCK"
    } else if (rocks > papers && rocks > scissors) {
      "PAPER"
    } else {
      "DYNAMITE"
    }
  }
}
