package waiter

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random


class DiningPhilosophersWithWaiter(private val philosopherAmount: Int) {
    // tracks how many resources are free
    // true indicates the fork is available
    private val forks = BooleanArray(philosopherAmount) { true }
    private val waiter = ReentrantLock(true)
    private val maxAmountOfForksUsedCondition = waiter.newCondition()
    private val neighboringForksTakenCondition = waiter.newCondition()

    private fun think(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId thinks, therefore he is")
        Thread.sleep(Random.nextLong(1000))
    }

    private fun eat(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId eats")
        Thread.sleep(Random.nextLong(1000))
    }

    private fun neighboringForksTaken(philId: Int): Boolean {
        // returns true if both left (forks[philid) is false and right (forks[(philid + 1)...])
        // is false
        return !(forks[philId] && forks[(philId + 1) % philosopherAmount])
    }

    private fun askForks(philId: Int, indentAmount: String) {
        // Each philosopher must ask the waiter for permission to use forks
        // Otherwise wait until fork frees up
        waiter.withLock {
            while (forks.count { !it } == philosopherAmount - 1) {
                maxAmountOfForksUsedCondition.await()
                println("$indentAmount$philId is waiting in the queue")
            }
            maxAmountOfForksUsedCondition.signalAll()

            while (neighboringForksTaken(philId)) {
                neighboringForksTakenCondition.await()
                println("$indentAmount$philId is waiting for neighboring forks to free")
            }

            neighboringForksTakenCondition.signalAll()

            forks[philId] = false
            forks[(philId + 1) % philosopherAmount] = false
            println("$indentAmount$philId locked both forks")
        }
    }

    private fun returnForks(philId: Int, indentAmount: String) {
        //set availability of the forks to true
        waiter.withLock {
            forks[philId] = true
            forks[(philId + 1) % philosopherAmount] = true
            println("${indentAmount}${philId} releases forks")
        }
    }


    fun dine(philId: Int) {
        while (true) {
            val indentAmount = "\t".repeat(philId * 2)
            think(philId, indentAmount)
            askForks(philId, indentAmount)
            eat(philId, indentAmount)
            returnForks(philId, indentAmount)
        }
    }
}