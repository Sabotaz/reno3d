/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcRevolvedAreaSolid<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcRevolvedAreaSolid extends IfcSweptAreaSolid implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcProfileDef","IfcAxis2Placement3D","IfcAxis1Placement","IfcPlaneAngleMeasure"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** Axis is an DEMANDED attribute - may not be null**/
	protected IfcAxis1Placement Axis;
	/** Angle is an DEMANDED attribute - may not be null**/
	protected IfcPlaneAngleMeasure Angle;
	/**
	* The default constructor.
	**/
	public IfcRevolvedAreaSolid(){}

	/**
	* Constructs a new IfcRevolvedAreaSolid object using the given parameters.
	*
	* @param SweptArea DEMANDED parameter of type IfcProfileDef - may not be null.
	* @param Position DEMANDED parameter of type IfcAxis2Placement3D - may not be null.
	* @param Axis DEMANDED parameter of type IfcAxis1Placement - may not be null.
	* @param Angle DEMANDED parameter of type IfcPlaneAngleMeasure - may not be null.
	**/
	public IfcRevolvedAreaSolid(IfcProfileDef SweptArea, IfcAxis2Placement3D Position, IfcAxis1Placement Axis, IfcPlaneAngleMeasure Angle)
	{
		this.SweptArea = SweptArea;
		this.Position = Position;
		this.Axis = Axis;
		this.Angle = Angle;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcRevolvedAreaSolid object using the given parameters.
	*
	* @param SweptArea DEMANDED parameter of type IfcProfileDef - may not be null.
	* @param Position DEMANDED parameter of type IfcAxis2Placement3D - may not be null.
	* @param Axis DEMANDED parameter of type IfcAxis1Placement - may not be null.
	* @param Angle DEMANDED parameter of type IfcPlaneAngleMeasure - may not be null.
	**/
	public void setParameters(IfcProfileDef SweptArea, IfcAxis2Placement3D Position, IfcAxis1Placement Axis, IfcPlaneAngleMeasure Angle)
	{
		this.SweptArea = SweptArea;
		this.Position = Position;
		this.Axis = Axis;
		this.Angle = Angle;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.SweptArea = (IfcProfileDef) parameters.get(0);
		this.Position = (IfcAxis2Placement3D) parameters.get(1);
		this.Axis = (IfcAxis1Placement) parameters.get(2);
		this.Angle = (IfcPlaneAngleMeasure) parameters.get(3);
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
		return IfcRevolvedAreaSolid.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCREVOLVEDAREASOLID(");
		if(getRedefinedDerivedAttributeTypes().contains("SweptArea")) stepString = stepString.concat("*,");
		else{
		if(this.SweptArea != null)		stepString = stepString.concat(((RootInterface)this.SweptArea).getStepParameter(IfcProfileDef.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Position")) stepString = stepString.concat("*,");
		else{
		if(this.Position != null)		stepString = stepString.concat(((RootInterface)this.Position).getStepParameter(IfcAxis2Placement3D.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Axis")) stepString = stepString.concat("*,");
		else{
		if(this.Axis != null)		stepString = stepString.concat(((RootInterface)this.Axis).getStepParameter(IfcAxis1Placement.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Angle")) stepString = stepString.concat("*);");
		else{
		if(this.Angle != null)		stepString = stepString.concat(((RootInterface)this.Angle).getStepParameter(IfcPlaneAngleMeasure.class.isInterface())+");");
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
	* This method sets the Axis attribute to the given value.
	*
	* @param Axis OPTIONAL value to set
	**/
	public void setAxis(IfcAxis1Placement Axis)
	{
		this.Axis = Axis;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Axis attribute.
	*
	* @return the value of Axis
	/**/
	public IfcAxis1Placement getAxis()
	{
		return this.Axis;
	}

	/**
	* This method sets the Angle attribute to the given value.
	*
	* @param Angle OPTIONAL value to set
	**/
	public void setAngle(IfcPlaneAngleMeasure Angle)
	{
		this.Angle = Angle;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Angle attribute.
	*
	* @return the value of Angle
	/**/
	public IfcPlaneAngleMeasure getAngle()
	{
		return this.Angle;
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
		IfcRevolvedAreaSolid ifcRevolvedAreaSolid = new IfcRevolvedAreaSolid();
		if(this.SweptArea != null)
			ifcRevolvedAreaSolid.setSweptArea((IfcProfileDef)this.SweptArea.clone());
		if(this.Position != null)
			ifcRevolvedAreaSolid.setPosition((IfcAxis2Placement3D)this.Position.clone());
		if(this.Axis != null)
			ifcRevolvedAreaSolid.setAxis((IfcAxis1Placement)this.Axis.clone());
		if(this.Angle != null)
			ifcRevolvedAreaSolid.setAngle((IfcPlaneAngleMeasure)this.Angle.clone());
		return ifcRevolvedAreaSolid;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcRevolvedAreaSolid ifcRevolvedAreaSolid = new IfcRevolvedAreaSolid();
		if(this.SweptArea != null)
			ifcRevolvedAreaSolid.setSweptArea(this.SweptArea);
		if(this.Position != null)
			ifcRevolvedAreaSolid.setPosition(this.Position);
		if(this.Axis != null)
			ifcRevolvedAreaSolid.setAxis(this.Axis);
		if(this.Angle != null)
			ifcRevolvedAreaSolid.setAngle(this.Angle);
		return ifcRevolvedAreaSolid;
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
