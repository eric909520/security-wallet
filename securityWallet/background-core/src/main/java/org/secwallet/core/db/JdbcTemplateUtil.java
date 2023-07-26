package org.secwallet.core.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 */
public class JdbcTemplateUtil {

    /**
     * Get the name JdbcTemplate.
     * <pre>
     * SQL statements take the form of the following templates.
     * select * from test where name=:name;
     * Map<String, Object> paramMap = new HashMap<String, Object>();
     *	paramMap.put("name", "name5");
     *</pre>
     * @param jdbcTemplate
     * @return
     */
    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

}
