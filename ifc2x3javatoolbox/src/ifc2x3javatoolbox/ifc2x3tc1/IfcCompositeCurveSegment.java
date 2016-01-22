/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcCompositeCurveSegment<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcCompositeCurveSegment extends IfcGeometricRepresentationItem implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcTransitionCode","BOOLEAN","IfcCurve"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** Transition is an DEMANDED attribute - may not be null**/
	protected IfcTransitionCode Transition;
	/** SameSense is an DEMANDED attribute - may not be null**/
	protected BOOLEAN SameSense;
	/** ParentCurve is an DEMANDED attribute - may not be null**/
	protected IfcCurve ParentCurve;
	protected SET<IfcCompositeCurve> UsingCurves_Inverse;
	/**
	* The default constructor.
	**/
	public IfcCompositeCurveSegment(){}

	/**
	* Constructs a new IfcCompositeCurveSegment object using the given parameters.
	*
	* @param Transition DEMANDED parameter of type IfcTransitionCode - may not be null.
	* @param SameSense DEMANDED parameter of type BOOLEAN - may not be null.
	* @param ParentCurve DEMANDED parameter of type IfcCurve - may not be null.
	**/
	public IfcCompositeCurveSegment(IfcTransitionCode Transition, BOOLEAN SameSense, IfcCurve ParentCurve)
	{
		this.Transition = Transition;
		this.SameSense = SameSense;
		this.ParentCurve = ParentCurve;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcCompositeCurveSegment object using the given parameters.
	*
	* @param Transition DEMANDED parameter of type IfcTransitionCode - may not be null.
	* @param SameSense DEMANDED parameter of type BOOLEAN - may not be null.
	* @param ParentCurve DEMANDED parameter of type IfcCurve - may not be null.
	**/
	public void setParameters(IfcTransitionCode Transition, BOOLEAN SameSense, IfcCurve ParentCurve)
	{
		this.Transition = Transition;
		this.SameSense = SameSense;
		this.ParentCurve = ParentCurve;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.Transition = (IfcTransitionCode) parameters.get(0);
		this.SameSense = (BOOLEAN) parameters.get(1);
		this.ParentCurve = (IfcCurve) parameters.get(2);
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void destruct()
	{
		 super.destruct();
		listenerList = null;
		UsingCurves_Inverse = null;
	}

	private void resolveInverses()
	{
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	String[] getNonInverseAttributeTypes()
	{
		return IfcCompositeCurveSegment.nonInverseAttributes;	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	private java.util.HashSet<String> getRedefinedDerivedAttributeTypes()
	{
		java.util.HashSet<String> redefinedDerivedAttributes = new java.util.HashSet<String>();
		return redefinedDerivedAttributes;	}

	/**
 * This method returns the object IFC  STEP representation. This method is called by the IfcModel object to write IFC STEP files.
	 *
	 * @return the IFC STEP representation of this object
	**/
	public String getStepLine()
	{
		String stepString = new String("#"+this.stepLineNumber+"= ");
		stepString = stepString.concat("IFCCOMPOSITECURVESEGMENT(");
		if(getRedefinedDerivedAttributeTypes().contains("Transition")) stepString = stepString.concat("*,");
		else{
		if(this.Transition != null)		stepString = stepString.concat(((RootInterface)this.Transition).getStepParameter(IfcTransitionCode.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("SameSense")) stepString = stepString.concat("*,");
		else{
		if(this.SameSense != null)		stepString = stepString.concat(((RootInterface)this.SameSense).getStepParameter(BOOLEAN.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("ParentCurve")) stepString = stepString.concat("*);");
		else{
		if(this.ParentCurve != null)		stepString = stepString.concat(((RootInterface)this.ParentCurve).getStepParameter(IfcCurve.class.isInterface())+");");
		else		stepString = stepString.concat("$);");
		}
		return stepString;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	public String getStepParameter(boolean isSelectType)
	{
		return "#" + this.stepLineNumber;
	}

	/**
	 * This method returns the line number within a IFC  STEP representation. This method is called from other objects, where this one is referenced.
	 *
	 * @return the STEP line number
	**/
	public int getStepLineNumber()
	{
		return this.stepLineNumber;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void setStepLineNumber(int number)
	{
		this.stepLineNumber = number;
	}

	/**
	* This method returns a copy of the set of the UsingCurves_Inverse attribute.
	*
	* @return a copy of the UsingCurves_Inverse set
	**/
	public SET<IfcCompositeCurve> getUsingCurves_Inverse()
	{
		if(this.UsingCurves_Inverse != null)
			return new SET<IfcCompositeCurve>(this.UsingCurves_Inverse);
		return null;
	}

	/**
	* This method sets the Transition attribute to the given value.
	*
	* @param Transition OPTIONAL value to set
	**/
	public void setTransition(IfcTransitionCode Transition)
	{
		this.Transition = Transition;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Transition attribute.
	*
	* @return the value of Transition
	/**/
	public IfcTransitionCode getTransition()
	{
		return this.Transition;
	}

	/**
	* This method sets the SameSense attribute to the given value.
	*
	* @param SameSense OPTIONAL value to set
	**/
	public void setSameSense(BOOLEAN SameSense)
	{
		this.SameSense = SameSense;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the SameSense attribute.
	*
	* @return the value of SameSense
	/**/
	public BOOLEAN getSameSense()
	{
		return this.SameSense;
	}

	/**
	* This method sets the ParentCurve attribute to the given value.
	*
	* @param ParentCurve OPTIONAL value to set
	**/
	public void setParentCurve(IfcCurve ParentCurve)
	{
		this.ParentCurve = ParentCurve;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the ParentCurve attribute.
	*
	* @return the value of ParentCurve
	/**/
	public IfcCurve getParentCurve()
	{
		return this.ParentCurve;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void setStepParameter(java.util.ArrayList<CloneableObject> parameter)
	{
		this.stepParameter = parameter;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	java.util.ArrayList<CloneableObject> getStepParameter()
	{
		return this.stepParameter;
	}

	/**
	 * This method registers an ObjectChangeListener to this object. An event is fired whenever one of its values was changed.
	 * 
	 *@param listener the listener to register
	**/
	public void addObjectChangeListener(ObjectChangeListener listener)
	{
		if (listenerList == null)	listenerList = new java.util.HashSet<ObjectChangeListener>(1,1);
		listenerList.add(listener);
	}

	/**
	 * This method unregisters an ObjectChangeListener from this object.
	 * 
	 *@param listener the listener to unregister
	**/
	public void removeObjectChangeListener(ObjectChangeListener listener)
	{
		if (listenerList == null)	return;
		listenerList.remove(listener);
		if (listenerList.size()==0) listenerList = null;
	}

	/**
	 * This method removes all currently registered ObjectChangeListeners from this object.
	**/
	public void removeAllObjectChangeListeners()
	{
		listenerList = null;
	}

	protected void fireChangeEvent()
	{
		if(listenerList == null) return;
		for(ObjectChangeListener listener : listenerList)
			listener.ifcModelObjectChange(this);
	}

	/**
	 * This method clones the object (deep cloning).
	 *
	 * @return the cloned object
	**/
	public Object clone()
	{
		IfcCompositeCurveSegment ifcCompositeCurveSegment = new IfcCompositeCurveSegment();
		if(this.Transition != null)
			ifcCompositeCurveSegment.setTransition((IfcTransitionCode)this.Transition.clone());
		if(this.SameSense != null)
			ifcCompositeCurveSegment.setSameSense((BOOLEAN)this.SameSense.clone());
		if(this.ParentCurve != null)
			ifcCompositeCurveSegment.setParentCurve((IfcCurve)this.ParentCurve.clone());
		return ifcCompositeCurveSegment;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcCompositeCurveSegment ifcCompositeCurveSegment = new IfcCompositeCurveSegment();
		if(this.Transition != null)
			ifcCompositeCurveSegment.setTransition(this.Transition);
		if(this.SameSense != null)
			ifcCompositeCurveSegment.setSameSense(this.SameSense);
		if(this.ParentCurve != null)
			ifcCompositeCurveSegment.setParentCurve(this.ParentCurve);
		return ifcCompositeCurveSegment;
	}

	/**
	* This method returns the objects standard description.
	*
	* @return the standard description
	**/
	public String toString()
	{
		return "#"+ this.getStepLineNumber() + " " + this.getClass().getSimpleName();
	}


}
