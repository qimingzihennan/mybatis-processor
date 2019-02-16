package com.vcg.mybatis.example.starter.domain;

import org.apache.ibatis.cursor.Cursor;
import org.springframework.data.util.StreamUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

public class StreamCursor<T> implements Cursor<T>, Stream<T> {

    private Cursor<T> cursor;

    private Stream<T> stream;

    @Override
    public boolean isOpen() {
        return this.cursor.isOpen();
    }

    @Override
    public boolean isConsumed() {
        return this.cursor.isConsumed();
    }

    @Override
    public int getCurrentIndex() {
        return this.cursor.getCurrentIndex();
    }

    @Override
    public void close() {
        try {
            this.cursor.close();
            this.stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this.cursor.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        this.cursor.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return this.cursor.spliterator();
    }

    @Override
    public boolean isParallel() {
        return getStream().isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return getStream().sequential();
    }

    @Override
    public Stream<T> parallel() {
        return getStream().parallel();
    }

    @Override
    public Stream<T> unordered() {
        return getStream().unordered();
    }

    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        return getStream().onClose(closeHandler);
    }

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return getStream().filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return getStream().map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return getStream().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return getStream().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return getStream().mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return getStream().flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return getStream().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return getStream().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return getStream().flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return getStream().distinct();
    }

    @Override
    public Stream<T> sorted() {
        return getStream().sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return getStream().sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return getStream().peek(action);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        return getStream().limit(maxSize);
    }

    @Override
    public Stream<T> skip(long n) {
        return getStream().skip(n);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        getStream().forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return getStream().toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return getStream().toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return getStream().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return getStream().reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return this.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return getStream().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return getStream().collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return getStream().min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return getStream().max(comparator);
    }

    @Override
    public long count() {
        return getStream().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return getStream().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return getStream().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return getStream().noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return getStream().findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return getStream().findAny();
    }

    private Stream<T> getStream() {
        if (this.stream == null) {
            this.stream = StreamUtils.createStreamFromIterator(this.cursor.iterator());
        }
        return this.stream;
    }

    public void setCursor(Cursor<T> cursor) {
        this.cursor = cursor;
    }
}
