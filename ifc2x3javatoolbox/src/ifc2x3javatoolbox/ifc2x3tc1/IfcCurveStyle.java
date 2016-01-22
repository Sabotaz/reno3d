/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcCurveStyle<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcCurveStyle extends IfcPresentationStyle implements IfcPresentationStyleSelect, ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcLabel","IfcCurveFontOrScaledCurveFontSelect","IfcSizeSelect","IfcColour"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** CurveFont is an OPTIONAL attribute**/
	protected IfcCurveFontOrScaledCurveFontSelect CurveFont;
	/** CurveWidth is an OPTIONAL attribute**/
	protected IfcSizeSelect CurveWidth;
	/** CurveColour is an OPTIONAL attribute**/
	protected IfcColour CurveColour;
	/**
	* The default constructor.
	**/
	public IfcCurveStyle(){}

	/**
	* Constructs a new IfcCurveStyle object using the given parameters.
	*
	* @param Name OPTIONAL parameter of type IfcLabel
	* @param CurveFont OPTIONAL parameter of type IfcCurveFontOrScaledCurveFontSelect
	* @param CurveWidth OPTIONAL parameter of type IfcSizeSelect
	* @param CurveColour OPTIONAL parameter of type IfcColour
	**/
	public IfcCurveStyle(IfcLabel Name, IfcCurveFontOrScaledCurveFontSelect CurveFont, IfcSizeSelect CurveWidth, IfcColour CurveColour)
	{
		this.Name = Name;
		this.CurveFont = CurveFont;
		this.CurveWidth = CurveWidth;
		this.CurveColour = CurveColour;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcCurveStyle object using the given parameters.
	*
	* @param Name OPTIONAL parameter of type IfcLabel
	* @param CurveFont OPTIONAL parameter of type IfcCurveFontOrScaledCurveFontSelect
	* @param CurveWidth OPTIONAL parameter of type IfcSizeSelect
	* @param CurveColour OPTIONAL parameter of type IfcColour
	**/
	public void setParameters(IfcLabel Name, IfcCurveFontOrScaledCurveFontSelect CurveFont, IfcSizeSelect CurveWidth, IfcColour CurveColour)
	{
		this.Name = Name;
		this.CurveFont = CurveFont;
		this.CurveWidth = CurveWidth;
		this.CurveColour = CurveColour;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.Name = (IfcLabel) parameters.get(0);
		this.CurveFont = (IfcCurveFontOrScaledCurveFontSelect) parameters.get(1);
		this.CurveWidth = (IfcSizeSelect) parameters.get(2);
		this.CurveColour = (IfcColour) parameters.get(3);
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
		return IfcCurveStyle.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCCURVESTYLE(");
		if(getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,");
		else{
		if(this.Name != null)		stepString = stepString.concat(((RootInterface)this.Name).getStepParameter(IfcLabel.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("CurveFont")) stepString = stepString.concat("*,");
		else{
		if(this.CurveFont != null)		stepString = stepString.concat(((RootInterface)this.CurveFont).getStepParameter(IfcCurveFontOrScaledCurveFontSelect.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("CurveWidth")) stepString = stepString.concat("*,");
		else{
		if(this.CurveWidth != null)		stepString = stepString.concat(((RootInterface)this.CurveWidth).getStepParameter(IfcSizeSelect.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("CurveColour")) stepString = stepString.concat("*);");
		else{
		if(this.CurveColour != null)		stepString = stepString.concat(((RootInterface)this.CurveColour).getStepParameter(IfcColour.class.isInterface())+");");
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
	* This method sets the CurveFont attribute to the given value.
	*
	* @param CurveFont DEMANDED value to set - may not be null
	**/
	public void setCurveFont(IfcCurveFontOrScaledCurveFontSelect CurveFont)
	{
		this.CurveFont = CurveFont;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the CurveFont attribute.
	*
	* @return the value of CurveFont
	/**/
	public IfcCurveFontOrScaledCurveFontSelect getCurveFont()
	{
		return this.CurveFont;
	}

	/**
	* This method sets the CurveWidth attribute to the given value.
	*
	* @param CurveWidth DEMANDED value to set - may not be null
	**/
	public void setCurveWidth(IfcSizeSelect CurveWidth)
	{
		this.CurveWidth = CurveWidth;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the CurveWidth attribute.
	*
	* @return the value of CurveWidth
	/**/
	public IfcSizeSelect getCurveWidth()
	{
		return this.CurveWidth;
	}

	/**
	* This method sets the CurveColour attribute to the given value.
	*
	* @param CurveColour DEMANDED value to set - may not be null
	**/
	public void setCurveColour(IfcColour CurveColour)
	{
		this.CurveColour = CurveColour;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the CurveColour attribute.
	*
	* @return the value of CurveColour
	/**/
	public IfcColour getCurveColour()
	{
		return this.CurveColour;
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
		IfcCurveStyle ifcCurveStyle = new IfcCurveStyle();
		if(this.Name != null)
			ifcCurveStyle.setName((IfcLabel)this.Name.clone());
		if(this.CurveFont != null)
			ifcCurveStyle.setCurveFont((IfcCurveFontOrScaledCurveFontSelect)this.CurveFont.clone());
		if(this.CurveWidth != null)
			ifcCurveStyle.setCurveWidth((IfcSizeSelect)this.CurveWidth.clone());
		if(this.CurveColour != null)
			ifcCurveStyle.setCurveColour((IfcColour)this.CurveColour.clone());
		return ifcCurveStyle;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcCurveStyle ifcCurveStyle = new IfcCurveStyle();
		if(this.Name != null)
			ifcCurveStyle.setName(this.Name);
		if(this.CurveFont != null)
			ifcCurveStyle.setCurveFont(this.CurveFont);
		if(this.CurveWidth != null)
			ifcCurveStyle.setCurveWidth(this.CurveWidth);
		if(this.CurveColour != null)
			ifcCurveStyle.setCurveColour(this.CurveColour);
		return ifcCurveStyle;
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
