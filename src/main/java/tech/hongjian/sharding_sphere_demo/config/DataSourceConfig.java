package tech.hongjian.sharding_sphere_demo.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;

import io.shardingsphere.core.api.ShardingDataSourceFactory;
import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.core.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.api.config.strategy.StandardShardingStrategyConfiguration;


/**
 * @author xiahongjian 
 * @time   2018-07-30 14:16:37
 *
 */
@Configuration
public class DataSourceConfig {
	public static final long TABLE_MAX_COUNT = 100L;
	
	@Bean("master")
	@ConfigurationProperties(prefix = "spring.datasource.primary")
	public DataSource masterDataSource() {
		return new DruidDataSource();
	}
	
	@Bean("slave")
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	public DataSource slaveDataSource() {
		return new DruidDataSource();
	}
	
	
	@Bean
	@Primary	// 解决多个DataSource时，mybatis自动配置出错问题
	public DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getUserInfoTableRuleConfiguration());
//        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", new PreciseShardingAlgorithm<Long>() {
			@Override
			public String doSharding(Collection<String> availableTargetNames,
					PreciseShardingValue<Long> shardingValue) {
				for (String name : availableTargetNames) {
					long value = shardingValue.getValue();
					String suffix = (value / TABLE_MAX_COUNT) + "";
					if (name.endsWith(suffix))
						return name;
				}
				return null;
			}
		}, new RangeShardingAlgorithm<Long>() {
			@Override
			public Collection<String> doSharding(Collection<String> availableTargetNames,
					RangeShardingValue<Long> shardingValue) {
				Long lowerEndpoint = shardingValue.getValueRange().lowerEndpoint();
				Long upperEndpoint = shardingValue.getValueRange().upperEndpoint();
				List<String> res = new ArrayList<>();
				List<String> tables = inTables(lowerEndpoint, upperEndpoint);
				for (String name : availableTargetNames) {
					for (String suffix : tables) {
						if (name.endsWith(suffix))
							res.add(name);
					}
				}
				return res;
			}
		}));
        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());
		return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new HashMap<String, Object>(), new Properties());
    }
	
	private List<String> inTables(Long lowerEndpoint, Long upperEndpoint) {
		int fromIndex = (int) (lowerEndpoint / TABLE_MAX_COUNT);
		int toIndex = (int) (upperEndpoint / TABLE_MAX_COUNT) + 1;
		return IntStream.range(fromIndex, toIndex).mapToObj(o -> o + "").collect(Collectors.toList());
	}
	
    private TableRuleConfiguration getUserInfoTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration();
        result.setLogicTable("user_info");
        result.setActualDataNodes("ds0.user_info_${0..1}");
        result.setKeyGeneratorColumnName("user_id");
        return result;	
    }
    
    private List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("ds0", "masterDataSource", Arrays.asList("slaveDataSource"));
        return Lists.newArrayList(masterSlaveRuleConfig);
    }
    
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("masterDataSource", masterDataSource());
        result.put("slaveDataSource", slaveDataSource());
        return result;
    }
}
