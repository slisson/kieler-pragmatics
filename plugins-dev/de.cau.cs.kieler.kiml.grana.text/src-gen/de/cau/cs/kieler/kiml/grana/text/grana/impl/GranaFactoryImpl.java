/**
 */
package de.cau.cs.kieler.kiml.grana.text.grana.impl;

import de.cau.cs.kieler.kiml.grana.text.grana.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GranaFactoryImpl extends EFactoryImpl implements GranaFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static GranaFactory init()
  {
    try
    {
      GranaFactory theGranaFactory = (GranaFactory)EPackage.Registry.INSTANCE.getEFactory(GranaPackage.eNS_URI);
      if (theGranaFactory != null)
      {
        return theGranaFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new GranaFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GranaFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case GranaPackage.GRANA: return createGrana();
      case GranaPackage.JOB: return createJob();
      case GranaPackage.RESOURCE: return createResource();
      case GranaPackage.ANALYSIS: return createAnalysis();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Grana createGrana()
  {
    GranaImpl grana = new GranaImpl();
    return grana;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Job createJob()
  {
    JobImpl job = new JobImpl();
    return job;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Resource createResource()
  {
    ResourceImpl resource = new ResourceImpl();
    return resource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Analysis createAnalysis()
  {
    AnalysisImpl analysis = new AnalysisImpl();
    return analysis;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GranaPackage getGranaPackage()
  {
    return (GranaPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static GranaPackage getPackage()
  {
    return GranaPackage.eINSTANCE;
  }

} //GranaFactoryImpl
