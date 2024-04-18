package mappers;

public interface BaseMapper<S, D> {
    D map(S source);
}
