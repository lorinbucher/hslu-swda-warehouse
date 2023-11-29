package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.dto.LogEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Publishes log events to the log message queue.
 */
@Singleton
public final class EventLogger {

    private static final Logger LOG = LoggerFactory.getLogger(EventLogger.class);

    private final RabbitMqConfig config;
    private final BusConnector bus;

    /**
     * Constructor.
     */
    public EventLogger() {
        this.config = new RabbitMqConfig();
        this.bus = new BusConnector(config);
        this.bus.connectWithRetry();
    }

    /**
     * Publishes a log event message.
     *
     * @param logEvent Log event.
     */
    public void publishLog(final LogEventDTO logEvent) {
        try {
            String message = new ObjectMapper().writeValueAsString(logEvent);
            LOG.info("Sending log event message: {}", message);
            this.bus.talkAsync(config.getExchange(), Routes.LOG_EVENT, message);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize log event message: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("Failed to send log event message: {}", e.getMessage());
        }
    }
}
