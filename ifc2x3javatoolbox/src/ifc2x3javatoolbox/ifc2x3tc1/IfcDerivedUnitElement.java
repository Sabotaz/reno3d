/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcDerivedUnitElement<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcDerivedUnitElement extends InternalAccessClass implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcNamedUnit","INTEGER"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** Unit is an DEMANDED attribute - may not be null**/
	protected IfcNamedUnit Unit;
	/** Exponent is an DEMANDED attribute - may not be null**/
	protected INTEGER Exponent;
	/**
	* The default constructor.
	**/
	public IfcDerivedUnitElement(){}

	/**
	* Constructs a new IfcDerivedUnitElement object using the given parameters.
	*
	* @param Unit DEMANDED parameter of type IfcNamedUnit - may not be null.
	* @param Exponent DEMANDED parameter of type INTEGER - may not be null.
	**/
	public IfcDerivedUnitElement(IfcNamedUnit Unit, INTEGER Exponent)
	{
		this.Unit = Unit;
		this.Exponent = Exponent;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcDerivedUnitElement object using the given parameters.
	*
	* @param Unit DEMANDED parameter of type IfcNamedUnit - may not be null.
	* @param Exponent DEMANDED parameter of type INTEGER - may not be null.
	**/
	public void setParameters(IfcNamedUnit Unit, INTEGER Exponent)
	{
		this.Unit = Unit;
		this.Exponent = Exponent;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.Unit = (IfcNamedUnit) parameters.get(0);
		this.Exponent = (INTEGER) parameters.get(1);
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void destruct()
	{
		listenerList = null;
	}

	private void resolveInverses()
	{
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	String[] getNonInverseAttributeTypes()
	{
		return IfcDerivedUnitElement.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCDERIVEDUNITELEMENT(");
		if(getRedefinedDerivedAttributeTypes().contains("Unit")) stepString = stepString.concat("*,");
		else{
		if(this.Unit != null)		stepString = stepString.concat(((RootInterface)this.Unit).getStepParameter(IfcNamedUnit.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Exponent")) stepString = stepString.concat("*);");
		else{
		if(this.Exponent != null)		stepString = stepString.concat(((RootInterface)this.Exponent).getStepParameter(INTEGER.class.isInterface())+");");
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
	* This method sets the Unit attribute to the given value.
	*
	* @param Unit OPTIONAL value to set
	**/
	public void setUnit(IfcNamedUnit Unit)
	{
		this.Unit = Unit;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Unit attribute.
	*
	* @return the value of Unit
	/**/
	public IfcNamedUnit getUnit()
	{
		return this.Unit;
	}

	/**
	* This method sets the Exponent attribute to the given value.
	*
	* @param Exponent OPTIONAL value to set
	**/
	public void setExponent(INTEGER Exponent)
	{
		this.Exponent = Exponent;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Exponent attribute.
	*
	* @return the value of Exponent
	/**/
	public INTEGER getExponent()
	{
		return this.Exponent;
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
		IfcDerivedUnitElement ifcDerivedUnitElement = new IfcDerivedUnitElement();
		if(this.Unit != null)
			ifcDerivedUnitElement.setUnit((IfcNamedUnit)this.Unit.clone());
		if(this.Exponent != null)
			ifcDerivedUnitElement.setExponent((INTEGER)this.Exponent.clone());
		return ifcDerivedUnitElement;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcDerivedUnitElement ifcDerivedUnitElement = new IfcDerivedUnitElement();
		if(this.Unit != null)
			ifcDerivedUnitElement.setUnit(this.Unit);
		if(this.Exponent != null)
			ifcDerivedUnitElement.setExponent(this.Exponent);
		return ifcDerivedUnitElement;
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
