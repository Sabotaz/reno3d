/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity File_Name<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class File_Name extends InternalAccessClass implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"STRING","TimeStampText","LIST<STRING>","LIST<STRING>","STRING","STRING","STRING"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** name is an DEMANDED attribute - may not be null**/
	protected STRING name;
	/** timeStamp is an DEMANDED attribute - may not be null**/
	protected TimeStampText timeStamp;
	/** author is an DEMANDED attribute - may not be null**/
	protected LIST<STRING> author;
	/** organization is an DEMANDED attribute - may not be null**/
	protected LIST<STRING> organization;
	/** preprocessorVersion is an DEMANDED attribute - may not be null**/
	protected STRING preprocessorVersion;
	/** originatingSystem is an DEMANDED attribute - may not be null**/
	protected STRING originatingSystem;
	/** authorization is an DEMANDED attribute - may not be null**/
	protected STRING authorization;
	/**
	* The default constructor.
	**/
	public File_Name(){}

	/**
	* Constructs a new File_Name object using the given parameters.
	*
	* @param name DEMANDED parameter of type STRING - may not be null.
	* @param timeStamp DEMANDED parameter of type TimeStampText - may not be null.
	* @param author DEMANDED parameter of type LIST<STRING> - may not be null.
	* @param organization DEMANDED parameter of type LIST<STRING> - may not be null.
	* @param preprocessorVersion DEMANDED parameter of type STRING - may not be null.
	* @param originatingSystem DEMANDED parameter of type STRING - may not be null.
	* @param authorization DEMANDED parameter of type STRING - may not be null.
	**/
	public File_Name(STRING name, TimeStampText timeStamp, LIST<STRING> author, LIST<STRING> organization, STRING preprocessorVersion, STRING originatingSystem, STRING authorization)
	{
		this.name = name;
		this.timeStamp = timeStamp;
		this.author = author;
		this.organization = organization;
		this.preprocessorVersion = preprocessorVersion;
		this.originatingSystem = originatingSystem;
		this.authorization = authorization;
		resolveInverses();
	}

	/**
	 * This method initializes the File_Name object using the given parameters.
	*
	* @param name DEMANDED parameter of type STRING - may not be null.
	* @param timeStamp DEMANDED parameter of type TimeStampText - may not be null.
	* @param author DEMANDED parameter of type LIST<STRING> - may not be null.
	* @param organization DEMANDED parameter of type LIST<STRING> - may not be null.
	* @param preprocessorVersion DEMANDED parameter of type STRING - may not be null.
	* @param originatingSystem DEMANDED parameter of type STRING - may not be null.
	* @param authorization DEMANDED parameter of type STRING - may not be null.
	**/
	public void setParameters(STRING name, TimeStampText timeStamp, LIST<STRING> author, LIST<STRING> organization, STRING preprocessorVersion, STRING originatingSystem, STRING authorization)
	{
		this.name = name;
		this.timeStamp = timeStamp;
		this.author = author;
		this.organization = organization;
		this.preprocessorVersion = preprocessorVersion;
		this.originatingSystem = originatingSystem;
		this.authorization = authorization;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	@SuppressWarnings("unchecked")
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.name = (STRING) parameters.get(0);
		this.timeStamp = (TimeStampText) parameters.get(1);
		this.author = (LIST<STRING>) parameters.get(2);
		this.organization = (LIST<STRING>) parameters.get(3);
		this.preprocessorVersion = (STRING) parameters.get(4);
		this.originatingSystem = (STRING) parameters.get(5);
		this.authorization = (STRING) parameters.get(6);
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
		return File_Name.nonInverseAttributes;	}

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
		stepString = stepString.concat("FILE_NAME(");
		if(getRedefinedDerivedAttributeTypes().contains("name")) stepString = stepString.concat("*,");
		else{
		if(this.name != null)		stepString = stepString.concat(((RootInterface)this.name).getStepParameter(STRING.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("timeStamp")) stepString = stepString.concat("*,");
		else{
		if(this.timeStamp != null)		stepString = stepString.concat(((RootInterface)this.timeStamp).getStepParameter(TimeStampText.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("author")) stepString = stepString.concat("*,");
		else{
		if(this.author != null)		stepString = stepString.concat(((RootInterface)this.author).getStepParameter(STRING.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("organization")) stepString = stepString.concat("*,");
		else{
		if(this.organization != null)		stepString = stepString.concat(((RootInterface)this.organization).getStepParameter(STRING.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("preprocessorVersion")) stepString = stepString.concat("*,");
		else{
		if(this.preprocessorVersion != null)		stepString = stepString.concat(((RootInterface)this.preprocessorVersion).getStepParameter(STRING.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("originatingSystem")) stepString = stepString.concat("*,");
		else{
		if(this.originatingSystem != null)		stepString = stepString.concat(((RootInterface)this.originatingSystem).getStepParameter(STRING.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("authorization")) stepString = stepString.concat("*);");
		else{
		if(this.authorization != null)		stepString = stepString.concat(((RootInterface)this.authorization).getStepParameter(STRING.class.isInterface())+");");
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
	* This method sets the name attribute to the given value.
	*
	* @param name OPTIONAL value to set
	**/
	public void setname(STRING name)
	{
		this.name = name;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the name attribute.
	*
	* @return the value of name
	/**/
	public STRING getname()
	{
		return this.name;
	}

	/**
	* This method sets the timeStamp attribute to the given value.
	*
	* @param timeStamp OPTIONAL value to set
	**/
	public void settimeStamp(TimeStampText timeStamp)
	{
		this.timeStamp = timeStamp;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the timeStamp attribute.
	*
	* @return the value of timeStamp
	/**/
	public TimeStampText gettimeStamp()
	{
		return this.timeStamp;
	}

	/**
	* This method sets the author attribute to the given value.
	*
	* @param author OPTIONAL value to set
	**/
	public void setauthor(LIST<STRING> author)
	{
		this.author = author;
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the list of the author attribute.
	*
	* @return a copy of the author list
	**/
	public LIST<STRING> getauthor()
	{
		if(this.author != null)
			return new LIST<STRING>(this.author);
		return null;
	}

	/**
	* This method adds an STRING object to the author list.
	* @param author element to be appended to this list.
	**/
	public void addauthor(STRING author)
	{
		if(this.author == null)
			this.author = new LIST<STRING>();
		this.author.add(author);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of STRING objects to the author list.
	* @param author collection containing elements to be added to this list.
	**/
	public void addAllauthor(java.util.Collection<STRING> author)
	{
		if(this.author == null)
			this.author = new LIST<STRING>();
		this.author.addAll(author);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the author list.
	**/
	public void clearauthor()
	{
		if(this.author != null)
		{
			this.author.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an STRING object from the author list.
	* @param author element to be removed from this list.
	**/
	public void removeauthor(STRING author)
	{
		if(this.author != null)
		{
			this.author.remove(author);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of STRING objects from the author list.
	* @param author collection containing elements to be removed from this list.
	**/
	public void removeAllauthor(java.util.Collection<STRING> author)
	{
		if(this.author != null)
		{
			this.author.removeAll(author);
			fireChangeEvent();
		}
	}

	/**
	* This method sets the organization attribute to the given value.
	*
	* @param organization OPTIONAL value to set
	**/
	public void setorganization(LIST<STRING> organization)
	{
		this.organization = organization;
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the list of the organization attribute.
	*
	* @return a copy of the organization list
	**/
	public LIST<STRING> getorganization()
	{
		if(this.organization != null)
			return new LIST<STRING>(this.organization);
		return null;
	}

	/**
	* This method adds an STRING object to the organization list.
	* @param organization element to be appended to this list.
	**/
	public void addorganization(STRING organization)
	{
		if(this.organization == null)
			this.organization = new LIST<STRING>();
		this.organization.add(organization);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of STRING objects to the organization list.
	* @param organization collection containing elements to be added to this list.
	**/
	public void addAllorganization(java.util.Collection<STRING> organization)
	{
		if(this.organization == null)
			this.organization = new LIST<STRING>();
		this.organization.addAll(organization);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the organization list.
	**/
	public void clearorganization()
	{
		if(this.organization != null)
		{
			this.organization.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an STRING object from the organization list.
	* @param organization element to be removed from this list.
	**/
	public void removeorganization(STRING organization)
	{
		if(this.organization != null)
		{
			this.organization.remove(organization);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of STRING objects from the organization list.
	* @param organization collection containing elements to be removed from this list.
	**/
	public void removeAllorganization(java.util.Collection<STRING> organization)
	{
		if(this.organization != null)
		{
			this.organization.removeAll(organization);
			fireChangeEvent();
		}
	}

	/**
	* This method sets the preprocessorVersion attribute to the given value.
	*
	* @param preprocessorVersion OPTIONAL value to set
	**/
	public void setpreprocessorVersion(STRING preprocessorVersion)
	{
		this.preprocessorVersion = preprocessorVersion;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the preprocessorVersion attribute.
	*
	* @return the value of preprocessorVersion
	/**/
	public STRING getpreprocessorVersion()
	{
		return this.preprocessorVersion;
	}

	/**
	* This method sets the originatingSystem attribute to the given value.
	*
	* @param originatingSystem OPTIONAL value to set
	**/
	public void setoriginatingSystem(STRING originatingSystem)
	{
		this.originatingSystem = originatingSystem;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the originatingSystem attribute.
	*
	* @return the value of originatingSystem
	/**/
	public STRING getoriginatingSystem()
	{
		return this.originatingSystem;
	}

	/**
	* This method sets the authorization attribute to the given value.
	*
	* @param authorization OPTIONAL value to set
	**/
	public void setauthorization(STRING authorization)
	{
		this.authorization = authorization;
		fireChangeEvent();
	}

	/**
	* This method returns the value of the authorization attribute.
	*
	* @return the value of authorization
	/**/
	public STRING getauthorization()
	{
		return this.authorization;
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
		File_Name file_Name = new File_Name();
		if(this.name != null)
			file_Name.setname((STRING)this.name.clone());
		if(this.timeStamp != null)
			file_Name.settimeStamp((TimeStampText)this.timeStamp.clone());
		if(this.author != null)
			file_Name.setauthor((LIST<STRING>)this.author.clone());
		if(this.organization != null)
			file_Name.setorganization((LIST<STRING>)this.organization.clone());
		if(this.preprocessorVersion != null)
			file_Name.setpreprocessorVersion((STRING)this.preprocessorVersion.clone());
		if(this.originatingSystem != null)
			file_Name.setoriginatingSystem((STRING)this.originatingSystem.clone());
		if(this.authorization != null)
			file_Name.setauthorization((STRING)this.authorization.clone());
		return file_Name;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		File_Name file_Name = new File_Name();
		if(this.name != null)
			file_Name.setname(this.name);
		if(this.timeStamp != null)
			file_Name.settimeStamp(this.timeStamp);
		if(this.author != null)
			file_Name.setauthor(this.author);
		if(this.organization != null)
			file_Name.setorganization(this.organization);
		if(this.preprocessorVersion != null)
			file_Name.setpreprocessorVersion(this.preprocessorVersion);
		if(this.originatingSystem != null)
			file_Name.setoriginatingSystem(this.originatingSystem);
		if(this.authorization != null)
			file_Name.setauthorization(this.authorization);
		return file_Name;
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
