package com.samuel.distributed.brute.force.reader.util;

import com.google.common.collect.AbstractIterator;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Based on http://stackoverflow.com/questions/2149244/bufferediterator-implementation
 */
public class Iterators {

    private static ExecutorService defaultExecutor = Executors.newCachedThreadPool();

    private static final Object END_MARKER = new Object();

    private static final Object NULL_MARKER = new Object();

    public static <E> Iterator<E> buffer(final Iterator<E> source, final int capacity) {
        return buffer(source, capacity, defaultExecutor);
    }

    public static <E> Iterator<E> buffer(final Iterator<E> source, final int capacity, final ExecutorService exec) {
        if (capacity <= 0) return source;
        final BlockingQueue<E> queue = new ArrayBlockingQueue<>(capacity);

        // Temporary storage for an element we fetched but could not fit in the queue
        final AtomicReference<E> overflow = new AtomicReference<>();
        final Runnable bufferReader = new BufferReader<E>(source, queue, exec, overflow);
        // Fetch the first element.
        // The inserter will resubmit itself as necessary to fetch more elements.
        exec.submit(bufferReader);
        return new BufferedIterator<>(queue, exec, overflow, bufferReader);
    }

    @RequiredArgsConstructor
    private static class BufferedIterator<E> extends AbstractIterator<E> {
        private final BlockingQueue<E> queue;
        private final ExecutorService exec;
        private final AtomicReference<E> overflow;
        private final Runnable bufferReader;

        @Override
        protected E computeNext() {
            try {
                final E next = queue.take();
                final E overflowElem = overflow.getAndSet(null);
                if (overflowElem != null) {
                    // There is now a space in the queue
                    queue.put(overflowElem);
                    // Awaken the inserter thread
                    exec.submit(bufferReader);
                }
                if (next == END_MARKER) {
                    return endOfData();
                } else if (next == NULL_MARKER) {
                    return null;
                } else {
                    return next;
                }
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
                return endOfData();
            }
        }
    }

    @RequiredArgsConstructor
    private static class BufferReader<E> implements Runnable {
        private final Iterator<E> source;
        private final BlockingQueue<E> queue;
        private final ExecutorService exec;
        private final AtomicReference<E> overflow;

        @SuppressWarnings("unchecked")
        public void run() {
            E next = (E) END_MARKER;
            if (source.hasNext()) {
                next = source.next();
                // ArrayBlockingQueue does not allow nulls
                if (next == null) next = (E) NULL_MARKER;
            }
            if (queue.offer(next)) {
                // Keep buffering elements as long as we can
                if (next != END_MARKER) exec.submit(this);
            } else {
                // Save the element.  This also signals to the
                // iterator that the inserter thread is blocked.
                overflow.lazySet(next);
            }
        }
    }
}