/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.step.parser.util;
import java.util.ArrayList;
import ifc2x3javatoolbox.ifc2x3tc1.CloneableObject;
/**
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class NodeObject implements CloneableObject
{
	Integer lineNumber = null;
	String className = null;
	ArrayList<CloneableObject> parameters = null;

	public NodeObject()
	{
		parameters = new ArrayList<CloneableObject>(20);
	}

	public NodeObject(Integer lineNumber, String className,
			ArrayList<CloneableObject> parameters)
	{
		this.lineNumber = lineNumber;
		this.className = className;
		this.parameters = parameters;
	}

	public void reset() {
		lineNumber = null;
		className = null;
		parameters = new ArrayList<CloneableObject>(20);

	}

	public void addParameter(CloneableObject parameter) {
		this.parameters.add(parameter);
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = new Integer(lineNumber);
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = Integer.parseInt(lineNumber);
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<CloneableObject> getParameter() {
		parameters.trimToSize();
		return parameters;
	}

	public void setParameter(ArrayList<CloneableObject> parameter) {
		this.parameters = parameter;
	}

	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError("Clone not supported");
		}
	}
}