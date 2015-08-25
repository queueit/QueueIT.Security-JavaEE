package queueit.security;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class QueueFactory {
    private static String defaultHostDomain = "queue-it.net";

    private static Map<String, Queue> loadedQueues = new HashMap<String, Queue>();

    static void reset()
    {
        loadedQueues = new HashMap<String, Queue>();

        defaultHostDomain = "queue-it.net";
    }

    public static IQueue createQueue()
    {
        return createQueue("default");
    }

    public static IQueue createQueue(String queueName)
    {
        if (queueName == null || queueName.isEmpty())
            throw new IllegalArgumentException("Queue Name cannot be null or empty");

        String fileName = "queueit-" + queueName + ".properties";
        try {
            // Load the properties
            Properties props = QueueitProperties.getProperties(fileName);

            String landingPage = props.getProperty("landingPage", null);
            String language = props.getProperty("language", null);
            Queue queue = instantiateQueue(
                props.getProperty("customerId"),
                props.getProperty("eventId"),
                props.getProperty("domainAlias", null),
                landingPage == null ? null : landingPage,
                Boolean.valueOf(props.getProperty("useSsl", null)),
                Boolean.valueOf(props.getProperty("includeTargetUrl", null)),
                language == null || language.isEmpty() ? null : new Locale(language),
                props.getProperty("layoutName", null));

            return queue;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to initialize queue", e);
        }
    }

    public static IQueue createQueue(String customerId, String eventId)
    {
        if (customerId == null || customerId.isEmpty())
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        if (eventId == null || eventId.isEmpty())
            throw new IllegalArgumentException("Event ID cannot be null or empty");

        Queue queue = instantiateQueue(customerId, eventId, null, null, false, false, null, null);

        return queue;
    }

    public static void configure(String hostDomain)
    {
        if (hostDomain != null && !hostDomain.isEmpty())
            defaultHostDomain = hostDomain;
    }

    private static String generateKey(String customerId, String eventId)
    {
        return customerId + "_" + eventId;
    }

    private static Queue instantiateQueue(String customerId, String eventId, String domainAlias, String landingPage,
        Boolean sslEnabled, Boolean includeTargetUrl, Locale language, String layoutName)
    {
        String key = generateKey(customerId, eventId);

        Map<String, Queue> queues = loadedQueues;

        Queue queue = loadedQueues.get(key);

        if (queue != null)
            return queue;

        if (domainAlias == null || domainAlias.isEmpty())
        {
            domainAlias = customerId + "." + defaultHostDomain;
        }

        queue = new Queue(
            customerId,
            eventId,
            domainAlias,
            landingPage,
            sslEnabled,
            includeTargetUrl,
            language,
            layoutName);
        loadedQueues.put(key, queue);

        return queue;
    }
}
