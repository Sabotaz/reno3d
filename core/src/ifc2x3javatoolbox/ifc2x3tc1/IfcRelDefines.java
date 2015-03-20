/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcRelDefines<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public abstract class IfcRelDefines extends IfcRelationship implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcGloballyUniqueId","IfcOwnerHistory","IfcLabel","IfcText","SET<IfcObject>"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** RelatedObjects is an DEMANDED attribute - may not be null**/
	protected SET<IfcObject> RelatedObjects;
	/**
	* The default constructor.
	**/
	public IfcRelDefines(){}

	/**
	* Constructs a new IfcRelDefines object using the given parameters.
	*
	* @param GlobalId DEMANDED parameter of type IfcGloballyUniqueId - may not be null.
	* @param OwnerHistory DEMANDED parameter of type IfcOwnerHistory - may not be null.
	* @param Name OPTIONAL parameter of type IfcLabel
	* @param Description OPTIONAL parameter of type IfcText
	* @param RelatedObjects DEMANDED parameter of type SET<IfcObject> - may not be null.
	**/
	public IfcRelDefines(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, SET<IfcObject> RelatedObjects)
	{
		this.GlobalId = GlobalId;
		this.OwnerHistory = OwnerHistory;
		this.Name = Name;
		this.Description = Description;
		this.RelatedObjects = RelatedObjects;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcRelDefines object using the given parameters.
	*
	* @param GlobalId DEMANDED parameter of type IfcGloballyUniqueId - may not be null.
	* @param OwnerHistory DEMANDED parameter of type IfcOwnerHistory - may not be null.
	* @param Name OPTIONAL parameter of type IfcLabel
	* @param Description OPTIONAL parameter of type IfcText
	* @param RelatedObjects DEMANDED parameter of type SET<IfcObject> - may not be null.
	**/
	public void setParameters(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, SET<IfcObject> RelatedObjects)
	{
		this.GlobalId = GlobalId;
		this.OwnerHistory = OwnerHistory;
		this.Name = Name;
		this.Description = Description;
		this.RelatedObjects = RelatedObjects;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	@SuppressWarnings("unchecked")
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.GlobalId = (IfcGloballyUniqueId) parameters.get(0);
		this.OwnerHistory = (IfcOwnerHistory) parameters.get(1);
		this.Name = (IfcLabel) parameters.get(2);
		this.Description = (IfcText) parameters.get(3);
		this.RelatedObjects = (SET<IfcObject>) parameters.get(4);
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
		if(RelatedObjects != null)
		{
			for(IfcObject RelatedObjects$ : RelatedObjects)
			{
				if(RelatedObjects$.IsDefinedBy_Inverse == null)
				{
					RelatedObjects$.IsDefinedBy_Inverse = new SET<IfcRelDefines>();
				}
				RelatedObjects$.IsDefinedBy_Inverse.add(this);
			}
		}
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	String[] getNonInverseAttributeTypes()
	{
		return IfcRelDefines.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCRELDEFINES(");
		if(getRedefinedDerivedAttributeTypes().contains("GlobalId")) stepString = stepString.concat("*,");
		else{
		if(this.GlobalId != null)		stepString = stepString.concat(((RootInterface)this.GlobalId).getStepParameter(IfcGloballyUniqueId.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("OwnerHistory")) stepString = stepString.concat("*,");
		else{
		if(this.OwnerHistory != null)		stepString = stepString.concat(((RootInterface)this.OwnerHistory).getStepParameter(IfcOwnerHistory.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,");
		else{
		if(this.Name != null)		stepString = stepString.concat(((RootInterface)this.Name).getStepParameter(IfcLabel.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Description")) stepString = stepString.concat("*,");
		else{
		if(this.Description != null)		stepString = stepString.concat(((RootInterface)this.Description).getStepParameter(IfcText.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("RelatedObjects")) stepString = stepString.concat("*);");
		else{
		if(this.RelatedObjects != null)		stepString = stepString.concat(((RootInterface)this.RelatedObjects).getStepParameter(IfcObject.class.isInterface())+");");
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
	* This method sets the RelatedObjects attribute to the given value.
	*
	* @param RelatedObjects OPTIONAL value to set
	**/
	public void setRelatedObjects(SET<IfcObject> RelatedObjects)
	{
		synchronizeInversesRemoveRelatedObjects(this.RelatedObjects);
		this.RelatedObjects = RelatedObjects;
		synchronizeInversesAddRelatedObjects(this.RelatedObjects);
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the set of the RelatedObjects attribute.
	*
	* @return a copy of the RelatedObjects set
	**/
	public SET<IfcObject> getRelatedObjects()
	{
		if(this.RelatedObjects != null)
			return new SET<IfcObject>(this.RelatedObjects);
		return null;
	}

	/**
	* This method adds an IfcObject object to the RelatedObjects set.
	* @param RelatedObjects element to be appended to this set.
	**/
	public void addRelatedObjects(IfcObject RelatedObjects)
	{
		if(this.RelatedObjects == null)
			this.RelatedObjects = new SET<IfcObject>();
		this.RelatedObjects.add(RelatedObjects);
		synchronizeInversesAddRelatedObjects(RelatedObjects);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of IfcObject objects to the RelatedObjects set.
	* @param RelatedObjects collection containing elements to be added to this set.
	**/
	public void addAllRelatedObjects(java.util.Collection<IfcObject> RelatedObjects)
	{
		if(this.RelatedObjects == null)
			this.RelatedObjects = new SET<IfcObject>();
		this.RelatedObjects.addAll(RelatedObjects);
		synchronizeInversesAddRelatedObjects(RelatedObjects);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the RelatedObjects set.
	**/
	public void clearRelatedObjects()
	{
		if(this.RelatedObjects != null)
		{
			synchronizeInversesRemoveRelatedObjects(this.RelatedObjects);
			this.RelatedObjects.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an IfcObject object from the RelatedObjects set.
	* @param RelatedObjects element to be removed from this set.
	**/
	public void removeRelatedObjects(IfcObject RelatedObjects)
	{
		if(this.RelatedObjects != null)
		{
			this.RelatedObjects.remove(RelatedObjects);
			synchronizeInversesRemoveRelatedObjects(RelatedObjects);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of IfcObject objects from the RelatedObjects set.
	* @param RelatedObjects collection containing elements to be removed from this set.
	**/
	public void removeAllRelatedObjects(java.util.Collection<IfcObject> RelatedObjects)
	{
		if(this.RelatedObjects != null)
		{
			this.RelatedObjects.removeAll(RelatedObjects);
			synchronizeInversesRemoveRelatedObjects(RelatedObjects);
			fireChangeEvent();
		}
	}

	private void synchronizeInversesAddRelatedObjects(IfcObject RelatedObjects)
	{
		if(RelatedObjects != null)
		{
				if(RelatedObjects.IsDefinedBy_Inverse == null)
				{
					RelatedObjects.IsDefinedBy_Inverse = new SET<IfcRelDefines>();
				}
				RelatedObjects.IsDefinedBy_Inverse.add(this);
		}
	}

	private void synchronizeInversesAddRelatedObjects(java.util.Collection<IfcObject> RelatedObjects)
	{
		if(RelatedObjects != null)
		{
			for(IfcObject RelatedObjects$ : RelatedObjects)
			{
				synchronizeInversesAddRelatedObjects(RelatedObjects$);
			}
		}
	}

	private void synchronizeInversesRemoveRelatedObjects(IfcObject RelatedObjects)
	{
		if(RelatedObjects != null)
		{
				if(RelatedObjects.IsDefinedBy_Inverse != null)
				{
					RelatedObjects.IsDefinedBy_Inverse.remove(this);
				}
		}
	}

	private void synchronizeInversesRemoveRelatedObjects(java.util.Collection<IfcObject> RelatedObjects)
	{
		if(RelatedObjects != null)
		{
			for(IfcObject RelatedObjects$ : RelatedObjects)
			{
				synchronizeInversesRemoveRelatedObjects(RelatedObjects$);
			}
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
	public abstract Object clone();

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public abstract Object shallowCopy();

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
