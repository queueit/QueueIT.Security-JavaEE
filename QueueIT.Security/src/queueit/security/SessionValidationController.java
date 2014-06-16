package queueit.security;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Callable;

public class SessionValidationController {

    private static int defaultTicketExpiration = 0;
    private static IValidateResultRepository defaultValidationResultRepository = new CookieValidateResultRepository();
    private static Callable<IValidateResultRepository> defaultValidationResultProviderFactory = new Callable<IValidateResultRepository>() {
        public IValidateResultRepository call() {
            return defaultValidationResultRepository;
        }
    };

    SessionValidationController() {
        loadConfiguration();
    }

    public static void Configure(
            Integer ticketExpiration,
            Callable<IValidateResultRepository> validationResultProviderFactory) {
        if (ticketExpiration != null) {
            defaultTicketExpiration = ticketExpiration;
        }
        if (validationResultProviderFactory != null) {
            defaultValidationResultProviderFactory = validationResultProviderFactory;
        }
    }

    public static IValidateResult validateRequest() {
        return validateRequestDefaultQueue(null, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(Boolean includeTargetUrl) {
        return validateRequestDefaultQueue(includeTargetUrl, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(URI targetUrl) {
        return validateRequestDefaultQueue(null, targetUrl, null, null, null, null);
    }

    public static IValidateResult validateRequest(Boolean includeTargetUrl, Boolean sslEnabled) {
        return validateRequestDefaultQueue(includeTargetUrl, null, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(URI targetUrl, Boolean sslEnabled) {
        return validateRequestDefaultQueue(null, targetUrl, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestDefaultQueue(includeTargetUrl, null, sslEnabled, domainAlias, null, null);
    }

    public static IValidateResult validateRequest(
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestDefaultQueue(null, targetUrl, sslEnabled, domainAlias, null, null);
    }

    public static IValidateResult validateRequest(
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language) {
        return validateRequestDefaultQueue(includeTargetUrl, null, sslEnabled, domainAlias, language, null);
    }

    public static IValidateResult validateRequest(
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language) {
        return validateRequestDefaultQueue(null, targetUrl, sslEnabled, domainAlias, language, null);
    }

    public static IValidateResult validateRequest(
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        return validateRequestDefaultQueue(includeTargetUrl, null, sslEnabled, domainAlias, language, layoutName);
    }

    public static IValidateResult validateRequest(
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        return validateRequestDefaultQueue(null, targetUrl, sslEnabled, domainAlias, language, layoutName);
    }

    private static IValidateResult validateRequestDefaultQueue(
            Boolean includeTargetUrl,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        Queue queue = (Queue) QueueFactory.createQueue();

        return validateRequest(
                queue,
                sslEnabled != null ? sslEnabled : queue.getDefaultSslEnabled(),
                includeTargetUrl != null ? includeTargetUrl : queue.getDefaultIncludeTargetUrl(),
                targetUrl,
                domainAlias != null ? domainAlias : queue.getDefaultDomainAlias(),
                language != null ? language : queue.getDefaultLanguage(),
                layoutName != null ? layoutName : queue.getDefaultLayoutName());
    }

    public static IValidateResult validateRequest(String queueName) {
        return validateRequestQueueName(queueName, null, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(String queueName, Boolean includeTargetUrl) {
        return validateRequestQueueName(queueName, includeTargetUrl, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(String queueName, URI targetUrl) {
        return validateRequestQueueName(queueName, null, targetUrl, null, null, null, null);
    }

    public static IValidateResult validateRequest(
            String queueName,
            Boolean includeTargetUrl,
            Boolean sslEnabled) {
        return validateRequestQueueName(queueName, includeTargetUrl, null, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(
            String queueName,
            URI targetUrl,
            Boolean sslEnabled) {
        return validateRequestQueueName(queueName, null, targetUrl, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(
            String queueName,
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestQueueName(queueName, includeTargetUrl, null, sslEnabled, domainAlias, null, null);
    }

    public static IValidateResult validateRequest(
            String queueName,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestQueueName(queueName, null, targetUrl, sslEnabled, domainAlias, null, null);
    }

    private static IValidateResult validateRequestQueueName(
            String queueName,
            Boolean includeTargetUrl,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        if (queueName == null || queueName.isEmpty()) {
            throw new IllegalArgumentException("Queue name is required");
        }

        Queue queue = (Queue) QueueFactory.createQueue(queueName);

        return validateRequest(
                queue,
                sslEnabled != null ? sslEnabled : queue.getDefaultSslEnabled(),
                includeTargetUrl != null ? includeTargetUrl : queue.getDefaultIncludeTargetUrl(),
                targetUrl,
                domainAlias != null ? domainAlias : queue.getDefaultDomainAlias(),
                language != null ? language : queue.getDefaultLanguage(),
                layoutName != null ? layoutName : queue.getDefaultLayoutName());
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId) {
        return validateRequestFromIds(customerId, eventId, null, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            Boolean includeTargetUrl) {
        return validateRequestFromIds(customerId, eventId, includeTargetUrl, null, null, null, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            URI targetUrl) {
        return validateRequestFromIds(customerId, eventId, null, targetUrl, null, null, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            Boolean includeTargetUrl,
            Boolean sslEnabled) {
        return validateRequestFromIds(customerId, eventId, includeTargetUrl, null, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            URI targetUrl,
            Boolean sslEnabled) {
        return validateRequestFromIds(customerId, eventId, null, targetUrl, sslEnabled, null, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestFromIds(customerId, eventId, includeTargetUrl, null, sslEnabled, domainAlias, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias) {
        return validateRequestFromIds(customerId, eventId, null, targetUrl, sslEnabled, domainAlias, null, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language) {
        return validateRequestFromIds(customerId, eventId, includeTargetUrl, null, sslEnabled, domainAlias, language, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language) {
        return validateRequestFromIds(customerId, eventId, null, targetUrl, sslEnabled, domainAlias, language, null);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            Boolean includeTargetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        return validateRequestFromIds(customerId, eventId, includeTargetUrl, null, sslEnabled, domainAlias, language, layoutName);
    }

    public static IValidateResult validateRequest(
            String customerId,
            String eventId,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        return validateRequestFromIds(customerId, eventId, null, targetUrl, sslEnabled, domainAlias, language, layoutName);
    }

    private static IValidateResult validateRequestFromIds(
            String customerId,
            String eventId,
            Boolean includeTargetUrl,
            URI targetUrl,
            Boolean sslEnabled,
            String domainAlias,
            Locale language,
            String layoutName) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (eventId == null || eventId.isEmpty()) {
            throw new IllegalArgumentException("Event ID is required");
        }

        Queue queue = (Queue) QueueFactory.createQueue(customerId.toLowerCase(), eventId.toLowerCase());

        return validateRequest(
                queue,
                sslEnabled != null ? sslEnabled : queue.getDefaultSslEnabled(),
                includeTargetUrl != null ? includeTargetUrl : queue.getDefaultIncludeTargetUrl(),
                targetUrl,
                domainAlias != null ? domainAlias : queue.getDefaultDomainAlias(),
                language != null ? language : queue.getDefaultLanguage(),
                layoutName != null ? layoutName : queue.getDefaultLayoutName());
    }

    private static IValidateResult validateRequest(
            Queue queue,
            Boolean sslEnabled,
            Boolean includeTargetUrl,
            URI targetUrl,
            String domainAlias,
            Locale language,
            String layoutName) {
        IValidateResult sessionObject = null;
        try {
            sessionObject = defaultValidationResultProviderFactory.call().getValidationResult(queue);
        } catch (Exception ex) {
            // ignore
        }
        if (sessionObject != null) {
            AcceptedConfirmedResult confirmedResult = (AcceptedConfirmedResult) sessionObject;
            if (confirmedResult != null) {
                return new AcceptedConfirmedResult(queue, confirmedResult.getKnownUser(), false);
            }

            return sessionObject;
        }
        try {
            IKnownUser knownUser = KnownUserFactory.verifyMd5Hash();
            URI landingPage = null;
            if (knownUser == null) {
                if (targetUrl != null) {
                    landingPage = queue.getLandingPageUrl(targetUrl);
                } else {
                    landingPage = queue.getLandingPageUrl(includeTargetUrl);
                }

                if (landingPage != null) {
                    return new EnqueueResult(queue, landingPage);
                }

                if (targetUrl != null) {
                    return new EnqueueResult(queue, queue.getQueueUrl(targetUrl, sslEnabled, domainAlias, language, layoutName));
                } else {
                    return new EnqueueResult(queue, queue.getQueueUrl(includeTargetUrl, sslEnabled, domainAlias, language, layoutName));
                }
            }
            if (defaultTicketExpiration != 0 && knownUser.getTimeStamp().getTime() <= ((new Date()).getTime() - defaultTicketExpiration * 1000)) {
                throw new ExpiredValidationException(queue, knownUser);
            }

            AcceptedConfirmedResult result = new AcceptedConfirmedResult(queue, knownUser, true);
            try {
                defaultValidationResultProviderFactory.call().setValidationResult(queue, result);
            } catch (Exception ex) {
                //ignore
            }

            return result;
        } catch (InvalidKnownUserUrlException ex) {
            throw new KnownUserValidationException(ex, queue);
        } catch (InvalidKnownUserHashException ex) {
            throw new KnownUserValidationException(ex, queue);
        }
    }

    private static void loadConfiguration() {       
        try {
            // Load the properties
            Properties props = QueueitProperties.getProperties("queueit.properties");
            defaultTicketExpiration = Integer.parseInt(props.getProperty("ticketExpiration", "180"));
        } catch (Exception e) {
            // ignore
        }
    }
}
