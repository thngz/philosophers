import dijkstrasolution.DijkstraSolution
import resourcehierarchy.DiningPhilosophersWithRH
import waiter.DiningPhilosophersWithWaiter
import kotlin.concurrent.thread

fun main() {
    val philosopherAmount = 5
    val rh = DiningPhilosophersWithRH(philosopherAmount)
    val waiter = DiningPhilosophersWithWaiter(philosopherAmount)
    val dijkstraSolution = DijkstraSolution(philosopherAmount)

    val t1 = thread(true, name = "Philosopher 1") {rh.dine(0)}
    val t2 = thread(true, name = "Philosopher 2") {rh.dine(1)}
    val t3 = thread(true, name = "Philosopher 3") {rh.dine(2)}
    val t4 = thread(true, name = "Philosopher 4") {rh.dine(3)}
    val t5 = thread(true, name = "Philosopher 5") {rh.dine(4)}

//    val t1 = thread(true, name = "Philosopher 1") {waiter.dine(0)}
//    val t2 = thread(true, name = "Philosopher 2") {waiter.dine(1)}
//    val t3 = thread(true, name = "Philosopher 3") {waiter.dine(2)}
//    val t4 = thread(true, name = "Philosopher 4") {waiter.dine(3)}
//    val t5 = thread(true, name = "Philosopher 5") {waiter.dine(4)}


//    val t1 = thread(true, name = "Philosopher 1") { dijkstraSolution.dine(0) }
//    val t2 = thread(true, name = "Philosopher 2") { dijkstraSolution.dine(1) }
//    val t3 = thread(true, name = "Philosopher 3") { dijkstraSolution.dine(2) }
//    val t4 = thread(true, name = "Philosopher 4") { dijkstraSolution.dine(3) }
//    val t5 = thread(true, name = "Philosopher 5") { dijkstraSolution.dine(4) }

    t1.join()
    t2.join()
    t3.join()
    t4.join()
    t5.join()
}