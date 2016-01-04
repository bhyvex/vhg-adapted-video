package fr.labri.progess.comet.app;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;

import fr.labri.progess.comet.config.LabriConfig;
import fr.labri.progess.comet.proxy.LabriDefaultHttpProxyServer;

public class App {

	public static void main(String[] args) {

		try {
			// read args from CLI

			LabriConfig result = CliFactory.parseArguments(LabriConfig.class, args);
			ContextProvider provider = new ContextProvider();
			provider.provide().setFrontalHostName(result.getFrontalHostName());
			provider.provide().setFrontalPort(result.getFrontalPort());

			// configure the api for confiruring the proxy
			if (result.isDebug())
				new ConfigManager();

			// configure the proxy
			new LabriDefaultHttpProxyServer(result, provider.provide().getContent(), provider.provide().getConfigs());

			// SchedulerUtils.setupScheduler(content, configs,
			// result.getFrontalHostName(), result.getFrontalPort());
		} catch (HelpRequestedException e) {
			System.out.println(e.getMessage());
		} catch (ArgumentValidationException e) {
			System.out.println(e.getMessage());
		}

	}

}
