package com.knoldus.inMemoryDB

import redis.RedisClient
import redis.commands.TransactionBuilder
import redis.protocol.MultiBulk

import scala.concurrent.Future

class RedisHandler {

  implicit val akkaSystem = akka.actor.ActorSystem()
  val redisServerPort = 6379
  val redis = RedisClient("localhost", redisServerPort)
  val transaction: TransactionBuilder = redis.transaction()

  def ingestStatuses(technologyMap: Map[String, Long]): Future[MultiBulk] = {
    technologyMap.keys.foreach { technology =>
      transaction.watch(technology)
      transaction.set(technology, technologyMap(technology))

      println(technology + ":" + technologyMap(technology))
    }

    transaction.exec()
  }

}
