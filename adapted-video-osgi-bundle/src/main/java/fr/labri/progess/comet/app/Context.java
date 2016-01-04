package fr.labri.progess.comet.app;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import fr.labri.progess.comet.config.LabriConfig;
import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.FilterConfig;

public class Context {

	public Context(ConcurrentMap<String, Content> content, Set<FilterConfig> configs) {
		this.setContent(content);
		this.setConfigs(configs);
	}

	public ConcurrentMap<String, Content> getContent() {
		return content;
	}

	private void setContent(ConcurrentMap<String, Content> content) {
		this.content = content;
	}

	public Set<FilterConfig> getConfigs() {
		return configs;
	}

	private void setConfigs(Set<FilterConfig> configs) {
		this.configs = configs;
	}

	private ConcurrentMap<String, Content> content;
	private Set<FilterConfig> configs;

	private String frontalHostName;

	public String getFrontalHostName() {
		return frontalHostName;
	}

	public Integer getFrontalPort() {
		return frontalPort;
	}

	private Integer frontalPort;

	public void setFrontalHostName(String frontalHostName) {
		this.frontalHostName = frontalHostName;

	}

	public void setFrontalPort(Integer frontalPort) {
		this.frontalPort = frontalPort;

	}

}
