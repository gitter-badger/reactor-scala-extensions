package reactor.core.scala.publisher

import reactor.core.publisher.{Flux => JFlux, Mono => JMono}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MetricsTest extends AnyFreeSpec with ScalaConverters with Matchers {
  "Micrometer" - {
    "when it is available on classpath should be used by" - {
      "SFlux" in {
        JFlux.just(1, 2, 3).asScala.metrics.coreFlux.getClass.getName shouldBe "reactor.core.publisher.FluxMetricsFuseable"
      }
      "SMono" in {
        JMono.just(1).asScala.metrics.coreMono.getClass.getName shouldBe "reactor.core.publisher.MonoMetricsFuseable"
      }
    }
  }
}
