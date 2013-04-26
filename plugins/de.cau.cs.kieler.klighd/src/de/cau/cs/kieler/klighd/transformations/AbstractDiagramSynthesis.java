/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2013 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.transformations;

import java.lang.reflect.Method;

import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.krendering.KRenderingFactory;
import de.cau.cs.kieler.klighd.TransformationContext;

/**
 * This is a specialized {@link AbstractTransformation} with target model type {@link KNode}.<br>
 * <br>
 * Its aim is to simplify and shrink the class declaration parts of diagram synthesis
 * transformations creating KGraph/KRendering specifications. Please see also the documentation of
 * {@link AbstractTransformation}.
 * 
 * @param <S>
 *            Type of the model to be visualized
 * 
 * @author chsch
 */
public abstract class AbstractDiagramSynthesis<S> extends AbstractTransformation<S, KNode> {
    
    /**
     * This constant expression is a convenience handle to easy the access to the
     * {@link KRenderingFactory} in derivatives of this class, i.e. concrete diagram syntheses.
     */
    protected static final KRenderingFactory RENDERING_FACTORY = KRenderingFactory.eINSTANCE; 

    /**
     * {@inheritDoc}
     */
    public KNode transform(final S model, final TransformationContext<S, KNode> transformationContext) {
        use(transformationContext);
        return transform(model);
    }
    
    /**
     * Translates the an model of type S into a KGraph/KRendering diagram description.
     * 
     * @param model
     *            the semantic model to be depicted
     * @return the related KGraph/KRendering diagram description
     */
    public abstract KNode transform(S model);

    /**
     * {@inheritDoc}
     */
    protected void inferSourceAndTargetModelClass() {
        this.setTriedToInferClass();
        // try to find a method with one parameter which returns KNode
        // takes the first matching method with parameter 0 != Object
        Method transformMethod = null;
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 1
                    && method.getReturnType().equals(KNode.class)
                    && method.getName().equals(TRANSFORM_METHOD_NAME)) {
                transformMethod = method;
                // keep searching if the parameter is of type Object
                // this is necessary to skip the method with type Object that is always present when
                // dealing with generic typed methods
                if (!method.getParameterTypes()[0].equals(new Object().getClass())) {
                    break;
                }
            }
        }
        // infer the types if a matching method has been found
        if (transformMethod != null) {
            this.setSourceClass(transformMethod.getParameterTypes()[0]);
            this.setTargetClass(KNode.class);
        }
    }
}