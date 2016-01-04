package fr.labri.progess.comet.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class FilterConfigWrapper implements Iterable<FilterConfig> {

	public Set<FilterConfig> getFilterConfigs() {
		return filterConfigs;
	}

	public void setFilterConfigs(Set<FilterConfig> filterConfigs) {
		this.filterConfigs = filterConfigs;
	}

	Set<FilterConfig> filterConfigs = new HashSet<FilterConfig>();

	
	@Override
	public Iterator<FilterConfig> iterator() {
		return filterConfigs.iterator();
	}

	public static FilterConfigWrapper wraps(Set<FilterConfig> filterConfigs) {
		FilterConfigWrapper wrapper = new FilterConfigWrapper();
		wrapper.setFilterConfigs(filterConfigs);
		return wrapper;
	}
}
