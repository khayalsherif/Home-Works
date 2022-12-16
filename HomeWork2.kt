
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

//Task: Write 2 methods to create and run 1000 coroutines.
//Tips: Documentation is here: https://kotlinlang.org/docs/coroutines-guide.html

//Given:
val state = MutableStateFlow("empty") // flow to update UI (in our case just print to logcat)

//Do suspend fun for subscribe to flow
suspend fun main() {
    runSync()
    runAsync()

    // subscribe to flow updates and print state.value to logcat.
    state.collect { value ->
        println(value)
    }
}

fun runSync() {
    println("runSync method.")

    // Set a CoroutineScope
    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch {
        repeat(1000) {
            doWork(it.toString())
        }
    }

    //  launch 1000 coroutines. Invoke doWork(index/number of coroutine) one after another. Example 1, 2, 3, 4, 5, etc.
}

fun runAsync() {
    println("runAsync method.")
    // Set a CoroutineScope
    val scope = CoroutineScope(Dispatchers.IO)

    repeat(1000) {
        scope.launch {
            doWork(it.toString())
        }
    }
    //  launch 1000 coroutines. Invoke doWork(index/number of coroutine) in async way. Example 1, 2, 5, 3, 4, 8, etc.
}

private suspend fun doWork(name: String) {
    delay(500)
    state.update { "$name completed." }
}
