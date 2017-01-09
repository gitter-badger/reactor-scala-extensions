package reactor.core.scala

import java.lang.{Boolean => JBoolean}
import java.time.{Duration => JDuration}
import java.util.Optional
import java.util.function.{Consumer, Function, LongConsumer, Predicate}

import reactor.util.function.{Tuple2, Tuple3, Tuple4, Tuple5, Tuple6}

import scala.concurrent.duration.Duration
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

/**
  * Created by winarto on 12/31/16.
  */
package object publisher {
  implicit def scalaDuration2JavaDuration(duration: Duration): JDuration = {
    JDuration.ofNanos(duration.toNanos)
  }

  implicit def scalaOption2JavaOptional[T](option: Option[T]): Optional[T] = {
    option.map(Optional.of[T]).getOrElse(Optional.empty())
  }

  implicit def tupleTwo2ScalaTuple2[T1, T2](javaTuple2: Tuple2[T1, T2]): (T1, T2) = {
    (javaTuple2.getT1, javaTuple2.getT2)
  }

  implicit def tupleThree2ScalaTuple3[T1, T2, T3](javaTuple3: Tuple3[T1, T2, T3]): (T1, T2, T3) = {
    (javaTuple3.getT1, javaTuple3.getT2, javaTuple3.getT3)
  }

  implicit def tupleFour2ScalaTuple4[T1, T2, T3, T4](javaTuple4: Tuple4[T1, T2, T3, T4]): (T1, T2, T3, T4) = {
    (javaTuple4.getT1, javaTuple4.getT2, javaTuple4.getT3, javaTuple4.getT4)
  }

  implicit def tupleFive2ScalaTuple5[T1, T2, T3, T4, T5](javaTuple5: Tuple5[T1, T2, T3, T4, T5]): (T1, T2, T3, T4, T5) = {
    (javaTuple5.getT1, javaTuple5.getT2, javaTuple5.getT3, javaTuple5.getT4, javaTuple5.getT5)
  }

  implicit def tupleSix2ScalaTuple6[T1, T2, T3, T4, T5, T6](javaTuple6: Tuple6[T1, T2, T3, T4, T5, T6]): (T1, T2, T3, T4, T5, T6) = {
    (javaTuple6.getT1, javaTuple6.getT2, javaTuple6.getT3, javaTuple6.getT4, javaTuple6.getT5, javaTuple6.getT6)
  }

/*
Uncomment this when used. It is not used for now and reduce the code coverage
  implicit def try2Boolean[T](atry: Try[T]): Boolean = atry match {
    case Success(_) => true
    case Failure(_) => false
  }
*/

  type ScalaConsumer[T] = (T => Unit)
  type ScalaPredicate[T] = (T => Boolean)

  implicit def scalaConsumer2JConsumer[T](sc: ScalaConsumer[T]): Consumer[T] = {
    new Consumer[T] {
      override def accept(t: T): Unit = sc(t)
    }
  }

  implicit def scalaPredicate2JPredicate[T](sp: ScalaPredicate[T]): Predicate[T] = {
    new Predicate[T] {
      override def test(t: T): Boolean = sp(t)
    }
  }

  implicit def scalaLongConsumer2JLongConsumer(lc: ScalaConsumer[Long]): LongConsumer = {
    new LongConsumer {
      override def accept(value: Long): Unit = lc(value)
    }
  }

  implicit def scalaFunction2JavaFunction[T, U](function: T => U): Function[T, U] = {
    new Function[T, U] {
      override def apply(t: T): U = function(t)
    }
  }
}
