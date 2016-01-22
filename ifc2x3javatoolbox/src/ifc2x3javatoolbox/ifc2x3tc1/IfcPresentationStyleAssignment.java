/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcPresentationStyleAssignment<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcPresentationStyleAssignment extends InternalAccessClass implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"SET<IfcPresentationStyleSelect>"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** Styles is an DEMANDED attribute - may not be null**/
	protected SET<IfcPresentationStyleSelect> Styles;
	/**
	* The default constructor.
	**/
	public IfcPresentationStyleAssignment(){}

	/**
	* Constructs a new IfcPresentationStyleAssignment object using the given parameters.
	*
	* @param Styles DEMANDED parameter of type SET<IfcPresentationStyleSelect> - may not be null.
	**/
	public IfcPresentationStyleAssignment(SET<IfcPresentationStyleSelect> Styles)
	{
		this.Styles = Styles;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcPresentationStyleAssignment object using the given parameters.
	*
	* @param Styles DEMANDED parameter of type SET<IfcPresentationStyleSelect> - may not be null.
	**/
	public void setParameters(SET<IfcPresentationStyleSelect> Styles)
	{
		this.Styles = Styles;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	@SuppressWarnings("unchecked")
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.Styles = (SET<IfcPresentationStyleSelect>) parameters.get(0);
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
		return IfcPresentationStyleAssignment.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCPRESENTATIONSTYLEASSIGNMENT(");
		if(getRedefinedDerivedAttributeTypes().contains("Styles")) stepString = stepString.concat("*);");
		else{
		if(this.Styles != null)		stepString = stepString.concat(((RootInterface)this.Styles).getStepParameter(IfcPresentationStyleSelect.class.isInterface())+");");
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
	* This method sets the Styles attribute to the given value.
	*
	* @param Styles OPTIONAL value to set
	**/
	public void setStyles(SET<IfcPresentationStyleSelect> Styles)
	{
		this.Styles = Styles;
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the set of the Styles attribute.
	*
	* @return a copy of the Styles set
	**/
	public SET<IfcPresentationStyleSelect> getStyles()
	{
		if(this.Styles != null)
			return new SET<IfcPresentationStyleSelect>(this.Styles);
		return null;
	}

	/**
	* This method adds an IfcPresentationStyleSelect object to the Styles set.
	* @param Styles element to be appended to this set.
	**/
	public void addStyles(IfcPresentationStyleSelect Styles)
	{
		if(this.Styles == null)
			this.Styles = new SET<IfcPresentationStyleSelect>();
		this.Styles.add(Styles);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of IfcPresentationStyleSelect objects to the Styles set.
	* @param Styles collection containing elements to be added to this set.
	**/
	public void addAllStyles(java.util.Collection<IfcPresentationStyleSelect> Styles)
	{
		if(this.Styles == null)
			this.Styles = new SET<IfcPresentationStyleSelect>();
		this.Styles.addAll(Styles);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the Styles set.
	**/
	public void clearStyles()
	{
		if(this.Styles != null)
		{
			this.Styles.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an IfcPresentationStyleSelect object from the Styles set.
	* @param Styles element to be removed from this set.
	**/
	public void removeStyles(IfcPresentationStyleSelect Styles)
	{
		if(this.Styles != null)
		{
			this.Styles.remove(Styles);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of IfcPresentationStyleSelect objects from the Styles set.
	* @param Styles collection containing elements to be removed from this set.
	**/
	public void removeAllStyles(java.util.Collection<IfcPresentationStyleSelect> Styles)
	{
		if(this.Styles != null)
		{
			this.Styles.removeAll(Styles);
			fireChangeEvent();
		}
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
	@SuppressWarnings("unchecked")
	public Object clone()
	{
		IfcPresentationStyleAssignment ifcPresentationStyleAssignment = new IfcPresentationStyleAssignment();
		if(this.Styles != null)
			ifcPresentationStyleAssignment.setStyles((SET<IfcPresentationStyleSelect>)this.Styles.clone());
		return ifcPresentationStyleAssignment;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcPresentationStyleAssignment ifcPresentationStyleAssignment = new IfcPresentationStyleAssignment();
		if(this.Styles != null)
			ifcPresentationStyleAssignment.setStyles(this.Styles);
		return ifcPresentationStyleAssignment;
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
