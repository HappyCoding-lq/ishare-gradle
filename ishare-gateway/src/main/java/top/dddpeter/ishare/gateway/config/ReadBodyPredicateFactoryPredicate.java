package top.dddpeter.ishare.gateway.config;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * @author : huangshuanbao
 */
@Component
public class ReadBodyPredicateFactoryPredicate implements Predicate {

    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    @Override
    public boolean test(Object o) {
        return true;
    }

}
