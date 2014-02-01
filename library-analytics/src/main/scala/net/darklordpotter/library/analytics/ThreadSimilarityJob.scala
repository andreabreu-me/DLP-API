package net.darklordpotter.library.analytics

import com.twitter.scalding._

class ThreadSimilarityJob(args : Args) extends Job(args) {
  // tr.userid,tr.threadid,tr.vote,UNIX_TIMESTAMP(tr.date) as date,t.forumid
  val input = Csv( "/tmp/ratings.csv", skipHeader = true)

//  val output = JDBCTap

  val ratings = input.mapTo((0,1,2,3,4) -> ('userId,'threadId,'vote,'date,'forumId))  { fields : (Long, Long, Long, Long, Long) => fields }

  val numRaters =
    ratings
      // Put the number of people who rated each movie into a field called "numRaters".
      .groupBy('threadId) { _.size }.rename('size -> 'numRaters) // Shortcut: .groupBy('movie) { _.size('numRaters) }
      // Rename, since when we join, Scalding currently requires both sides to have distinctly named fields.
      .rename('threadId -> 'threadIdX)


  // Merge `ratings` with `numRaters`, by joining on their movie fields.
  val ratingsWithSize =
    numRaters
      .joinWithSmaller('threadIdX -> 'threadId, ratings)
      .discard('threadIdX) // Remove the extra field.

  val ratings2 =
    ratingsWithSize
      .rename(('numRaters, 'userId,'threadId,'vote,'date,'forumId) -> ('numRaters2,'userId2,'threadId2,'vote2,'date2,'forumId2))

  val ratingPairs =
    ratingsWithSize
      .joinWithSmaller('userId -> 'userId2, ratings2)
      // De-dupe so that we don't calculate similarity of both (A, B) and (B, A).
      .filter('threadId, 'threadId2) { movies : (String, String) => movies._1 < movies._2 }
      .project('threadId, 'vote, 'numRaters, 'threadId2, 'vote2, 'numRaters2)


  val vectorCalcs =
    ratingPairs
      // Compute (x*y, x^2, y^2), which we need for dot products and norms.
      .map(('vote, 'vote2) -> ('ratingProd, 'ratingSq, 'rating2Sq)) {
      ratings : (Double, Double) =>
        (ratings._1 * ratings._2, math.pow(ratings._1, 2), math.pow(ratings._2, 2))
    }
      .groupBy('threadId, 'threadId2) {
      _
        .size // length of each vector
        .sum('ratingProd -> 'dotProduct)
        .sum('vote -> 'ratingSum)
        .sum('vote2 -> 'rating2Sum)
        .sum('ratingSq -> 'ratingNormSq)
        .sum('rating2Sq -> 'rating2NormSq)
        .max('numRaters) // Just an easy way to make sure the numRaters field stays.
        .max('numRaters2)
      // Notice that all of these operations chain together like in a builder object.
    }

  val correlations =
    vectorCalcs
      .map(('size, 'dotProduct, 'ratingSum, 'rating2Sum, 'ratingNormSq, 'rating2NormSq) -> 'correlation) {
      fields : (Double, Double, Double, Double, Double, Double) =>
        correlation(fields._1, fields._2, fields._3, fields._4, fields._5, fields._6)
    }

  val PRIOR_COUNT = 10
  val PRIOR_CORRELATION = 0

  // Other similarity metrics
  val similarities =
    vectorCalcs
      .map(('size, 'dotProduct, 'ratingSum, 'rating2Sum, 'ratingNormSq, 'rating2NormSq, 'numRaters, 'numRaters2) ->
      ('correlation, 'regularizedCorrelation, 'cosineSimilarity, 'jaccardSimilarity)) {

      fields : (Double, Double, Double, Double, Double, Double, Double, Double) =>

        val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields

        val corr = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
        val regCorr = regularizedCorrelation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, PRIOR_COUNT, PRIOR_CORRELATION)
        val cosSim = cosineSimilarity(dotProduct, math.sqrt(ratingNormSq), math.sqrt(rating2NormSq))
        val jaccard = jaccardSimilarity(size, numRaters, numRaters2)

        (corr, regCorr, cosSim, jaccard)
    }

  // Exclude 'NaN' from result set
  val onlyRealResults =
    similarities.filter('correlation) { correlation : Double => correlation > 0 }

  // Write to output file
  onlyRealResults.write( Csv( args("output"), writeHeader = true ) )


  def correlation(size : Double, dotProduct : Double, ratingSum : Double,
                  rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double) = {

    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = math.sqrt(size * ratingNormSq - ratingSum * ratingSum) * math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)

    numerator / denominator
  }

  def cosineSimilarity(dotProduct : Double, ratingNorm : Double, rating2Norm : Double) = {
    dotProduct / (ratingNorm * rating2Norm)
  }

  def regularizedCorrelation(size : Double, dotProduct : Double, ratingSum : Double,
                             rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double,
                             virtualCount : Double, priorCorrelation : Double) = {

    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + virtualCount)

    w * unregularizedCorrelation + (1 - w) * priorCorrelation
  }

  def jaccardSimilarity(usersInCommon : Double, totalUsers1 : Double, totalUsers2 : Double) = {
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }
}