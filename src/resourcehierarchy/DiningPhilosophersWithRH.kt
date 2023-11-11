package resourcehierarchy

import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

class DiningPhilosophersWithRH(private val philosopherAmount: Int) {
    // array showing the availability of forks
    // true indicates the fork is free
    // false indicates that the fork is taken
    private val forks = List(philosopherAmount) { ReentrantLock(true) }
    private val leftForkLock = ReentrantLock()

    private fun shouldTakeLeftFirst(philId: Int): Boolean {
        // if philosopher is the last one, they should take left one first
        // if philosopher is not last one, they should take right fork first
        // thus preventing a situation where all of them lock up left fork (deadlock)
        return philId + 1 == philosopherAmount
    }

    private fun takeForks(philId: Int, indentAmount: String) {
        if (shouldTakeLeftFirst(philId)) {
            forks[philId].lock()
            forks[(philId + 1) % philosopherAmount].lock()
        } else {
            forks[(philId + 1) % philosopherAmount].lock()
            forks[philId].lock()
        }

        println("$indentAmount$philId both forks gotten")

    }

    private fun releaseForks(philId: Int, indentAmount: String) {
        forks[philId].unlock()
        println("$indentAmount$philId released left fork")
        forks[(philId + 1) % philosopherAmount].unlock()
        println("$indentAmount$philId released right fork")
    }

    private fun think(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId thinks, therefore he is")
        Thread.sleep(Random.nextLong(1000))
    }

    private fun eat(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId eats")
        Thread.sleep(Random.nextLong(1000))
    }

    fun dine(philId: Int) {
        while (true) {
            val indentAmount = "\t".repeat(philId * 2)
            think(philId, indentAmount)
            takeForks(philId, indentAmount)
            eat(philId, indentAmount)
            releaseForks(philId, indentAmount)
        }
    }
}