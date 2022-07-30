package pl.battleships.javaquarkusship.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import javax.validation.constraints.Max;

/**
 * @see <a href="https://quarkus.io/guides/config-mappings">config-mappings documentation</a>
 */
@StaticInitSafe
@ConfigMapping(prefix = "game")
public interface GameProperties {
    /**
     * @return
     */
    String team();

    /**
     * @return
     */
    @WithDefault("100")
    @Max(10000)
    long sleep();
}
