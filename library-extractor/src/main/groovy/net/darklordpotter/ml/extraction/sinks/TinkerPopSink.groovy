/*
 * Copyright (C) 2013 Michael Rose
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 *  of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.darklordpotter.ml.extraction.sinks

import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.DataSink

/**
 * 2013-04-05
 * @author Michael Rose <michael@fullcontact.com>
 */
class TinkerPopSink implements DataSink {
    Neo4jGraph g
    TinkerPopSink() {
        g = new Neo4jGraph("/tmp/stories_neo4j")
    }

    void insertStory(Story result) {
        Vertex storyVertex = g.addVertex(result)
        storyVertex.setProperty("title", result.title)
        storyVertex.setProperty("author", result.author)


        for(String tag : result.tags) {
            Vertex tagVertex = g.addVertex(tag)
            storyVertex.addEdge("has_tag", tagVertex)
            tagVertex.addEdge("tagged", storyVertex)
        }
    }
}
