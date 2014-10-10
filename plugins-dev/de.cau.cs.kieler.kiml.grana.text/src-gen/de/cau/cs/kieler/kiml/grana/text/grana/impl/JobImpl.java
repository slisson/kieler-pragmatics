/**
 */
package de.cau.cs.kieler.kiml.grana.text.grana.impl;

import de.cau.cs.kieler.kiml.grana.text.grana.Analysis;
import de.cau.cs.kieler.kiml.grana.text.grana.GranaPackage;
import de.cau.cs.kieler.kiml.grana.text.grana.Job;
import de.cau.cs.kieler.kiml.grana.text.grana.Resource;

import de.cau.cs.kieler.kiml.klayoutdata.KIdentifier;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Job</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#isLayoutBeforeAnalysis <em>Layout Before Analysis</em>}</li>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#getLayoutOptions <em>Layout Options</em>}</li>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#getAnalyses <em>Analyses</em>}</li>
 *   <li>{@link de.cau.cs.kieler.kiml.grana.text.grana.impl.JobImpl#getOutput <em>Output</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JobImpl extends MinimalEObjectImpl.Container implements Job
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #isLayoutBeforeAnalysis() <em>Layout Before Analysis</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLayoutBeforeAnalysis()
   * @generated
   * @ordered
   */
  protected static final boolean LAYOUT_BEFORE_ANALYSIS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isLayoutBeforeAnalysis() <em>Layout Before Analysis</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLayoutBeforeAnalysis()
   * @generated
   * @ordered
   */
  protected boolean layoutBeforeAnalysis = LAYOUT_BEFORE_ANALYSIS_EDEFAULT;

  /**
   * The cached value of the '{@link #getResources() <em>Resources</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResources()
   * @generated
   * @ordered
   */
  protected EList<Resource> resources;

  /**
   * The cached value of the '{@link #getLayoutOptions() <em>Layout Options</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLayoutOptions()
   * @generated
   * @ordered
   */
  protected EList<KIdentifier> layoutOptions;

  /**
   * The cached value of the '{@link #getAnalyses() <em>Analyses</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAnalyses()
   * @generated
   * @ordered
   */
  protected EList<Analysis> analyses;

  /**
   * The default value of the '{@link #getOutput() <em>Output</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutput()
   * @generated
   * @ordered
   */
  protected static final String OUTPUT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOutput() <em>Output</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutput()
   * @generated
   * @ordered
   */
  protected String output = OUTPUT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JobImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return GranaPackage.Literals.JOB;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GranaPackage.JOB__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isLayoutBeforeAnalysis()
  {
    return layoutBeforeAnalysis;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLayoutBeforeAnalysis(boolean newLayoutBeforeAnalysis)
  {
    boolean oldLayoutBeforeAnalysis = layoutBeforeAnalysis;
    layoutBeforeAnalysis = newLayoutBeforeAnalysis;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GranaPackage.JOB__LAYOUT_BEFORE_ANALYSIS, oldLayoutBeforeAnalysis, layoutBeforeAnalysis));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Resource> getResources()
  {
    if (resources == null)
    {
      resources = new EObjectContainmentEList<Resource>(Resource.class, this, GranaPackage.JOB__RESOURCES);
    }
    return resources;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<KIdentifier> getLayoutOptions()
  {
    if (layoutOptions == null)
    {
      layoutOptions = new EObjectContainmentEList<KIdentifier>(KIdentifier.class, this, GranaPackage.JOB__LAYOUT_OPTIONS);
    }
    return layoutOptions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Analysis> getAnalyses()
  {
    if (analyses == null)
    {
      analyses = new EObjectContainmentEList<Analysis>(Analysis.class, this, GranaPackage.JOB__ANALYSES);
    }
    return analyses;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOutput()
  {
    return output;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOutput(String newOutput)
  {
    String oldOutput = output;
    output = newOutput;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GranaPackage.JOB__OUTPUT, oldOutput, output));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case GranaPackage.JOB__RESOURCES:
        return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
      case GranaPackage.JOB__LAYOUT_OPTIONS:
        return ((InternalEList<?>)getLayoutOptions()).basicRemove(otherEnd, msgs);
      case GranaPackage.JOB__ANALYSES:
        return ((InternalEList<?>)getAnalyses()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case GranaPackage.JOB__NAME:
        return getName();
      case GranaPackage.JOB__LAYOUT_BEFORE_ANALYSIS:
        return isLayoutBeforeAnalysis();
      case GranaPackage.JOB__RESOURCES:
        return getResources();
      case GranaPackage.JOB__LAYOUT_OPTIONS:
        return getLayoutOptions();
      case GranaPackage.JOB__ANALYSES:
        return getAnalyses();
      case GranaPackage.JOB__OUTPUT:
        return getOutput();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case GranaPackage.JOB__NAME:
        setName((String)newValue);
        return;
      case GranaPackage.JOB__LAYOUT_BEFORE_ANALYSIS:
        setLayoutBeforeAnalysis((Boolean)newValue);
        return;
      case GranaPackage.JOB__RESOURCES:
        getResources().clear();
        getResources().addAll((Collection<? extends Resource>)newValue);
        return;
      case GranaPackage.JOB__LAYOUT_OPTIONS:
        getLayoutOptions().clear();
        getLayoutOptions().addAll((Collection<? extends KIdentifier>)newValue);
        return;
      case GranaPackage.JOB__ANALYSES:
        getAnalyses().clear();
        getAnalyses().addAll((Collection<? extends Analysis>)newValue);
        return;
      case GranaPackage.JOB__OUTPUT:
        setOutput((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case GranaPackage.JOB__NAME:
        setName(NAME_EDEFAULT);
        return;
      case GranaPackage.JOB__LAYOUT_BEFORE_ANALYSIS:
        setLayoutBeforeAnalysis(LAYOUT_BEFORE_ANALYSIS_EDEFAULT);
        return;
      case GranaPackage.JOB__RESOURCES:
        getResources().clear();
        return;
      case GranaPackage.JOB__LAYOUT_OPTIONS:
        getLayoutOptions().clear();
        return;
      case GranaPackage.JOB__ANALYSES:
        getAnalyses().clear();
        return;
      case GranaPackage.JOB__OUTPUT:
        setOutput(OUTPUT_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case GranaPackage.JOB__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case GranaPackage.JOB__LAYOUT_BEFORE_ANALYSIS:
        return layoutBeforeAnalysis != LAYOUT_BEFORE_ANALYSIS_EDEFAULT;
      case GranaPackage.JOB__RESOURCES:
        return resources != null && !resources.isEmpty();
      case GranaPackage.JOB__LAYOUT_OPTIONS:
        return layoutOptions != null && !layoutOptions.isEmpty();
      case GranaPackage.JOB__ANALYSES:
        return analyses != null && !analyses.isEmpty();
      case GranaPackage.JOB__OUTPUT:
        return OUTPUT_EDEFAULT == null ? output != null : !OUTPUT_EDEFAULT.equals(output);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(", layoutBeforeAnalysis: ");
    result.append(layoutBeforeAnalysis);
    result.append(", output: ");
    result.append(output);
    result.append(')');
    return result.toString();
  }

} //JobImpl