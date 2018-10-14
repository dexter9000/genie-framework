package com.genie.jpa.tenant;

/**
 * 分数据库多租户,
 * 需要搭配hibernate的多租户配置，如下：
 * <code>hibernate.multiTenancy: SCHEMA</code>
 * <code>hibernate.tenant_identifier_resolver: com.genie.jpa.tenant.TenantIdResolver</code>
 * <code>hibernate.multi_tenant_connection_provider: com.genie.jpa.tenant.SchemaBasedMultiTenantConnectionProvider</code>
 */
//public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider, Stoppable,
//    Configurable, ServiceRegistryAwareService {
//
//    private static final long serialVersionUID = 1L;
//    private final C3P0ConnectionProvider connectionProvider = new C3P0ConnectionProvider();
//    private final C3P0ConnectionProvider connectionProvider2 = new C3P0ConnectionProvider();
//
//    private final Map<String, C3P0ConnectionProvider> tenantIdConnMap = new HashMap<String, C3P0ConnectionProvider>();
//
//    private C3P0ConnectionProvider getProvider() {
//        String tenantIdentifier = ThreadLocalUtil.tendantId.get();
//        tenantIdentifier = tenantIdentifier.split(":")[0];
//        return tenantIdConnMap.get(tenantIdentifier);
//    }
//
//    @Override
//    public Connection getAnyConnection() throws SQLException {
//        return getProvider().getConnection();
//    }
//
//    @Override
//    public void releaseAnyConnection(Connection connection) throws SQLException {
//        getProvider().closeConnection(connection);
//    }
//
//    @Override
//    public Connection getConnection(String tenantIdentifier) throws SQLException {
//        //ThreadLocalUtil.tendantId.set(tenantIdentifier);
//        tenantIdentifier = tenantIdentifier.split(":")[1];
//        final Connection connection = getAnyConnection();
//        try {
//            connection.createStatement().execute("USE " + tenantIdentifier);
//        } catch (SQLException e) {
//            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier
//                + "]", e);
//        }
//        return connection;
//    }
//
//    @Override
//    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
//        try {
//            connection.createStatement().execute("USE test");
//        } catch (SQLException e) {
//            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier
//                + "]", e);
//        }
//        getProvider().closeConnection(connection);
//    }
//
//    @Override
//    public boolean isUnwrappableAs(Class unwrapType) {
//        return this.getProvider().isUnwrappableAs(unwrapType);
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> unwrapType) {
//        return this.getProvider().unwrap(unwrapType);
//    }
//
//    @Override
//    public void stop() {
//        this.getProvider().stop();
//    }
//
//    @Override
//    public boolean supportsAggressiveRelease() {
//        return this.getProvider().supportsAggressiveRelease();
//    }
//
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    @Override
//    public void configure(Map configurationValues) {
//
//        //connectorProvider初始化
//        this.connectionProvider.configure(configurationValues);
//
//        configurationValues.put("hibernate.connection.url", "jdbc:mysql://{db-server-url}:3306/dbname?useUnicode=true&amp;characterEncoding=utf8");
//        configurationValues.put("hibernate.connection.password", "password");
//        this.connectionProvider2.configure(configurationValues);
//
//        //connectorProvider与tenantId的关系映射
//        tenantIdConnMap.put("dataSource1", connectionProvider);
//        tenantIdConnMap.put("dataSource2", connectionProvider2);
//
//
//    }
//
//    @Override
//    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
//        connectionProvider.injectServices(serviceRegistry);
//        connectionProvider2.injectServices(serviceRegistry);
//    }
//
//}
