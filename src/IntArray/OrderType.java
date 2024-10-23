package IntArray;

public enum OrderType {
    /**
     * По убыванию
     */
    descending,
    /**
     * По возрастанию
     */
    ascending,
    /**
     * Не упорядочены
     */
    unordered,
    /**
     * Не определено
     */
    unknown;

    @Override
    public String toString() {
        return switch (this) {
            case ascending -> "По возрастанию";
            case descending -> "По убыванию";
            case unknown -> "Не определено";
            case unordered -> "Не отсортирован";
        };
    }
}

