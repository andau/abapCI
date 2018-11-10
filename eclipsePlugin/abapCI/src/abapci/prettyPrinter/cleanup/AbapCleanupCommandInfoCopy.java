package abapci.prettyPrinter.cleanup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class AbapCleanupCommandInfoCopy {
	@SuppressWarnings("unused")
	private static final String SCOPE_ALL = "all";
	@SuppressWarnings("unused")
	private static final String SCOPE_SELECTION = "selection";
	@SuppressWarnings("unused")
	private static final String DELETE_UNUSED_VARIABLES_OPERATION = "deleteUnusedVariables";
	private static List<AbapCleanupCommandInfoCopy> registry = new ArrayList<AbapCleanupCommandInfoCopy>();
	private static boolean registryInitialized = false;
	private final String commandId;
	private final String operation;
	private final String scope;

	private static void initializeRegistry() {
		if (!registryInitialized) {
			AbapCleanupCommandInfoCopy.register("com.sap.adt.tools.abapsource.ui.cleanup.deleteUnusedVariables.all",
					"deleteUnusedVariables", "all");
			AbapCleanupCommandInfoCopy.register(
					"com.sap.adt.tools.abapsource.ui.cleanup.deleteUnusedVariables.selection", "deleteUnusedVariables",
					"selection");
		}
		registryInitialized = true;
	}

	private static void register(String commandId, String operation, String scope) {
		for (AbapCleanupCommandInfoCopy command : registry) {
			Assert.isTrue((boolean) (!command.getCommandId().equals(commandId)));
		}
		AbapCleanupCommandInfoCopy instance = new AbapCleanupCommandInfoCopy(commandId, operation, scope);
		registry.add(instance);
	}

	public static AbapCleanupCommandInfoCopy findByCommandId(String commandId) {
		AbapCleanupCommandInfoCopy.initializeRegistry();
		for (AbapCleanupCommandInfoCopy command : registry) {
			if (!command.getCommandId().equals(commandId))
				continue;
			return command;
		}
		return null;
	}

	private AbapCleanupCommandInfoCopy(String commandId, String operation, String scope) {
		Assert.isTrue((boolean) (commandId != null && operation != null && scope != null));
		this.commandId = commandId;
		this.operation = operation;
		this.scope = scope;
	}

	public String getCommandId() {
		return this.commandId;
	}

	public String getOperation() {
		return this.operation;
	}

	public String getScope() {
		return this.scope;
	}
}