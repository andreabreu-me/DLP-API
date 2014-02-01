package net.darklordpotter.ml.new_extraction

import scala.collection.mutable.ListBuffer
import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.StoryElement

/**
 * 2013-08-14
 * @author Michael Rose <michael@fullcontact.com>
 */
class ExtractionPipeline[T <: StoryElement] {
  val actions:ListBuffer[ExtractionAction] = ListBuffer()

  def register(action: ExtractionAction) = {
    actions += action
    this
  }

  def execute(ctx: ExtractionContext) = {
    actions.foreach(ac => ac(ctx))
    ctx
  }
}