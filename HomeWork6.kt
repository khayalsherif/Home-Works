import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicReference

suspend fun main() {
    val numbers = (1..5).asFlow().onEach { delay(300) }
    val strings = flowOf("A", "B", "C", "D").onEach { delay(400) }

    numbers.withLatestFrom(strings) { a, b -> "$a$b" }.collect { value ->
        println(value)
    }
}

fun <A, B : Any, R> Flow<A>.withLatestFrom(
    other: Flow<B>,
    transform: suspend (A, B) -> R
): Flow<R> = flow {
    coroutineScope {
        val latestB = AtomicReference<B?>()
        val outerScope = this
        launch {
            try {
                other.collect { latestB.set(it) }
            } catch (e: CancellationException) {
                outerScope.cancel(e)
            }
        }
        collect { a: A ->
            latestB.get()?.let { b -> emit(transform(a, b)) }
        }
    }
}
