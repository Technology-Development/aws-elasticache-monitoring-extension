package com.appdynamics.extensions.aws.elasticache;

import static com.appdynamics.extensions.aws.Constants.METRIC_PATH_SEPARATOR;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.config.Configuration;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;

/**
 * @author Florencio Sarmiento
 *
 */
public class ElastiCacheMonitor extends SingleNamespaceCloudwatchMonitor<Configuration> {
	
	private static final Logger LOGGER = Logger.getLogger("com.singularity.extensions.aws.ElastiCacheMonitor");

	private static final String DEFAULT_METRIC_PREFIX = String.format("%s%s%s%s", 
			"Custom Metrics", METRIC_PATH_SEPARATOR, "ElastiCache", METRIC_PATH_SEPARATOR);
	
	public ElastiCacheMonitor() {
		super(Configuration.class);
		LOGGER.info(String.format("Using AWS ElastiCacheMonitor Monitor Version [%s]", 
				this.getClass().getPackage().getImplementationTitle()));
	}

	@Override
	protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(
			Configuration config) {
		MetricsProcessor metricsProcessor = createMetricsProcessor(config);

		return new NamespaceMetricStatisticsCollector(
				config.getAccounts(),
				config.getConcurrencyConfig(), 
				config.getMetricsConfig(),
				metricsProcessor);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	protected String getMetricPrefix(Configuration config) {
		return StringUtils.isNotBlank(config.getMetricPrefix()) ? 
				config.getMetricPrefix() : DEFAULT_METRIC_PREFIX;
	}

	private MetricsProcessor createMetricsProcessor(Configuration config) {
		return new ElastiCacheMetricsProcessor(
				config.getMetricsConfig().getMetricTypes(), 
				config.getMetricsConfig().getExcludeMetrics());
	}

}