package net.darklordpotter.api.fiction

import com.twitter.finatra.FinatraServer
import net.darklordpotter.api.fiction.search.ElasticClientSearchFactory
import net.darklordpotter.api.fiction.service.{CORSFilter, SearchController}


object App extends FinatraServer {
  val searchClient = ElasticClientSearchFactory.clientFromHost("localhost")

  addFilter(new CORSFilter)
  register(new SearchController(searchClient))
}




