/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.graphdrawing.graphml.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.graphdrawing.graphml.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.graphdrawing.graphml.GraphMLPackage
 * @generated
 */
public class GraphMLAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static GraphMLPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphMLAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = GraphMLPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GraphMLSwitch<Adapter> modelSwitch =
        new GraphMLSwitch<Adapter>() {
            @Override
            public Adapter caseDataExtensionType(DataExtensionType object) {
                return createDataExtensionTypeAdapter();
            }
            @Override
            public Adapter caseDataType(DataType object) {
                return createDataTypeAdapter();
            }
            @Override
            public Adapter caseDefaultType(DefaultType object) {
                return createDefaultTypeAdapter();
            }
            @Override
            public Adapter caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            @Override
            public Adapter caseEdgeType(EdgeType object) {
                return createEdgeTypeAdapter();
            }
            @Override
            public Adapter caseEndpointType(EndpointType object) {
                return createEndpointTypeAdapter();
            }
            @Override
            public Adapter caseGraphmlType(GraphmlType object) {
                return createGraphmlTypeAdapter();
            }
            @Override
            public Adapter caseGraphType(GraphType object) {
                return createGraphTypeAdapter();
            }
            @Override
            public Adapter caseHyperedgeType(HyperedgeType object) {
                return createHyperedgeTypeAdapter();
            }
            @Override
            public Adapter caseKeyType(KeyType object) {
                return createKeyTypeAdapter();
            }
            @Override
            public Adapter caseLocatorType(LocatorType object) {
                return createLocatorTypeAdapter();
            }
            @Override
            public Adapter caseNodeType(NodeType object) {
                return createNodeTypeAdapter();
            }
            @Override
            public Adapter casePortType(PortType object) {
                return createPortTypeAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.DataExtensionType <em>Data Extension Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.DataExtensionType
     * @generated
     */
    public Adapter createDataExtensionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.DataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.DataType
     * @generated
     */
    public Adapter createDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.DefaultType <em>Default Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.DefaultType
     * @generated
     */
    public Adapter createDefaultTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.EdgeType <em>Edge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.EdgeType
     * @generated
     */
    public Adapter createEdgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.EndpointType <em>Endpoint Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.EndpointType
     * @generated
     */
    public Adapter createEndpointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.GraphmlType <em>Graphml Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.GraphmlType
     * @generated
     */
    public Adapter createGraphmlTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.GraphType <em>Graph Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.GraphType
     * @generated
     */
    public Adapter createGraphTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.HyperedgeType <em>Hyperedge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.HyperedgeType
     * @generated
     */
    public Adapter createHyperedgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.KeyType <em>Key Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.KeyType
     * @generated
     */
    public Adapter createKeyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.LocatorType <em>Locator Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.LocatorType
     * @generated
     */
    public Adapter createLocatorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.NodeType <em>Node Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.NodeType
     * @generated
     */
    public Adapter createNodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.graphdrawing.graphml.PortType <em>Port Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.graphdrawing.graphml.PortType
     * @generated
     */
    public Adapter createPortTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //GraphMLAdapterFactory
