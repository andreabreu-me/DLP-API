package net.darklordpotter.api.fiction.search

import org.elasticsearch.action.{ActionListener, ListenableActionFuture}
import com.twitter.util.{Promise => TwPromise}
import rx.lang.scala.Observable
import scala.concurrent.Promise
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * 2014-03-18
 * @author Michael Rose
 */

object ElasticSearch {
  implicit class RichListenableFuture[T](val laf: ListenableActionFuture[T]) extends AnyVal {
    def toPromise: TwPromise[T] = {
      val p = TwPromise[T]()
      laf.addListener(new ActionListenerTwPromiseAdapter[T](p))
      p
    }
    def toObservable: Observable[T] = {
      val p = Promise[T]()
      laf.addListener(new ActionListenerPromiseAdapter[T](p))
      Observable.from(p.future)
    }
  }

  class ActionListenerTwPromiseAdapter[T](p:TwPromise[T]) extends ActionListener[T] {
    def onFailure(e: Throwable) = p.setException(e)
    def onResponse(response: T) = p.setValue(response)
  }

  class ActionListenerPromiseAdapter[T](p:Promise[T]) extends ActionListener[T] {
    def onFailure(e: Throwable) = p.failure(e)
    def onResponse(response: T) = p.success(response)
  }
}