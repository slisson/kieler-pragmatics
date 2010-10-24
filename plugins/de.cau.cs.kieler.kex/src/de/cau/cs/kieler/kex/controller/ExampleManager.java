/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2009 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.kex.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

import de.cau.cs.kieler.core.KielerException;
import de.cau.cs.kieler.kex.controller.util.ExampleExport;
import de.cau.cs.kieler.kex.controller.util.ExampleImport;
import de.cau.cs.kieler.kex.model.Example;
import de.cau.cs.kieler.kex.model.SourceType;
import de.cau.cs.kieler.kex.model.database.DBExampleCollector;
import de.cau.cs.kieler.kex.model.plugin.PluginExampleCollector;
import de.cau.cs.kieler.kex.model.plugin.PluginExampleCreator;

public class ExampleManager {

	private static ExampleManager instance;

	/**
	 * set true, if examples has been loaded, otherwise false.
	 */
	private boolean isLoaded;

	private final PluginExampleCollector extensionCollector;
	private final DBExampleCollector databaseCollector;

	private final PluginExampleCreator extensionCreator;

	// TODO wenn in ui ein editor offen ist, dann macht er den wizard nicht auf.

	// TODO Thesis, begr�nden weshalb hier instance genommen wurde.
	// da wir den Examplepool nicht jedes mal erneut laden wollen, wenn
	// wir darauf zugreifen wollen, k�nnen unter anderem viele werden.

	// TODO category refactoring, das wird so alles nicht mehr gebraucht...
	// da wir die kategorien aus den examples filtern.

	private ExampleManager() {
		this.extensionCollector = new PluginExampleCollector();
		this.extensionCreator = new PluginExampleCreator();
		this.databaseCollector = new DBExampleCollector();
	}

	public synchronized static ExampleManager get() {
		if (instance == null)
			instance = new ExampleManager();
		return instance;
	}

	/**
	 * Loads examples, if not loaded before.
	 * 
	 * @param forceLoad
	 *            , set this parameter to force loading of examples
	 * @throws KielerException
	 */
	public void load(boolean forceLoad) throws KielerException {
		if (!this.isLoaded || forceLoad) {
			load();
			// after completely loaded
			this.isLoaded = true;
		}
	}

	public Example getExample(SourceType type, String exampleTitle)
			throws KielerException {
		if (type == SourceType.KIELER) {
			if (isLoaded) {
				Map<String, Example> examplePool = extensionCollector
						.getExamplePool();
				Example example = examplePool.get(exampleTitle);
				if (example != null) {
					return example;
				} else {
					throw new KielerException(ErrorMessage.NO_EXAMPLE_FOUND
							+ exampleTitle);
				}
			} else {
				return PluginExampleCollector.getExample(exampleTitle);
			}
		}
		if (type == SourceType.PUBLIC) {
			// search in online interface for example
			return DBExampleCollector.getExample(exampleTitle);
		}
		return null;
	}

	private void load() throws KielerException {
		this.extensionCollector.load();
		// TODO test impl of an online interface.
		this.databaseCollector.load();
	}

	public Map<String, Example> getExamples() {
		Map<String, Example> result = this.extensionCollector.getExamplePool();
		result.putAll(databaseCollector.getExamplePool());
		return result;
	}

	public List<String> getCategories() {
		List<String> result = new ArrayList<String>();
		result.addAll(databaseCollector.getCategories());
		result.addAll(extensionCollector.getCategories());
		return result;
	}

	public List<String> importExamples(IPath selectedResource,
			List<Example> selectedExamples, boolean checkDuplicate)
			throws KielerException {
		ExampleImport.validate(selectedResource, selectedExamples,
				checkDuplicate);
		return ExampleImport.importExamples(selectedResource, selectedExamples,
				checkDuplicate);
	}

	/**
	 * Exports a given example. Created and deleted categories will managed,
	 * too.
	 * 
	 * @param properties
	 * @throws KielerException
	 */
	public void export(Map<ExampleElement, Object> properties)
			throws KielerException {

		ExampleExport.validate(properties, this.extensionCollector,
				this.databaseCollector);

		if (SourceType.KIELER.equals(properties.get(ExampleElement.SOURCETYPE)))
			ExampleExport.exportInPlugin(properties, this.extensionCreator);
		else if (SourceType.PUBLIC.equals(properties
				.get(ExampleElement.SOURCETYPE))) {
			// TODO build online interface
		} else
			throw new KielerException(ErrorMessage.NO_SOURCETYPE);
	}

	public InputStream loadOverviewPic(Example example) throws KielerException {
		return ExampleImport.loadOverviewPic(example);
	}

	public InputStream loadStandardPic() {
		return ExampleImport.loadStandardPic();
	}

	public List<String> quickStartImport(Example quickStarter)
			throws KielerException {
		List<Example> quickStarts = new ArrayList<Example>();
		quickStarts.add(quickStarter);
		return ExampleImport.importExamples(null, quickStarts, false);
	}

}
