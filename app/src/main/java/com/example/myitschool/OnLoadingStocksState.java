package com.example.myitschool;

import java.util.List;

public interface OnLoadingStocksState {
    public void changeState(State state);

    class State {
        static class Error extends State {}
        static class Loading extends State {}
        static class Success extends State {
            private final List<StocksShortData> movies;

            Success(List<StocksShortData> movies) {
                this.movies = movies;
            }

            public List<StocksShortData> getStocks() {
                return movies;
            }
        }
    }
}
