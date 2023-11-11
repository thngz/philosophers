package dijkstrasolution

import java.util.concurrent.Semaphore
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random

enum class State {
    THINKING, HUNGRY, EATING
}

class DijkstraSolution(private val philosopherAmount: Int) {
    // acquired semaphore in this context means that philosopher
    // owns both forks
    private val acquiredForks = List(philosopherAmount) { Semaphore(1, true) }

    // lock to restrict access to critical section
    private val lock = ReentrantLock(true)
    private val philosopherStates = Array(philosopherAmount) { State.THINKING }
    private fun think(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId thinks, therefore he is")
        Thread.sleep(Random.nextLong(1000))
    }

    private fun eat(philId: Int, indentAmount: String) {
        println("${indentAmount}Philosopher $philId eats")
        Thread.sleep(Random.nextLong(1000))
    }

    private fun test(philId: Int) {
        val currentPhilosopherHungry = philosopherStates[philId] == State.HUNGRY
        val leftNeighborIsNotEating =
            philosopherStates[(philId - 1 + philosopherAmount) % philosopherAmount] != State.EATING
        val rightNeighborIsNotEating = philosopherStates[(philId + 1) % philosopherAmount] != State.EATING

        if (currentPhilosopherHungry && leftNeighborIsNotEating && rightNeighborIsNotEating) {
            philosopherStates[philId] = State.EATING
            acquiredForks[philId].release()
        }
    }

    private fun takeForks(philId: Int, indentAmount: String) {
        lock.withLock {
            philosopherStates[philId] = State.HUNGRY
            println("$indentAmount$philId is hungry")
            test(philId)
        }
        acquiredForks[philId].acquire()
    }

    private fun putForks(philId: Int) {
        lock.withLock {
            philosopherStates[philId] = State.THINKING
            test((philId - 1 + philosopherAmount) % philosopherAmount) // test left neighbor if he can eat
            test((philId + 1) % philosopherAmount)
        }
    }

    fun dine(philId: Int) {
        while (true) {
            val indentAmount = "\t".repeat(philId * 2)
            think(philId, indentAmount)
            takeForks(philId, indentAmount)
            eat(philId, indentAmount)
            putForks(philId)
        }
    }
}