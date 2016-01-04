package fr.labri.progess.comet.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ContentWrapper implements Iterable<Content> {

	List<Content> contents;

	@XmlElementWrapper
	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	
	@Override
	public Iterator<Content> iterator() {
		return contents.iterator();
	}

	
	public static ContentWrapper wraps(Collection<Content> qsd) {
		ContentWrapper wrapper = new ContentWrapper();
		wrapper.setContents(new ArrayList<Content>(qsd));
		return wrapper;
	}

}
