/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcTwoDirectionRepeatFactor<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcTwoDirectionRepeatFactor extends IfcOneDirectionRepeatFactor implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcVector","IfcVector"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** SecondRepeatFactor is an DEMANDED attribute - may not be null**/
	protected IfcVector SecondRepeatFactor;
	/**
	* The default constructor.
	**/
	public IfcTwoDirectionRepeatFactor(){}

	/**
	* Constructs a new IfcTwoDirectionRepeatFactor object using the given parameters.
	*
	* @param RepeatFactor DEMANDED parameter of type IfcVector - may not be null.
	* @param SecondRepeatFactor DEMANDED parameter of type IfcVector - may not be null.
	**/
	public IfcTwoDirectionRepeatFactor(IfcVector RepeatFactor, IfcVector SecondRepeatFactor)
	{
		this.RepeatFactor = RepeatFactor;
		this.SecondRepeatFactor = SecondRepeatFactor;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcTwoDirectionRepeatFactor object using the given parameters.
	*
	* @param RepeatFactor DEMANDED parameter of type IfcVector - may not be null.
	* @param SecondRepeatFactor DEMANDED parameter of type IfcVector - may not be null.
	**/
	public void setParameters(IfcVector RepeatFactor, IfcVector SecondRepeatFactor)
	{
		this.RepeatFactor = RepeatFactor;
		this.SecondRepeatFactor = SecondRepeatFactor;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.RepeatFactor = (IfcVector) parameters.get(0);
		this.SecondRepeatFactor = (IfcVector) parameters.get(1);
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void destruct()
	{
		 super.destruct();
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
		return IfcTwoDirectionRepeatFactor.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCTWODIRECTIONREPEATFACTOR(");
		if(getRedefinedDerivedAttributeTypes().contains("RepeatFactor")) stepString = stepString.concat("*,");
		else{
		if(this.RepeatFactor != null)		stepString = stepString.concat(((RootInterface)this.RepeatFactor).getStepParameter(IfcVector.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("SecondRepeatFactor")) stepString = stepString.concat("*);");
		else{
		if(this.SecondRepeatFactor != null)		stepString = stepString.concat(((RootInterface)this.SecondRepeatFactor).getStepParameter(IfcVector.class.isInterface())+");");
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
	* This method sets the SecondRepeatFactor attribute to the given value.
	*
	* @param SecondRepeatFactor OPTIONAL value to set
	**/
	public void setSecondRepeatFactor(IfcVector SecondRepeatFactor)
	{
		this.SecondRepeatFactor = SecondRepeatFactor;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the SecondRepeatFactor attribute.
	*
	* @return the value of SecondRepeatFactor
	/**/
	public IfcVector getSecondRepeatFactor()
	{
		return this.SecondRepeatFactor;
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
		IfcTwoDirectionRepeatFactor ifcTwoDirectionRepeatFactor = new IfcTwoDirectionRepeatFactor();
		if(this.RepeatFactor != null)
			ifcTwoDirectionRepeatFactor.setRepeatFactor((IfcVector)this.RepeatFactor.clone());
		if(this.SecondRepeatFactor != null)
			ifcTwoDirectionRepeatFactor.setSecondRepeatFactor((IfcVector)this.SecondRepeatFactor.clone());
		return ifcTwoDirectionRepeatFactor;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcTwoDirectionRepeatFactor ifcTwoDirectionRepeatFactor = new IfcTwoDirectionRepeatFactor();
		if(this.RepeatFactor != null)
			ifcTwoDirectionRepeatFactor.setRepeatFactor(this.RepeatFactor);
		if(this.SecondRepeatFactor != null)
			ifcTwoDirectionRepeatFactor.setSecondRepeatFactor(this.SecondRepeatFactor);
		return ifcTwoDirectionRepeatFactor;
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
