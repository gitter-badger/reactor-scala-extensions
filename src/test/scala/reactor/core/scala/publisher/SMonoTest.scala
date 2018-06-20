package reactor.core.scala.publisher

import java.util.concurrent.Callable

import org.scalatest.{FreeSpec, Matchers}
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class SMonoTest extends FreeSpec with Matchers {
  private val randomValue = Random.nextLong()

  "SMono" - {
    ".just should emit the specified item" in {
      StepVerifier.create(SMono.just(randomValue))
        .expectNext(randomValue)
        .verifyComplete()
    }

    ".raiseError should create Mono that emit error" in {
      StepVerifier.create(SMono.raiseError(new RuntimeException("runtime error")))
        .expectError(classOf[RuntimeException])
        .verify()
    }

    ".delaySubscription" - {
      "with delay duration should delay subscription as long as the provided duration" in {
        StepVerifier.withVirtualTime(() => SMono.just(1).delaySubscription(1 hour))
          .thenAwait(1 hour)
          .expectNext(1)
          .verifyComplete()
      }
      "with delay duration and scheduler should delay subscription as long as the provided duration" in {
        StepVerifier.withVirtualTime(() => SMono.just(1).delaySubscription(1 hour, Schedulers.single()))
          .thenAwait(1 hour)
          .expectNext(1)
          .verifyComplete()
      }
      "with another publisher should delay the current subscription until the other publisher completes" in {
        StepVerifier.withVirtualTime(() => SMono.just(1).delaySubscription(SMono.just("one").delaySubscription(1 hour)))
          .thenAwait(1 hour)
          .expectNext(1)
          .verifyComplete()

      }
    }

    ".from" - {
      "a publisher should ensure that the publisher will emit 0 or 1 item." in {
        StepVerifier.create(SMono.from(SFlux.just(1, 2, 3, 4, 5)))
          .expectNext(1)
          .expectComplete()
          .verify()
      }

      "a callable should ensure that Mono will return a value from the Callable" in {
        StepVerifier.create(SMono.fromCallable(new Callable[Long] {
          override def call(): Long = randomValue
        }))
          .expectNext(randomValue)
          .expectComplete()
          .verify()
      }

      "source direct should return mono of the source" in {
        StepVerifier.create(SMono.fromDirect(Flux.just(1, 2, 3)))
          .expectNext(1, 2, 3)
          .verifyComplete()
      }

      "a future should result Mono that will return the value from the future object" in {
        import scala.concurrent.ExecutionContext.Implicits.global
        StepVerifier.create(SMono.fromFuture(Future[Long] {
          randomValue
        }))
          .expectNext(randomValue)
          .verifyComplete()
      }
    }

    ".map should map the type of Mono from T to R" in {
      StepVerifier.create(SMono.just(randomValue).map(_.toString))
        .expectNext(randomValue.toString)
        .expectComplete()
        .verify()
    }
  }
}