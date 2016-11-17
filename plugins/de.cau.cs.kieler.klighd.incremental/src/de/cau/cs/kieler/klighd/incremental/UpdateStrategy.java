/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2014 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */

package de.cau.cs.kieler.klighd.incremental;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import de.cau.cs.kieler.klighd.IUpdateStrategy;
import de.cau.cs.kieler.klighd.ViewContext;
import de.cau.cs.kieler.klighd.incremental.diff.Comparison;
import de.cau.cs.kieler.klighd.incremental.merge.KGraphMerger;
import de.cau.cs.kieler.klighd.incremental.util.UIDAdapter;
import de.cau.cs.kieler.klighd.incremental.util.UIDAdapters;
import de.cau.cs.kieler.klighd.kgraph.KNode;
import de.cau.cs.kieler.klighd.krendering.SimpleUpdateStrategy;

/**
 * Implements an incremental update strategy which uses EMF Compare
 * to compare and merge the KGraph view models.
 * 
 * @author csp
 */
public class UpdateStrategy implements IUpdateStrategy {

    /** The id used at registration of this strategy in the plugin.xml. */
    public static final String ID = UpdateStrategy.class.getCanonicalName();

    /** This plugin's id. */
    public static final String PLUGIN_ID = "de.cau.cs.kieler.klighd.incremental";

    /** the priority for this update strategy. */
    public static final int PRIORITY = 30;
    
    private SimpleUpdateStrategy fallbackDelegate = null;

    /**
     * {@inheritDoc}
     */
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * {@inheritDoc}
     */
    public void update(final KNode baseModel, final KNode newModel, final ViewContext viewContext) {
        final boolean debug = false;
        System.out.println("update " + System.currentTimeMillis());
        if (debug) {
            serialize("origin.kgx", EcoreUtil.copy(baseModel));
            serialize("new.kgx", EcoreUtil.copy(newModel));
        }
        if (baseModel.getChildren().isEmpty()) {
            fallback(baseModel, newModel, viewContext);
            System.out.println("simple init: Empty base model.");
            return;
        }
        System.out.println("retrieving base adapter");
        UIDAdapter baseAdapter = UIDAdapters.retrieveAdapter(baseModel);
        if (baseAdapter.isInvalid()) {
            UIDAdapters.removeAdapter(baseModel);
            fallback(baseModel, newModel, viewContext);
            System.out.println("simple update: Duplicate UID in base model!");
            return;
        }
        System.out.flush();
        System.out.println("retrieving new adapter");
        UIDAdapter newAdapter = UIDAdapters.retrieveAdapter(newModel);
        if (newAdapter.isInvalid()) {
            UIDAdapters.removeAdapter(newModel);
            fallback(baseModel, newModel, viewContext);
            System.out.println("simple update: Duplicate UID in new model!");
            return;
        }
        
        try {
            Comparison comparison = new Comparison(baseAdapter, newAdapter);
            KGraphMerger merger = new KGraphMerger(comparison);
            merger.merge();

            UIDAdapters.removeAdapter(newModel);

        } catch (RuntimeException e) {
            final String msg = "KLighD: Incremental update of diagram by means of the EMF"
                    + "Compare-based update strategy failed.";
            StatusManager.getManager().handle(new Status(IStatus.ERROR, PLUGIN_ID, msg, e),
                    StatusManager.LOG);
            e.printStackTrace();
            
            // if incremental updating failed, apply the SimpleUpdateStrategy
            fallback(baseModel, newModel, viewContext);
        }

        if (debug) {
            serialize("merged.kgx", EcoreUtil.copy(baseModel));
        }
    }

    private void fallback(final KNode baseModel, final KNode newModel,
            final ViewContext viewContext) {

        if (this.fallbackDelegate == null) {
            this.fallbackDelegate = new SimpleUpdateStrategy();
        }
        this.fallbackDelegate.update(baseModel, newModel, viewContext);
    }

    /** A helper for debugging purposes! */
    // @SuppressWarnings("unused")
    private void serialize(final String path, final EObject... objects) {
        URI fileURI = URI.createURI("platform:/meta/" + PLUGIN_ID + "/" + path, false);

        ResourceSet set = new ResourceSetImpl();
        Resource resource = set.createResource(fileURI);

        for (EObject object : objects) {
//            if (object instanceof KNode) {
//                KimlUtil.persistDataElements((KNode) object);
//            }
            resource.getContents().add(object);
        }

        try {
            resource.save(Collections.emptyMap());
            // System.out.println("written "+ fileURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resource.unload();
    }

    /**
     * {@inheritDoc}
     */
    public boolean requiresDiagramSynthesisReRun(final ViewContext viewContext) {
        return true;
    }
}
