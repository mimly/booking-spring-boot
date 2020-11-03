package mimly.booking.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import com.hazelcast.map.listener.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import static mimly.booking.config.HazelcastHttpSessionConfig.MAX_INACTIVE_INTERVAL_IN_SECONDS;
import static mimly.booking.config.HazelcastHttpSessionConfig.SESSION_MAP_NAME;

@Configuration
@EnableHazelcastHttpSession(maxInactiveIntervalInSeconds = MAX_INACTIVE_INTERVAL_IN_SECONDS, sessionMapName = SESSION_MAP_NAME)
@Slf4j
public class HazelcastHttpSessionConfig {

    public final static int MAX_INACTIVE_INTERVAL_IN_SECONDS = 24 * 60 * 60;
    public final static int DEFAULT_INACTIVE_INTERVAL_IN_SECONDS = 5 * 60;
    public final static String SESSION_MAP_NAME = "sessions";

    private static class MyEntryListener implements
            EntryAddedListener<String, MapSession>,
            EntryEvictedListener<String, MapSession>,
            EntryExpiredListener<String, MapSession>,
            EntryLoadedListener<String, MapSession>,
            EntryRemovedListener<String, MapSession>,
            EntryUpdatedListener<String, MapSession>,
            MapClearedListener,
            MapEvictedListener {

        private String getMapSessionInfo(MapSession mapSession) {
            return mapSession == null
                    ? "null"
                    : String.format("[ creationTime: %s, lastAccessedTime: %s, maxInactiveInterval: %s, isExpired: %s ]",
                    mapSession.getCreationTime(),
                    mapSession.getLastAccessedTime(),
                    mapSession.getMaxInactiveInterval(),
                    mapSession.isExpired());
        }

        @Override
        public void entryAdded(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void entryEvicted(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void entryExpired(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s/%s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getOldValue()), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void entryLoaded(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void entryRemoved(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void entryUpdated(EntryEvent<String, MapSession> event) {
            log.debug(String.format("\033[1;48;5;17m %s: %s -> %s \033[0m", event.getEventType().name(), event.getKey(), getMapSessionInfo(event.getValue())));
        }

        @Override
        public void mapCleared(MapEvent event) {
            log.debug(String.format("\033[1;48;5;17m %s \033[0m", event));
        }

        @Override
        public void mapEvicted(MapEvent event) {
            log.debug(String.format("\033[1;48;5;17m %s \033[0m", event));
        }
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
//        MapConfig mapConfig = config.getMapConfig(SESSION_MAP_NAME);
//        mapConfig.setMaxIdleSeconds(30);
        /**
         * In order to serialize MapSession objects efficiently, HazelcastSessionSerializer needs to be registered.
         * If this is not set, Hazelcast will serialize sessions using native Java serialization.
         */
        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
        config.getSerializationConfig().addSerializerConfig(serializerConfig);
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        IMap<String, MapSession> iMap = hazelcastInstance.getMap(SESSION_MAP_NAME);
        iMap.addEntryListener(new MyEntryListener(), true);
        return hazelcastInstance;
    }
}