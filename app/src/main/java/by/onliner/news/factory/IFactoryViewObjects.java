package by.onliner.news.factory;

/**
 * Created by Mi Air on 13.10.2016.
 */

public interface IFactoryViewObjects<A,B> {
    B create(A data);
}
