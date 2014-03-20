package net.darklordpotter.api.fiction.service

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Response, Request}

/**
 * 2014-03-19
 * @author Michael Rose <michael@fullcontact.com>
 */

class CORSFilter extends SimpleFilter[Request, Response] {
  def apply(request: Request, service: Service[Request, Response]) = {
    service(request) map { response =>
      response.headers().set("Access-Control-Allow-Origin", "*")
      response
    }
  }
}