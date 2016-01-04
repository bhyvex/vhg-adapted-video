package fr.labri.progess.comet.app;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.glassfish.hk2.api.Factory;

import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.FilterConfig;

class ContextProvider implements Factory<Context> {

	// create global var which will be used to
	private static final ConcurrentMap<String, Content> content = new ConcurrentHashMap<String, Content>();
	private static final Set<FilterConfig> configs = new CopyOnWriteArraySet<FilterConfig>();

	final static Context context = new Context(content, configs);

	@Override
	public Context provide() {
		return context;
	}

	@Override
	public void dispose(Context instance) {

	}

}