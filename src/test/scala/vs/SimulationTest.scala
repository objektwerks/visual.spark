package vs

import org.scalatest.FunSuite

class SimulationTest extends FunSuite {
  test("simulation") {
    val simulation = new Simulation()
    val result = simulation.play()
    assert(result.producedKafkaMessages.nonEmpty)
    assert(result.selectedLineChartDataFromCassandra.nonEmpty)
    assert(result.selectedPieChartDataFromCassandra.nonEmpty)
    println(result.toString)
  }
}