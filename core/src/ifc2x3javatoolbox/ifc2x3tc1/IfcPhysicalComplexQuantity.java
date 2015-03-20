/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcPhysicalComplexQuantity<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcPhysicalComplexQuantity extends IfcPhysicalQuantity implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"IfcLabel","IfcText","SET<IfcPhysicalQuantity>","IfcLabel","IfcLabel","IfcLabel"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** HasQuantities is an DEMANDED attribute - may not be null**/
	protected SET<IfcPhysicalQuantity> HasQuantities;
	/** Discrimination is an DEMANDED attribute - may not be null**/
	protected IfcLabel Discrimination;
	/** Quality is an OPTIONAL attribute**/
	protected IfcLabel Quality;
	/** Usage is an OPTIONAL attribute**/
	protected IfcLabel Usage;
	/**
	* The default constructor.
	**/
	public IfcPhysicalComplexQuantity(){}

	/**
	* Constructs a new IfcPhysicalComplexQuantity object using the given parameters.
	*
	* @param Name DEMANDED parameter of type IfcLabel - may not be null.
	* @param Description OPTIONAL parameter of type IfcText
	* @param HasQuantities DEMANDED parameter of type SET<IfcPhysicalQuantity> - may not be null.
	* @param Discrimination DEMANDED parameter of type IfcLabel - may not be null.
	* @param Quality OPTIONAL parameter of type IfcLabel
	* @param Usage OPTIONAL parameter of type IfcLabel
	**/
	public IfcPhysicalComplexQuantity(IfcLabel Name, IfcText Description, SET<IfcPhysicalQuantity> HasQuantities, IfcLabel Discrimination, IfcLabel Quality, IfcLabel Usage)
	{
		this.Name = Name;
		this.Description = Description;
		this.HasQuantities = HasQuantities;
		this.Discrimination = Discrimination;
		this.Quality = Quality;
		this.Usage = Usage;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcPhysicalComplexQuantity object using the given parameters.
	*
	* @param Name DEMANDED parameter of type IfcLabel - may not be null.
	* @param Description OPTIONAL parameter of type IfcText
	* @param HasQuantities DEMANDED parameter of type SET<IfcPhysicalQuantity> - may not be null.
	* @param Discrimination DEMANDED parameter of type IfcLabel - may not be null.
	* @param Quality OPTIONAL parameter of type IfcLabel
	* @param Usage OPTIONAL parameter of type IfcLabel
	**/
	public void setParameters(IfcLabel Name, IfcText Description, SET<IfcPhysicalQuantity> HasQuantities, IfcLabel Discrimination, IfcLabel Quality, IfcLabel Usage)
	{
		this.Name = Name;
		this.Description = Description;
		this.HasQuantities = HasQuantities;
		this.Discrimination = Discrimination;
		this.Quality = Quality;
		this.Usage = Usage;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	@SuppressWarnings("unchecked")
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.Name = (IfcLabel) parameters.get(0);
		this.Description = (IfcText) parameters.get(1);
		this.HasQuantities = (SET<IfcPhysicalQuantity>) parameters.get(2);
		this.Discrimination = (IfcLabel) parameters.get(3);
		this.Quality = (IfcLabel) parameters.get(4);
		this.Usage = (IfcLabel) parameters.get(5);
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
		if(HasQuantities != null)
		{
			for(IfcPhysicalQuantity HasQuantities$ : HasQuantities)
			{
				if(HasQuantities$.PartOfComplex_Inverse == null)
				{
					HasQuantities$.PartOfComplex_Inverse = new SET<IfcPhysicalComplexQuantity>();
				}
				HasQuantities$.PartOfComplex_Inverse.add(this);
			}
		}
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	String[] getNonInverseAttributeTypes()
	{
		return IfcPhysicalComplexQuantity.nonInverseAttributes;	}

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
		stepString = stepString.concat("IFCPHYSICALCOMPLEXQUANTITY(");
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
		if(getRedefinedDerivedAttributeTypes().contains("HasQuantities")) stepString = stepString.concat("*,");
		else{
		if(this.HasQuantities != null)		stepString = stepString.concat(((RootInterface)this.HasQuantities).getStepParameter(IfcPhysicalQuantity.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Discrimination")) stepString = stepString.concat("*,");
		else{
		if(this.Discrimination != null)		stepString = stepString.concat(((RootInterface)this.Discrimination).getStepParameter(IfcLabel.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Quality")) stepString = stepString.concat("*,");
		else{
		if(this.Quality != null)		stepString = stepString.concat(((RootInterface)this.Quality).getStepParameter(IfcLabel.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("Usage")) stepString = stepString.concat("*);");
		else{
		if(this.Usage != null)		stepString = stepString.concat(((RootInterface)this.Usage).getStepParameter(IfcLabel.class.isInterface())+");");
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
	* This method sets the HasQuantities attribute to the given value.
	*
	* @param HasQuantities OPTIONAL value to set
	**/
	public void setHasQuantities(SET<IfcPhysicalQuantity> HasQuantities)
	{
		synchronizeInversesRemoveHasQuantities(this.HasQuantities);
		this.HasQuantities = HasQuantities;
		synchronizeInversesAddHasQuantities(this.HasQuantities);
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the set of the HasQuantities attribute.
	*
	* @return a copy of the HasQuantities set
	**/
	public SET<IfcPhysicalQuantity> getHasQuantities()
	{
		if(this.HasQuantities != null)
			return new SET<IfcPhysicalQuantity>(this.HasQuantities);
		return null;
	}

	/**
	* This method adds an IfcPhysicalQuantity object to the HasQuantities set.
	* @param HasQuantities element to be appended to this set.
	**/
	public void addHasQuantities(IfcPhysicalQuantity HasQuantities)
	{
		if(this.HasQuantities == null)
			this.HasQuantities = new SET<IfcPhysicalQuantity>();
		this.HasQuantities.add(HasQuantities);
		synchronizeInversesAddHasQuantities(HasQuantities);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of IfcPhysicalQuantity objects to the HasQuantities set.
	* @param HasQuantities collection containing elements to be added to this set.
	**/
	public void addAllHasQuantities(java.util.Collection<IfcPhysicalQuantity> HasQuantities)
	{
		if(this.HasQuantities == null)
			this.HasQuantities = new SET<IfcPhysicalQuantity>();
		this.HasQuantities.addAll(HasQuantities);
		synchronizeInversesAddHasQuantities(HasQuantities);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the HasQuantities set.
	**/
	public void clearHasQuantities()
	{
		if(this.HasQuantities != null)
		{
			synchronizeInversesRemoveHasQuantities(this.HasQuantities);
			this.HasQuantities.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an IfcPhysicalQuantity object from the HasQuantities set.
	* @param HasQuantities element to be removed from this set.
	**/
	public void removeHasQuantities(IfcPhysicalQuantity HasQuantities)
	{
		if(this.HasQuantities != null)
		{
			this.HasQuantities.remove(HasQuantities);
			synchronizeInversesRemoveHasQuantities(HasQuantities);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of IfcPhysicalQuantity objects from the HasQuantities set.
	* @param HasQuantities collection containing elements to be removed from this set.
	**/
	public void removeAllHasQuantities(java.util.Collection<IfcPhysicalQuantity> HasQuantities)
	{
		if(this.HasQuantities != null)
		{
			this.HasQuantities.removeAll(HasQuantities);
			synchronizeInversesRemoveHasQuantities(HasQuantities);
			fireChangeEvent();
		}
	}

	private void synchronizeInversesAddHasQuantities(IfcPhysicalQuantity HasQuantities)
	{
		if(HasQuantities != null)
		{
				if(HasQuantities.PartOfComplex_Inverse == null)
				{
					HasQuantities.PartOfComplex_Inverse = new SET<IfcPhysicalComplexQuantity>();
				}
				HasQuantities.PartOfComplex_Inverse.add(this);
		}
	}

	private void synchronizeInversesAddHasQuantities(java.util.Collection<IfcPhysicalQuantity> HasQuantities)
	{
		if(HasQuantities != null)
		{
			for(IfcPhysicalQuantity HasQuantities$ : HasQuantities)
			{
				synchronizeInversesAddHasQuantities(HasQuantities$);
			}
		}
	}

	private void synchronizeInversesRemoveHasQuantities(IfcPhysicalQuantity HasQuantities)
	{
		if(HasQuantities != null)
		{
				if(HasQuantities.PartOfComplex_Inverse != null)
				{
					HasQuantities.PartOfComplex_Inverse.remove(this);
				}
		}
	}

	private void synchronizeInversesRemoveHasQuantities(java.util.Collection<IfcPhysicalQuantity> HasQuantities)
	{
		if(HasQuantities != null)
		{
			for(IfcPhysicalQuantity HasQuantities$ : HasQuantities)
			{
				synchronizeInversesRemoveHasQuantities(HasQuantities$);
			}
		}
	}

	/**
	* This method sets the Discrimination attribute to the given value.
	*
	* @param Discrimination OPTIONAL value to set
	**/
	public void setDiscrimination(IfcLabel Discrimination)
	{
		this.Discrimination = Discrimination;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Discrimination attribute.
	*
	* @return the value of Discrimination
	/**/
	public IfcLabel getDiscrimination()
	{
		return this.Discrimination;
	}

	/**
	* This method sets the Quality attribute to the given value.
	*
	* @param Quality DEMANDED value to set - may not be null
	**/
	public void setQuality(IfcLabel Quality)
	{
		this.Quality = Quality;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Quality attribute.
	*
	* @return the value of Quality
	/**/
	public IfcLabel getQuality()
	{
		return this.Quality;
	}

	/**
	* This method sets the Usage attribute to the given value.
	*
	* @param Usage DEMANDED value to set - may not be null
	**/
	public void setUsage(IfcLabel Usage)
	{
		this.Usage = Usage;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the Usage attribute.
	*
	* @return the value of Usage
	/**/
	public IfcLabel getUsage()
	{
		return this.Usage;
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
		IfcPhysicalComplexQuantity ifcPhysicalComplexQuantity = new IfcPhysicalComplexQuantity();
		if(this.Name != null)
			ifcPhysicalComplexQuantity.setName((IfcLabel)this.Name.clone());
		if(this.Description != null)
			ifcPhysicalComplexQuantity.setDescription((IfcText)this.Description.clone());
		if(this.HasQuantities != null)
			ifcPhysicalComplexQuantity.setHasQuantities((SET<IfcPhysicalQuantity>)this.HasQuantities.clone());
		if(this.Discrimination != null)
			ifcPhysicalComplexQuantity.setDiscrimination((IfcLabel)this.Discrimination.clone());
		if(this.Quality != null)
			ifcPhysicalComplexQuantity.setQuality((IfcLabel)this.Quality.clone());
		if(this.Usage != null)
			ifcPhysicalComplexQuantity.setUsage((IfcLabel)this.Usage.clone());
		return ifcPhysicalComplexQuantity;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcPhysicalComplexQuantity ifcPhysicalComplexQuantity = new IfcPhysicalComplexQuantity();
		if(this.Name != null)
			ifcPhysicalComplexQuantity.setName(this.Name);
		if(this.Description != null)
			ifcPhysicalComplexQuantity.setDescription(this.Description);
		if(this.HasQuantities != null)
			ifcPhysicalComplexQuantity.setHasQuantities(this.HasQuantities);
		if(this.Discrimination != null)
			ifcPhysicalComplexQuantity.setDiscrimination(this.Discrimination);
		if(this.Quality != null)
			ifcPhysicalComplexQuantity.setQuality(this.Quality);
		if(this.Usage != null)
			ifcPhysicalComplexQuantity.setUsage(this.Usage);
		return ifcPhysicalComplexQuantity;
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
