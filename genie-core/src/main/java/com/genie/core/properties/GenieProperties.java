package com.genie.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Properties specific to Genie.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "genie")
public class GenieProperties {

    private final Async async = new Async();

    private final Http http = new Http();

    private final Cache cache = new Cache();

    private final Mail mail = new Mail();

    private final Security security = new Security();

    private final Swagger swagger = new Swagger();

    private final Metrics metrics = new Metrics();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Social social = new Social();

    private final Gateway gateway = new Gateway();

    private final Ribbon ribbon = new Ribbon();

    private final Registry registry = new Registry();

    private final Locale locale = new Locale();

    private final Multipart multipart = new Multipart();

    private final LoggingAspect loggingAspect = new LoggingAspect();

    public LoggingAspect getLoggingAspect() {
        return loggingAspect;
    }

    public Async getAsync() {
        return async;
    }

    public Http getHttp() {
        return http;
    }

    public Cache getCache() {
        return cache;
    }

    public Mail getMail() {
        return mail;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Security getSecurity() {
        return security;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public Social getSocial() {
        return social;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    public Locale getLocale() {
        return locale;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        public enum Version {V_1_1, V_2_0}

        private final Cache cache = new Cache();

        /**
         * HTTP version, must be "V_1_1" (for HTTP/1.1) or V_2_0 (for (HTTP/2)
         */
        public Version version = Version.V_1_1;

        public Cache getCache() {
            return cache;
        }

        public Version getVersion() {
            return version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public static class Cache {

            private int timeToLiveInDays = 1461;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    public static class Cache {

        private final Hazelcast hazelcast = new Hazelcast();

        private final Ehcache ehcache = new Ehcache();

        public Hazelcast getHazelcast() {
            return hazelcast;
        }

        public Ehcache getEhcache() {
            return ehcache;
        }

        public static class Hazelcast {

            private int timeToLiveSeconds = 3600;

            private int backupCount = 1;

            public int getTimeToLiveSeconds() {
                return timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public int getBackupCount() {
                return backupCount;
            }

            public void setBackupCount(int backupCount) {
                this.backupCount = backupCount;
            }
        }

        public static class Ehcache {

            private int timeToLiveSeconds = 3600;

            private long maxEntries = 100;

            public int getTimeToLiveSeconds() {
                return timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return maxEntries;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

    public static class Mail {

        private String from = "";

        private String baseUrl = "";

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class Security {

        private final RememberMe rememberMe = new RememberMe();

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public static class ClientAuthorization {

            private String accessTokenUri;

            private String tokenServiceId;

            private String clientId;

            private String clientSecret;

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }

        public static class Authentication {

            private final Oauth oauth = new Oauth();

            private final Jwt jwt = new Jwt();

            public Oauth getOauth() {
                return oauth;
            }

            public Jwt getJwt() {
                return jwt;
            }

            public static class Oauth {

                private String clientId;

                private String clientSecret;

                private int tokenValidityInSeconds = 1800;

                public String getClientId() {
                    return clientId;
                }

                public void setClientId(String clientId) {
                    this.clientId = clientId;
                }

                public String getClientSecret() {
                    return clientSecret;
                }

                public void setClientSecret(String clientSecret) {
                    this.clientSecret = clientSecret;
                }

                public int getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(int tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }
            }

            public static class Jwt {

                private String secret;

                private long tokenValidityInSeconds = 1800;

                private long tokenValidityInSecondsForRememberMe = 2592000;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class RememberMe {

            @NotNull
            private String key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

    public static class Swagger {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl = null;
        private String contactName = null;
        private String contactUrl = null;
        private String contactEmail = null;
        private String license = null;
        private String licenseUrl = null;
        private String defaultIncludePattern = "/api/.*";
        private String host = null;
        private String[] protocols = new String[0];

        public Swagger() {
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return this.termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return this.contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return this.contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return this.contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return this.license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return this.licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return this.defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String[] getProtocols() {
            return this.protocols;
        }

        public void setProtocols(String[] protocols) {
            this.protocols = protocols;
        }
    }

    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Graphite graphite = new Graphite();

        private final Prometheus prometheus = new Prometheus();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

        public Graphite getGraphite() {
            return graphite;
        }

        public Prometheus getPrometheus() {
            return prometheus;
        }

        public Logs getLogs() {
            return logs;
        }

        public static class Jmx {

            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Graphite {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 2003;

            private String prefix = "genieApplication";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }
        }

        public static class Prometheus {

            private boolean enabled = false;

            private String endpoint = "/prometheusMetrics";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getEndpoint() {
                return endpoint;
            }

            public void setEndpoint(String endpoint) {
                this.endpoint = endpoint;
            }
        }

        public static class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    private final Logging logging = new Logging();

    public Logging getLogging() {
        return logging;
    }

    public static class Logging {

        private String level = "debug";

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 5000;

            private int queueSize = 512;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }

        private final SpectatorMetrics spectatorMetrics = new SpectatorMetrics();

        public SpectatorMetrics getSpectatorMetrics() {
            return spectatorMetrics;
        }

        public static class SpectatorMetrics {

            private boolean enabled = false;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    public static class Social {

        private String redirectAfterSignIn = "/#/home";

        public String getRedirectAfterSignIn() {
            return redirectAfterSignIn;
        }

        public void setRedirectAfterSignIn(String redirectAfterSignIn) {
            this.redirectAfterSignIn = redirectAfterSignIn;
        }
    }

    public static class Gateway {

        private final RateLimiting rateLimiting = new RateLimiting();

        public RateLimiting getRateLimiting() {
            return rateLimiting;
        }

        private Map<String, List<String>> authorizedMicroservicesEndpoints = new LinkedHashMap<>();

        public Map<String, List<String>> getAuthorizedMicroservicesEndpoints() {
            return authorizedMicroservicesEndpoints;
        }

        public void setAuthorizedMicroservicesEndpoints(Map<String, List<String>> authorizedMicroservicesEndpoints) {
            this.authorizedMicroservicesEndpoints = authorizedMicroservicesEndpoints;
        }

        public static class RateLimiting {

            private boolean enabled = false;

            private long limit = 100000L;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getLimit() {
                return this.limit;
            }

            public void setLimit(long limit) {
                this.limit = limit;
            }
        }
    }

    public static class Ribbon {

        private String[] displayOnActiveProfiles;

        public String[] getDisplayOnActiveProfiles() {
            return displayOnActiveProfiles;
        }

        public void setDisplayOnActiveProfiles(String[] displayOnActiveProfiles) {
            this.displayOnActiveProfiles = displayOnActiveProfiles;
        }
    }

    private static class Registry {

        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Locale {
        private String[] basenames;

        public String[] getBasenames() {
            return basenames;
        }

        public void setBasenames(String[] basenames) {
            this.basenames = basenames;
        }
    }

    /**
     * 文件上传配置
     */
    public static class Multipart {
        private long maxFileSize;

        private long maxRequestSize;

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public long getMaxRequestSize() {
            return maxRequestSize;
        }

        public void setMaxRequestSize(long maxRequestSize) {
            this.maxRequestSize = maxRequestSize;
        }
    }

    public static class LoggingAspect {
        private String[] patterns;

        public String[] getPatterns() {
            return patterns;
        }

        public void setPatterns(String[] patterns) {
            this.patterns = patterns;
        }
    }

    public static class Service {

        private String runType;

        public String getRunType() {
            return runType;
        }

        public void setRunType(String runType) {
            this.runType = runType;
        }
    }
}
