package net.darklordpotter.api.fiction.search

import org.elasticsearch.action.{ActionListener, ListenableActionFuture}
import com.twitter.util.Promise

/**
 * 2014-03-18
 * @author Michael Rose
 */

object ElasticSearch {
  implicit class RichListenableFuture[T](val laf: ListenableActionFuture[T]) extends AnyVal {
    def toPromise: Promise[T] = {
      val p = Promise[T]()
      laf.addListener(new ActionListenerPromiseAdapter[T](p))
      p
    }
  }

  class ActionListenerPromiseAdapter[T](p:Promise[T]) extends ActionListener[T] {
    def onFailure(e: Throwable) = p.setException(e)
    def onResponse(response: T) = p.setValue(response)
  }
}