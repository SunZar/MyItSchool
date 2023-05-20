package ru.sunzar.myitschool.utils;

public interface Resource<T> {
    class Success<T> implements Resource<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    class Error<T> implements Resource<T> {
    }

    class Loading<T> implements Resource<T> {
    }
}
