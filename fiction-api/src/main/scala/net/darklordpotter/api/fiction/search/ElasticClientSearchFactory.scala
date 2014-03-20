package net.darklordpotter.api.fiction.search

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress

/**
 * 2014-03-19
 * @author Michael Rose <michael@fullcontact.com>
 */
object ElasticClientSearchFactory {
  def clientFromHost(host: String): Client = {
    new TransportClient().addTransportAddresses(new InetSocketTransportAddress(host, 9300))
  }
}