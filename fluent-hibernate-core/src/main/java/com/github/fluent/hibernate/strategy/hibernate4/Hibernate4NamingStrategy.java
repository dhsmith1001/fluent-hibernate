package com.github.fluent.hibernate.strategy.hibernate4;

import org.hibernate.cfg.ImprovedNamingStrategy;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.strategy.HibernateNamingStrategy;
import com.github.fluent.hibernate.strategy.JoinTableNames;
import com.github.fluent.hibernate.strategy.JoinTableNames.TableDescription;
import com.github.fluent.hibernate.strategy.NamingStrategyUtils;

/**
 * A naming strategy for Hibernate 4.
 *
 * @author V.Ladynev
 */
public class Hibernate4NamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = -7668259354481970558L;

    private final HibernateNamingStrategy strategy = new HibernateNamingStrategy();

    private final JoinTableNames joinTableNames = new JoinTableNames();

    public void setTablePrefix(String tablePrefix) {
        strategy.setTablePrefix(tablePrefix);
    }

    @Override
    public String classToTableName(String className) {
        return strategy.classToTableName(className);
    }

    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable,
            String associatedEntity, String associatedEntityTable, String propertyName) {
        String tableName = strategy.collectionTableName(ownerEntityTable, associatedEntityTable);

        TableDescription description = new TableDescription(ownerEntityTable,
                associatedEntityTable, propertyName);

        String result = joinTableNames.hasSameNameForOtherProperty(tableName, description) ? strategy
                .collectionTableName(ownerEntityTable, associatedEntityTable, propertyName)
                : tableName;

        joinTableNames.put(result, description);

        return result;
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName,
            String propertyTableName, String referencedColumnName) {
        return strategy.foreignKeyColumnName(propertyName, propertyTableName);
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return strategy.joinKeyColumnName(joinedColumn, joinedTable);
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        String forEmbedded = NamingStrategyUtils.addUnderscores(propertyName);
        return strategy.propertyToColumnName(forEmbedded);
    }

    @Override
    public String logicalColumnName(String columnName, String propertyName) {
        // it is for embeddable properties
        return InternalUtils.StringUtils.isEmpty(columnName) ? propertyName : columnName;
    }

}