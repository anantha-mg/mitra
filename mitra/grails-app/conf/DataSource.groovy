dataSource {
    pooled = true
    driverClassName = 'com.mysql.jdbc.Driver'
    dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
    username = "cloud"
    password = "scape"
    properties {
        maxActive = 100
        maxWait = 1000
        //    poolPreparedStatements = true
        defaultAutoCommit = true
        testOnBorrow = true
        testWhileIdle = true
        validationQuery = "SELECT 1"
    }
    url = "jdbc:mysql://localhost:3306/mitradb"
}

hibernate {
    // default Grails configuration:
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.use_second_level_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'

    query.substitutions = "true 'Y', false 'N'"

    temp.use_jdbc_metadata_defaults = false

    // hibernate search configuration:
    search.default.indexBase = '/tmp/'
}

environments {
    development {
        dataSource {
            dbCreate = "update"
        }
    }
    test {
        dataSource {
            url = "jdbc:mysql://localhost:3306/mitradb"
        }
    }
    production {
        dataSource {
            url = "jdbc:mysql://localhost:3306/mitradb"
        }
    }
}
