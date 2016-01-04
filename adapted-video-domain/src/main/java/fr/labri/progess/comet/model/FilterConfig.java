package fr.labri.progess.comet.model;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilterConfig {

	private Collection<FileType> fileTypes = new HashSet<FileType>();
	public Collection<HeaderFilter> getHeaderResponseFilters() {
		return headerResponseFilters;
	}


	public void setHeaderResponseFilters(Collection<HeaderFilter> headerResponseFilters) {
		this.headerResponseFilters = headerResponseFilters;
	}


	public Collection<HeaderFilter> getHeaderRequestFilters() {
		return headerRequestFilters;
	}


	public void setHeaderRequestFilters(Collection<HeaderFilter> headerRequestFilters) {
		this.headerRequestFilters = headerRequestFilters;
	}


	public void setFileTypes(Collection<FileType> fileTypes) {
		this.fileTypes = fileTypes;
	}



	private Collection<HeaderFilter> headerResponseFilters = new HashSet<HeaderFilter>();
	private Collection<HeaderFilter> headerRequestFilters = new HashSet<HeaderFilter>();

	public Collection<FileType> getFileTypes() {
		return fileTypes;
	}

	
	public Collection<HeaderFilter> getHeaderResponseValues() {
		return headerResponseFilters;
	}

	

	public Collection<HeaderFilter> getHeaderRequestValues() {
		return headerRequestFilters;
	}

	

}
